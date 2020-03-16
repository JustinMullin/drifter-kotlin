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

typealias Uniform<T> = (program: ShaderProgram, name: String) -> (T) -> Unit

object Uniforms {
    val boolean: Uniform<Boolean> = { p, name -> { value -> p.setUniformi(name, if(value) 1 else 0) } }
    val int: Uniform<Int> = { p, name -> { value -> p.setUniformi(name, value) } }
    val float: Uniform<Float> = { p, name -> { value -> p.setUniformf(name, value) } }
    val vector2: Uniform<Vector2> = { p, name -> { value -> p.setUniformf(name, value) } }
    val vector3: Uniform<Vector3> = { p, name -> { value -> p.setUniformf(name, value) } }
    val matrix3: Uniform<Matrix3> = { p, name -> { value -> p.setUniformMatrix(name, value) } }
    val matrix4: Uniform<Matrix4> = { p, name -> { value -> p.setUniformMatrix(name, value) } }
    val color: Uniform<Color> = { p, name -> { value -> p.setUniformf(name, value) } }
}

class BooleanUniform(program: ShaderProgram, name: String,
                   override val tick: (() -> Boolean)?) : ShaderUniform<Boolean>(program, name, tick) {
    override fun set(value: Boolean) = Uniforms.boolean(program, name)(value)
}

class IntUniform(program: ShaderProgram, name: String,
                 override val tick: (() -> Int)?) : ShaderUniform<Int>(program, name, tick) {
    override fun set(value: Int) = Uniforms.int(program, name)(value)
}

class FloatUniform(program: ShaderProgram, name: String,
                   override val tick: (() -> Float)?) : ShaderUniform<Float>(program, name, tick) {
    override fun set(value: Float) = Uniforms.float(program, name)(value)
}

class Vector2Uniform(program: ShaderProgram, name: String,
                     override val tick: (() -> Vector2)?) : ShaderUniform<Vector2>(program, name, tick) {
    override fun set(value: Vector2) = Uniforms.vector2(program, name)(value)
}

class Vector3Uniform(program: ShaderProgram, name: String,
                     override val tick: (() -> Vector3)?) : ShaderUniform<Vector3>(program, name, tick) {
    override fun set(value: Vector3) = Uniforms.vector3(program, name)(value)
}

class Matrix3Uniform(program: ShaderProgram, name: String,
                     override val tick: (() -> Matrix3)?) : ShaderUniform<Matrix3>(program, name, tick) {
    override fun set(value: Matrix3) = Uniforms.matrix3(program, name)(value)
}

class Matrix4Uniform(program: ShaderProgram, name: String,
                     override val tick: (() -> Matrix4)?) : ShaderUniform<Matrix4>(program, name, tick) {
    override fun set(value: Matrix4) = Uniforms.matrix4(program, name)(value)
}

class ColorUniform(program: ShaderProgram, name: String,
                   override val tick: (() -> Color)?) : ShaderUniform<Color>(program, name, tick) {
    override fun set(value: Color) = Uniforms.color(program, name)(value)
}

class ArrayUniform<T>(program: ShaderProgram, name: String, private val subUniform: Uniform<T>,
                           override val tick: (() -> List<T>)?) : ShaderUniform<List<T>>(program, name, tick) {
    override fun set(value: List<T>) { value.forEachIndexed { i, v -> subUniform(program, "$name[$i]")(v) } }
}
