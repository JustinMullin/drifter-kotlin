package xyz.jmullin.drifter.animation

import xyz.jmullin.drifter.extensions.FloatMath.cos
import xyz.jmullin.drifter.extensions.FloatMath.pow
import xyz.jmullin.drifter.extensions.FloatMath.sin
import xyz.jmullin.drifter.extensions.FloatMath.sqrt
import xyz.jmullin.drifter.extensions.Pi
import xyz.jmullin.drifter.extensions.sin

/**
 * Defines an easing function for interpolation between 0-1.
 *
 * @param interpolate Function to use in interpolating; takes a linear alpha and returns the interpolated alpha.
 */
open class Easing(val interpolate: (Float) -> Float) {
    /**
     * Applies an alpha to the given interpolation function.
     */
    fun apply(a: Float) = interpolate(a)
}

/** Commonly used easing functions */
object Linear: Easing({a -> a})
object SineIn: Easing({ a -> (1 + sin((a-0.5f)*Pi)) / 2f})
object SineSlope: Easing({a -> 1.5708f * cos(Pi * (-0.5f + a))})
object SineInOut: Easing({ a -> (a*Pi*2f - Pi/2f).sin()/2f + 0.5f})
object Parabolic: Easing({a -> -pow(a*2f-1f, 2f)+1f})
object CircIn: Easing({ a -> -(sqrt(1f - a*a) - 1f) })
object CircOut: Easing({ a -> sqrt(1 - (a-1)*(a-1)) })