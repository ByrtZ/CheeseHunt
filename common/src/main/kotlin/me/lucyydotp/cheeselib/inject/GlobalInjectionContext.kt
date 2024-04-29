package me.lucyydotp.cheeselib.inject

/** The global injection context. */
object GlobalInjectionContext : InjectionContextImpl()

/** Binds this object in the [global injection context][GlobalInjectionContext]. */
inline fun <reified T : Any> T.bindGlobally() = also {
    GlobalInjectionContext.bind(this)
}
