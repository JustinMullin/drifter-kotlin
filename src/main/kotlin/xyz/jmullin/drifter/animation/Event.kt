package xyz.jmullin.drifter.animation

import xyz.jmullin.drifter.entity.Entity

/**
 * A trigger which will execute immediately upon starting.
 */
class Event(done: () -> Unit) : Trigger(done) {
    var executed = false

    /**
     * When true, this event is still executing. Events are expected to fall off their containers
     * when they are finished running (running is false).
     */
    override val running: Boolean = false

    /**
     * Called once per tick to update this event's state.
     */
    override fun update(delta: Float, e: Entity) {
        if(!executed) execute()
        executed = true
    }
}
