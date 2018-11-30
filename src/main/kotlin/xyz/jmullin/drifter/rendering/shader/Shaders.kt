package xyz.jmullin.drifter.rendering.shader

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShaderProgram

/**
 * Convenience object for shader switching.  Use switch in the context of a Layer2D to update
 * the current shader for that layer/batch and handle the context switching and flushing automatically.
 */
object Shaders {
    init {
        ShaderProgram.pedantic = false
    }

    val default = shader("default", "default")

    fun switch(s: ShaderSet, batch: SpriteBatch) {
        batch.flush()
        batch.shader = s.program
        batch.flush()

        s.update()
    }
}

/**
 * Convenience constructor for a shader. Provides a no-op block intended for placing side-effecting
 * uniform delegate creators.
 */
fun shader(fragShader: String, vertShader: String = "default", init: ShaderSet.() -> Unit = {}): ShaderSet {
    return ShaderSet(fragShader, vertShader).apply(init)
}
