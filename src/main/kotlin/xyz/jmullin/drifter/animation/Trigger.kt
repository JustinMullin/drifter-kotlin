package xyz.jmullin.drifter.animation

import xyz.jmullin.drifter.entity.Entity
import xyz.jmullin.drifter.entity.Hook

/**
 * A trigger abstracts an action to take place at some future time.  To start the trigger,
 * call go() from the context of some entity.  This will add a hook to run the trigger,
 * which will update until it finishes.
 *
 * Triggers can be chained using 'triggerA then triggerB.'  This will set up the second trigger
 * to go() after the first has completed.
 *
 * @param f Block to execute when the trigger finishes.
 */
abstract class Trigger(val f: () -> Unit) : Hook {
    var parent: Entity? = null
    var next: Trigger? = null

    /**
     * Start the trigger, adding it to the entity in context to run until finished.
     *
     * @return this
     */
    open fun go(parent: Entity): Trigger {
        this.parent = parent
        parent.add(this)
        parent.let { update(0f, it) }
        return this
    }

    /**
     * Method to be called when this trigger has completed.  The expectation is that this
     * method will be called from the update() method in any trigger implementation.
     *
     * @return this
     */
    fun execute(): Trigger {
        f()
        parent?.let { next?.go(it) }
        return this
    }

    /**
     * Returns the last trigger in the trigger chain, or this trigger if there are none after it.
     */
    fun last(): Trigger = next?.last() ?: this

    /**
     * Use to schedule another trigger to execute after this one has finished.
     *
     * @param t Trigger to scheduled next.
     * @return this
     */
    infix fun then(t: Trigger): Trigger {
        last().next = t
        return this
    }

    /**
     * Utility methods for easily creating the various types of triggers.
     */
    companion object {
        fun event(done: () -> Unit) = Event(done)
        fun delay(duration: Float, done: () -> Unit) = Timer(duration, done)
        fun tween(duration: Float, tick: (Float) -> Unit) = Tween(duration, tick, {})
        fun tweenDone(duration: Float, tick: (Float) -> Unit, done: () -> Unit) = Tween(duration, tick, done)
    }
}