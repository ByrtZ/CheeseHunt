package me.lucyydotp.cheeselib.inject

import kotlin.reflect.KClass

/**
 * A simple map-based [InjectionContext].
 */
open class InjectionContextImpl : InjectionContext {

    private val values: MutableMap<KClass<*>, Any> = mutableMapOf()

    override fun <T : Any> bind(clazz: KClass<in T>, value: T) {
        check(!values.containsKey(clazz)) { "Can't bind to ${clazz.simpleName} because it already has a bound value" }
        values[clazz] = value
    }

    override fun <T : Any> unbind(clazz: KClass<in T>, value: T?) {
        if (value != null && values[clazz] !== value) return
        values.remove(clazz)
    }


    @Suppress("UNCHECKED_CAST")
    override fun <T : Any> injectOrNull(clazz: KClass<T>): T? = values[clazz] as T?

}

/**
 * An injection context that defers to [parentContext] when getting bound values
 * if this context does not have a value directly.
 */
class CascadingInjectionContext(private val parentContext: InjectionContext) : InjectionContextImpl() {
    override fun <T : Any> injectOrNull(clazz: KClass<T>): T? = super.injectOrNull(clazz) ?: parentContext.injectOrNull(clazz)
}
