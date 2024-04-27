package me.lucyydotp.cheeselib.inject

import kotlin.reflect.KClass

/**
 * A context to use when injecting dependencies.
 */
interface InjectionContext : InjectionSite {

    /**
     * Binds a [value] to [clazz].
     * @throws IllegalStateException if [clazz] is already bound to a value in this direct context
     */
    fun <T : Any> bind(clazz: KClass<in T>, value: T)

    /**
     * Unbinds a [value] from [clazz].
     *
     * If [value] if specified, then [clazz]'s bound value will only be unbound if it is referentially equal to [value].
     * Otherwise, the current value is unbound.
     *
     * If no value is bound, does nothing.
     */
    fun <T : Any> unbind(clazz: KClass<in T>, value: T? = null)
}

/**
 * Binds a [value] to type [T].
 * @see InjectionContext.bind
 */
inline fun <reified T : Any> InjectionContext.bind(value: T) = value.also { bind(T::class, value) }

/**
 * Unbinds a [value] from type [T].
 * @see InjectionContext.unbind
 */
inline fun <reified T : Any> InjectionContext.unbind(value: T?) = unbind(T::class, value)
