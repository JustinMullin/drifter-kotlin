package xyz.jmullin.drifter.animation

import xyz.jmullin.drifter.extensions.Pi
import xyz.jmullin.drifter.extensions.sin

/**
 * Defines an easing function for interpolation between 0-1.
 *
 * @param interpolate Function to use in interpolating; takes a linear alpha and returns the interpolated alpha.
 */
open class Easing(val interpolate: (Float) -> Float) {
    fun apply(n: Float) = interpolate(n)

    /**
     * Commonly used easing functions.
     */
    companion object {
        object Linear : Easing({n -> n})
        object Sine : Easing({n -> (1 + ((n-0.5f)*Pi).sin()) / 2f})
        object SineBack : Easing({n -> (n*Pi*2f - Pi/2f).sin()/2f + 0.5f})
    }
}