package me.lucyydotp.cheeselib.inject

import me.lucyydotp.cheeselib.util.ReadOnlyDelegate
import kotlin.reflect.KClass

/**
 * A site at which bound objects can be injected.
 */
interface InjectionSite {

    /**
     * Gets the bound value for [clazz], or null if no value is bound
     */
    fun <T : Any> getOrNull(clazz: KClass<T>): T?

    /**
     * Gets the bound value for [clazz].
     * @throws IllegalStateException if no value is bound to [clazz]
     */
    fun <T : Any> get(clazz: KClass<T>): T =
        checkNotNull(getOrNull(clazz)) { "Attempt to retrieve unbound type ${clazz.simpleName}" }
}

/**
 * Gets the bound value for [T].
 * @see InjectionSite.get
 */
inline fun <reified T : Any> InjectionSite.get() = get(T::class)

/**
 * Optionally gets the bound value for [T].
 * @see InjectionSite.getOrNull
 */
inline fun <reified T : Any> InjectionSite.getOrNull() = getOrNull(T::class)

/**
 * Creates a delegate that gets the bound value for [T] on each access.
 * @see InjectionSite.get
 */
inline fun <reified T : Any> InjectionSite.context() = ReadOnlyDelegate { get<T>() }
