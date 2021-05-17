package com.github.yamamotoj.cachedproperty

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible

/**
 * Creates a property delegate that caches the value returned by [initializer] until [CachedProperty.invalidate] is called
 *
 * @see cached
 */
class CachedProperty<out T>(val initializer: () -> T) : ReadOnlyProperty<Any?, T>, Invalidatable {
    private var cachedValue: CachedValue<T> = CachedValue.Invalid

    /** Returns `true` when the cached value is still valid, `false` otherwise */
    internal val isValid get() = cachedValue !is CachedValue.Invalid

    override fun getValue(thisRef: Any?, property: KProperty<*>): T =
        when (val currentCachedValue = cachedValue) {
            CachedValue.Invalid -> initializer().also { cachedValue = CachedValue.Value(it) }
            is CachedValue.Value<T> -> currentCachedValue.value
        }

    override fun invalidate() {
        cachedValue = CachedValue.Invalid
    }

    /** Internal class defining cached value states */
    private sealed class CachedValue<out T> {
        object Invalid : CachedValue<Nothing>()
        class Value<out T>(val value: T) : CachedValue<T>()
    }
}

/**
 * Creates a property delegate that caches the value returned by [initializer] until [CachedProperty.invalidate] is called
 *
 * The created property delegate is similar to kotlin `lazy { }` property delegate.
 * The [initializer] is called once first value request is made.
 * After this moment that's the value returned on subsequent getter calls.
 *
 * The main difference from `lazy { }` comes when the [CachedProperty.invalidate] method is called on this delegate object.
 * It will clear the cache and the [initializer] will be called again on next getter request.
 *
 * To access the delegate after its assignment use the `::property` syntax.
 *
 * Example usage:
 *
 *      class SomeClass {
 *
 *          val myData by cached { Random(10).nextInt() }
 *
 *          // Clears cached value and makes the `cached`
 *          // initializer block run again on next `myData` request
 *          fun revoke() = ::myData.invalidateCache()
 *      }
 */
fun <T> cached(initializer: () -> T): CachedProperty<T> = CachedProperty(initializer)

/**
 * Invalidates this property *delegate* cache, scheduling a new computation on next access.
 *
 * This method has effect only if the provided property is delegated with [CachedProperty] instance.
 *
 *      val myData by cached { println("Computed") }
 *
 * @see cached
 */
fun KProperty0<*>.invalidateCache() {
    (getDelegate() as? CachedProperty<*>)?.also {
        if (it.isValid) {
            isAccessible = true
            it.invalidate()
            isAccessible = false
        }
    }
}

/** Calls [invalidateCache] on each property */
fun Iterable<KProperty0<*>>.invalidateAllCaches() = forEach { it.invalidateCache() }

/** Calls [invalidateCache] on each property */
fun Sequence<KProperty0<*>>.invalidateAllCaches() = forEach { it.invalidateCache() }
