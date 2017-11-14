package xyz.jmullin.drifter.rendering

import com.badlogic.gdx.graphics.Color

class FloatColor(r: Float, g: Float, b: Float, a: Float) : Color(r, g, b, a) {
    override fun clamp(): Color = this
}