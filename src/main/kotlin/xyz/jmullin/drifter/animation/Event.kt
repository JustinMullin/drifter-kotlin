package xyz.jmullin.drifter.animation

import xyz.jmullin.drifter.entity.Entity

/**
 * A trigger which will execute immediately upon starting.
 */
class Event(done: () -> Unit) : Trigger(done) {
    override val running: Boolean = false

    override fun go(): Trigger {
        execute()
        return super.go()
    }

    override fun update(delta: Float, e: Entity) {}
}
