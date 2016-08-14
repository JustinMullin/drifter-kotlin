package xyz.jmullin.drifter.animation

import xyz.jmullin.drifter.entity.Entity

/**
 * Trigger which counts down some amount of time and executes when the time has elapsed.
 *
 * @param duration Time in seconds this trigger should wait before executing.
 * @param done Block to execute when finished.
 */
open class Timer(var duration: Float, done: () -> Unit) : Trigger(done) {
    /**
     * Total time elapsed since this timer was activated.
     */
    var elapsed = 0f

    /**
     * Returns true if the timer has not yet elapsed its duration.
     */
    override val running: Boolean get() = elapsed < duration

    override fun update(delta: Float, e: Entity) {
        elapsed += delta
        if(elapsed >= duration) {
            execute()
        }
    }
}
