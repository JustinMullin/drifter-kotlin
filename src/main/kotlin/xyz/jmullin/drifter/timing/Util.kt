package xyz.jmullin.drifter.timing

import xyz.jmullin.drifter.entity.Entity2D

fun Entity2D.looper(initial: Float = 0f, max: Float = 1f, speed: Float = 1f, onLoop: () -> Unit = {}): Looper {
    return add(Looper(initial, max, speed, onLoop))
}

fun Entity2D.ticker(initial: Float = 0f, speed: Float = 1f): Ticker {
    return add(Ticker(initial, speed))
}