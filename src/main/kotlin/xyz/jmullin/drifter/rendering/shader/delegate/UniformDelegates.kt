package xyz.jmullin.drifter.rendering.shader.delegate

import xyz.jmullin.drifter.rendering.shader.ShaderSet
import xyz.jmullin.drifter.rendering.shader.ShaderUniform
import kotlin.reflect.KProperty

abstract class UniformDelegate<out T : ShaderUniform<*>> {
    companion object {
        fun <T : ShaderUniform<*>> make(shaderSet: ShaderSet, constructor: (String) -> T) = object : UniformDelegate<T>() {
            override fun initUniform(name: String) = constructor(name).apply { shaderSet.uniforms += this }
        }
    }

    private var uniform: T? = null
    abstract fun initUniform(name: String): T

    operator fun getValue(requestor: Any?, property: KProperty<*>): T {
        if(uniform == null) uniform = initUniform(property.name)
        return uniform!!
    }
}