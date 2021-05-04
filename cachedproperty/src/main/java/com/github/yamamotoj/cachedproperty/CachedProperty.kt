package com.github.yamamotoj.cachedproperty

import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class CachedProperty<out T>(val loader: () -> T) : ReadOnlyProperty<Any, T> {
    private var cachedValue: CachedValue<T> = CachedValue.Invalid

    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        val cachedValue = this.cachedValue
        return when (cachedValue) {
            CachedValue.Invalid -> loader().also { this.cachedValue = CachedValue.Value(it) }
            is CachedValue.Value<T> -> cachedValue.value
        }
    }

    fun invalidate() {
        cachedValue = CachedValue.Invalid
    }

    private sealed class CachedValue<out T> {
        object Invalid : CachedValue<Nothing>()
        class Value<out T>(val value: T) : CachedValue<T>()
    }
}
