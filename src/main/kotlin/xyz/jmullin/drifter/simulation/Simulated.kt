package xyz.jmullin.drifter.simulation

interface Simulated<T : Simulated<T>> {
    fun beforeStep(entities: List<T>)
    fun simulate(delta: Float, entities: List<T>): Unit
    val maxStepSize: Float
    val interactive: Boolean
}