package xyz.jmullin.drifter.rendering

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import xyz.jmullin.drifter.entity.Layer2D

/**
 * Convenience object for shader switching.  Use switch in the context of a Layer2D to update
 * the current shader for that layer/batch and handle the context switching and flushing automatically.
 */
object Shaders {
    init {
        ShaderProgram.pedantic = true
    }

    val default = ShaderSet("default", "default")

    fun switch(s: ShaderSet, layer: Layer2D, batch: SpriteBatch) {
        layer.currentShader = s
        batch.flush()
        batch.shader = s.program

        s.refresh()
        s.init()
        s.tick()
    }
}