package xyz.jmullin.drifter.timing

import xyz.jmullin.drifter.entity.Entity

class Looper(initial: Float = 0f, val max: Float = 1f, speed: Float = 1f, val onLoop: () -> Unit = {}) : Ticker(initial, speed) {
    override fun update(delta: Float, e: Entity) {
        super.update(delta, e)

        while (value >= max) {
            value -= max
            onLoop()
        }
    }
}