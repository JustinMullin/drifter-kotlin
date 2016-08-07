package xyz.jmullin.drifter.entity

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.GL20.GL_BACK
import com.badlogic.gdx.graphics.GL20.GL_LEQUAL
import com.badlogic.gdx.graphics.g3d.Attributes
import com.badlogic.gdx.graphics.g3d.Renderable
import com.badlogic.gdx.graphics.g3d.Shader
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute
import com.badlogic.gdx.graphics.g3d.attributes.CubemapAttribute
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader
import com.badlogic.gdx.graphics.glutils.ShaderProgram
import com.badlogic.gdx.math.Matrix3
import com.badlogic.gdx.math.Matrix4
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import xyz.jmullin.drifter.extensions.*
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader.Setter as SetterGDX

/**
 * Basic 3D LibGDX shader implementation in Scala, based on [[DefaultShader]].
 */
class DrifterShader3D(var renderable: Renderable) : BaseShader() {
    
    init {
        register(cameraProjection)
        register(cameraView)
        register(cameraCombined)
        register(cameraPosition)
        register(cameraDirection)
        register(cameraUp)
        register(worldTransform)
        register(viewWorldTransform)
        register(combinedViewWorldTransform)
        register(normalMatrix)
        register(shininess)
        register(diffuseColor)
        register(diffuseTexture)
        register(specularColor)
        register(specularTexture)
        register(emissiveColor)
        register(reflectionColor)
        register(normalTexture)
        register(environmentCubemap)
    }

    fun register(s: Setter) {
        s.add(this)
    }

    override fun init() {
        init(ShaderProgram(Gdx.files.internal("shader/default3d.vert"), Gdx.files.internal("shader/default3d.frag")), renderable)
    }

    override fun render(renderable: Renderable) {
        context.setBlending(false, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA)

        context.setCullFace(GL_BACK)
        context.setDepthTest(GL_LEQUAL, 0f, 1f)
        context.setDepthMask(true)

        super.render(renderable)
    }

    override fun canRender(instance: Renderable) = true
    override fun compareTo(other: Shader) = 0

    companion object DrifterShader3D {
        val cameraProjection = setter("cameraProjection", { s: BaseShader, r: Renderable, a: Attributes ->
            s.camera.projection }, true)

        val cameraView = setter("cameraView", { s: BaseShader, r: Renderable, a: Attributes ->
            s.camera.view }, true)

        val cameraCombined = setter("cameraCombined", { s: BaseShader, r: Renderable, a: Attributes ->
            s.camera.combined }, true)

        val cameraPosition = setter("cameraPosition", { s: BaseShader, r: Renderable, a: Attributes ->
            Pair(V3(s.camera.position.x, s.camera.position.y, s.camera.position.z), 1.1881f / (s.camera.far * s.camera.far)) }, true)

        val cameraDirection = setter("cameraDirection", { s: BaseShader, r: Renderable, a: Attributes ->
            s.camera.direction }, true)

        val cameraUp = setter("cameraUp", { s: BaseShader, r: Renderable, a: Attributes ->
            s.camera.up }, true)

        val worldTransform = setter<Matrix4>("worldTransform", { s: BaseShader, r: Renderable, a: Attributes ->
            r.worldTransform }, false)

        val viewWorldTransform = setter<Matrix4>("viewWorldTransform", { s: BaseShader, r: Renderable, a: Attributes ->
            s.camera.view.cpy().mul(r.worldTransform) }, false)

        val combinedViewWorldTransform = setter<Matrix4>("combinedViewWorldTransform", { s: BaseShader, r: Renderable, a: Attributes ->
            s.camera.combined.cpy().mul(r.worldTransform) }, false)

        val normalMatrix = setter<Matrix3>("normalMatrix", { s: BaseShader, r: Renderable, a: Attributes ->
            Matrix3().set(r.worldTransform).inv().transpose() }, false)

        val shininess = setter<Float>("shininess", { s: BaseShader, r: Renderable, a: Attributes ->
            a.get(FloatAttribute.Shininess).let { a -> when(a) { is FloatAttribute -> a.value; else -> 12f} } }, false)

        val diffuseColor = setter<Color>("diffuseColor", { s: BaseShader, r: Renderable, a: Attributes ->
            a.get(ColorAttribute.Diffuse).let { a -> when(a) { is ColorAttribute -> a.color; else -> Color.WHITE } } }, false)

        val diffuseTexture = setter<Int>("diffuseTexture", { s: BaseShader, r: Renderable, a: Attributes ->
            s.context.textureBinder.bind(a.get(TextureAttribute.Diffuse).let { a -> when(a) { is TextureAttribute -> a.textureDescription; else -> null } }) }, false)

        val specularColor = setter<Color>("specularColor", { s: BaseShader, r: Renderable, a: Attributes ->
            a.get(ColorAttribute.Specular).let { a -> when(a) { is ColorAttribute -> a.color; else -> Color.WHITE } } }, false)

        val specularTexture = setter<Int>("specularTexture", { s: BaseShader, r: Renderable, a: Attributes ->
            s.context.textureBinder.bind(a.get(TextureAttribute.Specular).let { a -> when(a) { is TextureAttribute -> a.textureDescription; else -> null } }) }, false)

        val emissiveColor = setter<Color>("emissiveColor", { s: BaseShader, r: Renderable, a: Attributes ->
            a.get(ColorAttribute.Emissive).let { a -> when(a) { is ColorAttribute -> a.color; else -> Color.WHITE } } }, false)

        val reflectionColor = setter<Color>("reflectionColor", { s: BaseShader, r: Renderable, a: Attributes ->
            a.get(ColorAttribute.Reflection).let { a -> when(a) { is ColorAttribute -> a.color; else -> Color.WHITE } } }, false)

        val normalTexture = setter<Int>("normalTexture", { s: BaseShader, r: Renderable, a: Attributes ->
            s.context.textureBinder.bind(a.get(TextureAttribute.Normal).let { a -> when(a) { is TextureAttribute -> a.textureDescription; else -> null } }) }, false)

        val environmentCubemap = setter<Int>("environmentCubemap", { s: BaseShader, r: Renderable, a: Attributes ->
            s.context.textureBinder.bind(a.get(CubemapAttribute.EnvironmentMap).let { a -> when(a) { is CubemapAttribute -> a.textureDescription; else -> null } }) }, false)

        abstract class Setter(val uniformName: String, val global: Boolean) : SetterGDX {
            override fun isGlobal(shader: BaseShader, inputID: Int) = global

            val uniform = Uniform(uniformName)

            fun add(shader: BaseShader) {
                shader.register(uniform, this)
            }
        }

        fun <T : Any> setter(uniformName: String, f: (BaseShader, Renderable, Attributes) -> T, global: Boolean) = object : Setter(uniformName, global) {
            override fun set(shader: BaseShader, inputID: Int, renderable: Renderable, attributes: Attributes) {
                val value = f(shader, renderable, attributes)
                when(value) {
                    is Float -> shader.set(inputID, value)
                    is Int -> shader.set(inputID, value)
                    is Vector2 -> shader.set(inputID, value)
                    is Vector3 -> shader.set(inputID, value)
                    is Pair<*, *> -> {
                        val v = value as Pair<Vector3, Float>
                        shader.set(inputID, v.first.x, v.first.y, v.first.z, v.second)
                    }
                    is Matrix3 -> shader.set(inputID, value)
                    is Matrix4 -> shader.set(inputID, value)
                    is Color -> shader.set(inputID, value)
                    else -> throw Exception("No shader setter found for type " + value.javaClass.simpleName)
                }
            }
        }

        fun <T : Any> setter(uniformName: String, v: T, global: Boolean): Setter = setter(uniformName, { s: BaseShader, r: Renderable, a: Attributes -> v }, global)

    }}

