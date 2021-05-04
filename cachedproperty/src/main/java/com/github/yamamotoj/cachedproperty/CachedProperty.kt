package com.github.yamamotoj.cachedproperty

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Defines a read only cached property delegate
 *
 * This property delegate works as the kotlin `lazy { }` property delegate,
 * but it can also be invalidated, triggering a new computation of the [initializer] function
 */
class CachedProperty<out T>(val initializer: () -> T) : ReadOnlyProperty<Any?, T>, Invalidatable {
    private var cachedValue: CachedValue<T> = CachedValue.Invalid

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
