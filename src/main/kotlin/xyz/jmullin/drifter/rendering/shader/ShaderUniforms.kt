package xyz.jmullin.drifter.rendering.shader

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Matrix3
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

abstract class ShaderUniform<T>(internal val program: ShaderProgram, internal val name: String, open val tick: (() -> T)?) {
    abstract fun set(value: T)
    fun setFromTick() {
        tick?.let { set(it()) }
    }
}

class BooleanUniform(program: ShaderProgram, name: String,
                   override val tick: (() -> Boolean)?) : ShaderUniform<Boolean>(program, name, tick) {
    override fun set(value: Boolean) { program.setUniformi(name, if(value) 1 else 0) }
}

class IntUniform(program: ShaderProgram, name: String,
                 override val tick: (() -> Int)?) : ShaderUniform<Int>(program, name, tick) {
    override fun set(value: Int) { program.setUniformi(name, value) }
}

class FloatUniform(program: ShaderProgram, name: String,
                   override val tick: (() -> Float)?) : ShaderUniform<Float>(program, name, tick) {
    override fun set(value: Float) { program.setUniformf(name, value) }
}

class Vector2Uniform(program: ShaderProgram, name: String,
                     override val tick: (() -> Vector2)?) : ShaderUniform<Vector2>(program, name, tick) {
    override fun set(value: Vector2) { program.setUniformf(name, value) }
}

class Vector3Uniform(program: ShaderProgram, name: String,
                     override val tick: (() -> Vector3)?) : ShaderUniform<Vector3>(program, name, tick) {
    override fun set(value: Vector3) { program.setUniformf(name, value) }
}

class Matrix3Uniform(program: ShaderProgram, name: String,
                     override val tick: (() -> Matrix3)?) : ShaderUniform<Matrix3>(program, name, tick) {
    override fun set(value: Matrix3) { program.setUniformMatrix(name, value) }
}

class Matrix4Uniform(program: ShaderProgram, name: String,
                     override val tick: (() -> Matrix4)?) : ShaderUniform<Matrix4>(program, name, tick) {
    override fun set(value: Matrix4) { program.setUniformMatrix(name, value) }
}

class ColorUniform(program: ShaderProgram, name: String,
                   override val tick: (() -> Color)?) : ShaderUniform<Color>(program, name, tick) {
    override fun set(value: Color) { program.setUniformf(name, value) }
}