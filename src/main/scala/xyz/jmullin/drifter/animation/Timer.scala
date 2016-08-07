package xyz.jmullin.drifter.animation

import xyz.jmullin.drifter.entity.Entity

/**
 * Trigger which counts down some amount of time and executes when the time has elapsed.
 *
 * @param duration Time in seconds this trigger should wait before executing.
 * @param done Block to execute when finished.
 */
class Timer(var duration: Float, done: -> Unit) extends Trigger(done) {
  var elapsed = 0f
  override fun running: Boolean = elapsed < duration

  override fun update(implicit delta: Float, e: Entity) {
    elapsed += delta
    if(elapsed >= duration) {
      execute()
    }
  }
}
