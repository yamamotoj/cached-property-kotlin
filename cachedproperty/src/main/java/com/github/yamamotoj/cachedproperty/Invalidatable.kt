package com.github.yamamotoj.cachedproperty

/** Defines something that can be invalidated */
interface Invalidatable {

    /** Invalidates this object */
    fun invalidate()
}

/** Invalidates all [Invalidatable] in this [Iterable] */
fun <T : Invalidatable> Iterable<T>.invalidateAll() = forEach { it.invalidate() }

/** Invalidates all [Invalidatable] in this [Sequence] */
fun <T : Invalidatable> Sequence<T>.invalidateAll() = forEach { it.invalidate() }