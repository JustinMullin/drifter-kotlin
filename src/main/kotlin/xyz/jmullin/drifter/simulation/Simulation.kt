package xyz.jmullin.drifter.simulation

import xyz.jmullin.drifter.entity.Entity2D
import xyz.jmullin.drifter.entity.EntityContainer2D
import xyz.jmullin.drifter.extensions.FloatMath

class Simulation<T : Simulated<T>>(val minStepSize: Float, _entities: List<T> = emptyList()) : Entity2D() {
    var entities = _entities

    override fun create(container: EntityContainer2D) {
        depth = Float.MIN_VALUE

        super.create(container)
    }

    override fun update(delta: Float) {
        var stepped = 0f
        val stepSize = minStepSize //FloatMath.max(minStepSize, entities.minBy { it.maxStepSize }?.maxStepSize ?: 0f)

        entities.forEach { it.beforeStep(entities) }

        while(stepped < delta) {
            entities.forEach { it.simulate(stepSize, entities.filter { it.interactive }) }

            stepped += stepSize
        }

        super.update(delta)
    }
}