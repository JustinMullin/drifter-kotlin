package xyz.jmullin.drifter.animation

import xyz.jmullin.drifter.entity.Entity

/**
 * A trigger which will execute immediately upon starting.
 */
class Event(done: -> Unit) extends Trigger(done) {
  override fun running: Boolean = false

  override fun go()(implicit e: Entity): Trigger = {
    execute()
    super.go()
  }

  override fun update(implicit delta: Float, e: Entity) {}
}
