package xyz.jmullin.drifter.timing

import xyz.jmullin.drifter.entity.Entity
import xyz.jmullin.drifter.entity.Hook

open class Ticker(initial: Float = 0f, var speed: Float = 1f) : Hook {
    var value = initial
    override val valid: Boolean = true

    override fun update(delta: Float, e: Entity) {
        value += delta * speed
    }
}