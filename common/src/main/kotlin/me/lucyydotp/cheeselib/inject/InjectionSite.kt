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
    fun <T : Any> injectOrNull(clazz: KClass<T>): T?

    /**
     * Gets the bound value for [clazz].
     * @throws IllegalStateException if no value is bound to [clazz]
     */
    fun <T : Any> inject(clazz: KClass<T>): T =
        checkNotNull(injectOrNull(clazz)) { "Attempt to retrieve unbound type ${clazz.simpleName}" }
}

/**
 * Gets the bound value for [T].
 * @see InjectionSite.inject
 */
inline fun <reified T : Any> InjectionSite.inject() = inject(T::class)

/**
 * Optionally gets the bound value for [T].
 * @see InjectionSite.injectOrNull
 */
inline fun <reified T : Any> InjectionSite.injectOrNull() = injectOrNull(T::class)

/**
 * Creates a delegate that gets the bound value for [T] on each access.
 * @see InjectionSite.inject
 */
inline fun <reified T : Any> InjectionSite.context() = ReadOnlyDelegate { inject<T>() }
