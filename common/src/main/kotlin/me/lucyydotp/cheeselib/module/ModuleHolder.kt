package me.lucyydotp.cheeselib.module

import me.lucyydotp.cheeselib.inject.InjectionContext

/**
 * A container that manages a set of modules.
 * @param ThisT self-referential generic
 */
interface ModuleHolder : InjectionContext {
    val modules: Set<Module>
}
