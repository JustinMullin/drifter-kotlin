package xyz.jmullin.drifter.rendering

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

    val default = ShaderSet("default", "default")

    fun switch(s: ShaderSet, batch: SpriteBatch) {
        batch.flush()
        batch.shader = s.program

        s.init()
        s.update()
    }
}

fun shader(fragShader: String, vertShader: String, tick: (ShaderProgram) -> Unit = {}): ShaderSet {
    return object : ShaderSet(fragShader, vertShader) {
        override fun tick() {
            program?.let { tick(it) }
            super.tick()
        }
    }
}