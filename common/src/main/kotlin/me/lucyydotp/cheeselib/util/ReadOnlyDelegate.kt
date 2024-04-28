package me.lucyydotp.cheeselib.util

import kotlin.reflect.KProperty

/**
 * A simple read-only delegate for SAM-conversion.
 */
fun interface ReadOnlyDelegate<T> {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = get()

    /** Gets the delegate's value. */
    fun get(): T
}
