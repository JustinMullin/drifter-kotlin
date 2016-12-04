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
    var previous: Trigger? = null
    var next: Trigger? = null

    /**
     * Start the trigger, adding it to the entity in context to run until finished.
     * This will bubble up the "go" trigger to the first in the chain if multiple
     * triggers have been combined together using 'then'
     *
     * @return this
     */
    open infix fun go(parent: Entity): Trigger {
        if(previous == null) {
            goNow(parent)
        } else {
            previous?.go(parent)
        }
        return this
    }

    /**
     * Starts the trigger immediately, ignoring any previous triggers in the chain, if any.
     */
    fun goNow(parent: Entity) {
        this.parent = parent
        parent.add(this)
        parent.let { update(0f, it) }
    }

    /**
     * Will be called when this trigger has completed.
     *
     * @return this
     */
    fun execute(): Trigger {
        f()
        parent?.let { next?.goNow(it) }
        return this
    }

    /**
     * Returns the first trigger in the trigger chain, or this trigger if there are none before it.
     */
    fun first(): Trigger = previous?.first() ?: this

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
        next = t
        t.previous = this
        return t
    }

    /**
     * Convenience block for expressing events as simple blocks.
     */
    infix fun then(done: () -> Unit) = then(event(done))
}

infix fun (() -> Unit).then(t: Trigger) = event(this) then(t)
infix fun (() -> Unit).go(parent: Entity) = event(this) go(parent)

/**
 * Utility methods for easily creating the various types of triggers.
 */
fun event(done: () -> Unit) = Event(done)
fun delay(duration: Float, done: () -> Unit) = Timer(duration, done)
fun tween(duration: Float, easing: Easing, tick: (Float) -> Unit) = Tween(duration, tick, {}, easing)
fun tween(duration: Float, tick: (Float) -> Unit) = Tween(duration, tick, {})
fun tweenDone(duration: Float, tick: (Float) -> Unit, done: () -> Unit) = Tween(duration, tick, done)