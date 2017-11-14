package xyz.jmullin.drifter.entity

/**
 * Basic unit of simulation.  An entity can be updated and contain hooks which it
 * will update in turn.
 */
open class Entity {
    /**
     * Hooks this entity is currently managing.
     */
    var hooks = emptySet<Hook>()

    /**
     * Add a hook to be managed by this entity.
     *
     * @param h Hook to add.
     */
    fun <T : Hook> add(h: T): T {
        hooks += h
        return h
    }

    /**
     * Remove a hook from management by this entity.
     *
     * @param h Hook to remove.
     */
    fun remove(h: Hook) {
        hooks -= h
    }

    /**
     * Remove all hooks from this entity.
     */
    fun clearHooks() {
        hooks = emptySet()
    }

    /**
     * Called to update the entity and process any child hooks.
     *
     * @param delta Time in seconds elapsed since the last update tick.
     */
    open fun update(delta: Float) {
        hooks.forEach { it.update(delta, this) }
        hooks = hooks.filter(Hook::valid).toSet()
    }
}
