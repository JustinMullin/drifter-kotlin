package xyz.jmullin.drifter.animation

import xyz.jmullin.drifter.entity._

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
abstract class Trigger(f: -> Unit) extends Hook {
  var next: Option[Trigger] = None

  /**
   * Start the trigger, adding it to the entity in context to run until finished.
   *
   * @param e Implicit entity to add this trigger to.
   * @return this
   */
  fun go()(implicit e: Entity) = {
    e.add(this)
    update(0, e)
    this
  }

  /**
   * Method to be called when this trigger has completed.  The expectation is that this
   * method will be called from the update() method in any trigger implementation.
   *
   * @param e Implicit entity context this trigger is executing under.
   * @return this
   */
  fun execute()(implicit e: Entity) = {
    f
    next.forEach(it.go())
    this
  }

  /**
   * Returns the last trigger in the trigger chain, or this trigger if there are none after it.
   */
  fun last: Trigger = next match {
    case n -> n.last
    case None -> this
  }

  /**
   * Use to schedule another trigger to execute after this one has finished.
   *
   * @param t Trigger to scheduled next.
   * @param e Implicit entity to schedule the new trigger under.
   * @return this
   */
  fun >>(t: Trigger)(implicit e: Entity) = {
    last.next = t
    this
  }
}

/**
 * Utility methods for easily creating the various types of triggers.
 */
object Trigger {
  fun event(done: -> Unit)(implicit e: Entity2D) = new Event(done)
  fun delay(duration: Float)(done: -> Unit)(implicit e: Entity2D) = new Timer(duration, done)
  fun tween(duration: Float)(tick: Float -> Unit)(implicit e: Entity2D) = new Tween(duration, tick)
  fun tweenDone(duration: Float)(tick: Float -> Unit)(done: -> Unit)(implicit e: Entity2D) = new Tween(duration, tick, done)
}