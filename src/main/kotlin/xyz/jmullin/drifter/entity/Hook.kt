package xyz.jmullin.drifter.entity

/**
 * A hook abstracts the concept of some process which is added to an entity, updated on each frame,
 * and may eventually "fall off" when finished.
 */
interface Hook {
    /**
     * Entities will generally detach a hook which is no longer valid.
     */
    val valid: Boolean

    /**
     * Called each tick to update the hook.
     */
    fun update(delta: Float, e: Entity)
}
