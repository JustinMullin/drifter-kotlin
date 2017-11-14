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
import xyz.jmullin.drifter.debug.log
import xyz.jmullin.drifter.extensions.V3
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader.Setter as SetterGDX

/**
 * Basic 3D LibGDX shader implementation in Kotlin, based on [[DefaultShader]].
 */
open class DrifterShader3D(var renderable: Renderable,
                           val vertexShader: String? = null,
                           val fragmentShader: String? = null) : BaseShader() {

    var blendingEnabled = false
    var blendSource = GL20.GL_SRC_ALPHA
    var blendDest = GL20.GL_ONE_MINUS_SRC_ALPHA
    var cullFace = GL_BACK
    var depthTest = GL_LEQUAL
    var depthRangeNear = 0f
    var depthRangeFar = 1f
    var depthMask = true

    init {
        defaultShaders()
    }

    open fun defaultShaders() {
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
        register(emissiveTexture)
        register(reflectionColor)
        register(ambientColor)
        register(normalTexture)
        register(environmentCubemap)
    }

    fun register(s: Setter) {
        s.add(this)
    }

    override fun init() {
        log("Initializing shader $vertexShader / $fragmentShader")

        init(ShaderProgram(
            Gdx.files.internal("shader/${vertexShader ?: "default3d"}.vert"),
            Gdx.files.internal("shader/${fragmentShader ?: "default3d"}.frag")),
            renderable)
    }

    override fun render(renderable: Renderable) {
        context.setBlending(blendingEnabled, blendSource, blendDest)

        context.setCullFace(cullFace)
        context.setDepthTest(depthTest, depthRangeNear, depthRangeFar)
        context.setDepthMask(depthMask)

        super.render(renderable)
    }

    override fun canRender(instance: Renderable) = true
    override fun compareTo(other: Shader) = 0

    fun <T : Any> uniform(name: String, f: (BaseShader, Renderable?, Attributes?) -> T) = register(setter(name, f, true))
    fun <T : Any> uniformLocal(name: String, f: (BaseShader, Renderable?, Attributes?) -> T) = register(setter(name, f, false))

    fun <T : Any> uniformSimple(name: String, v: () -> T) = register(setter(name, { _, _, _ -> v() }, true))
    fun <T : Any> uniformSimpleLocal(name: String, v: () -> T) = register(setter(name, { _, _, _ -> v() }, false))

    companion object DrifterShader3D {
        val cameraProjection = setter("cameraProjection", { s: BaseShader, _: Renderable?, _: Attributes? ->
            s.camera.projection }, true)

        val cameraView = setter("cameraView", { s: BaseShader, _: Renderable?, _: Attributes? ->
            s.camera.view }, true)

        val cameraCombined = setter("cameraCombined", { s: BaseShader, _: Renderable?, _: Attributes? ->
            s.camera.combined }, true)

        val cameraPosition = setter("cameraPosition", { s: BaseShader, _: Renderable?, _: Attributes? ->
            Pair(V3(s.camera.position.x, s.camera.position.y, s.camera.position.z), 1.1881f / (s.camera.far * s.camera.far)) }, true)

        val cameraDirection = setter("cameraDirection", { s: BaseShader, _: Renderable?, _: Attributes? ->
            s.camera.direction }, true)

        val cameraUp = setter("cameraUp", { s: BaseShader, _: Renderable?, _: Attributes? ->
            s.camera.up }, true)

        val worldTransform = setter<Matrix4>("worldTransform", { _: BaseShader, r: Renderable?, _: Attributes? ->
            r!!.worldTransform }, false)

        val viewWorldTransform = setter<Matrix4>("viewWorldTransform", { s: BaseShader, r: Renderable?, _: Attributes? ->
            s.camera.view.cpy().mul(r?.worldTransform) }, false)

        val combinedViewWorldTransform = setter<Matrix4>("combinedViewWorldTransform", { s: BaseShader, r: Renderable?, _: Attributes? ->
            s.camera.combined.cpy().mul(r?.worldTransform) }, false)

        val normalMatrix = setter<Matrix3>("normalMatrix", { _: BaseShader, r: Renderable?, _: Attributes? ->
            Matrix3().set(r?.worldTransform).inv().transpose() }, false)

        val shininess = setter("material.shininess", { _: BaseShader, _: Renderable?, atr: Attributes? ->
            atr?.get(FloatAttribute.Shininess).let { a -> when(a) { is FloatAttribute -> a.value; else -> 12f} } }, false)

        val diffuseColor = setter<Color>("material.diffuseColor", { _: BaseShader, _: Renderable?, a: Attributes? ->
            a?.get(ColorAttribute.Diffuse).let { diffuse -> when(diffuse) { is ColorAttribute -> diffuse.color; else -> Color.WHITE } } }, false)

        val diffuseTexture = setter("material.diffuseTexture", { s: BaseShader, _: Renderable?, a: Attributes? ->
            a?.get(TextureAttribute.Diffuse).let { diffuse -> when(diffuse) { is TextureAttribute -> diffuse.textureDescription; else -> null } }?.let { s.context.textureBinder.bind(it) } ?: 0 }, false)

        val specularColor = setter<Color>("material.specularColor", { _: BaseShader, _: Renderable?, a: Attributes? ->
            a?.get(ColorAttribute.Specular).let { specular -> when(specular) { is ColorAttribute -> specular.color; else -> Color.WHITE } } }, false)

        val specularTexture = setter("material.specularTexture", { s: BaseShader, _: Renderable?, a: Attributes? ->
            s.context.textureBinder.bind(a?.get(TextureAttribute.Specular).let { specular -> when(specular) { is TextureAttribute -> specular.textureDescription; else -> null } }) }, false)

        val emissiveColor = setter<Color>("material.emissiveColor", { _: BaseShader, _: Renderable?, a: Attributes? ->
            a?.get(ColorAttribute.Emissive).let { emissive -> when(emissive) { is ColorAttribute -> emissive.color; else -> Color.WHITE } } }, false)

        val emissiveTexture = setter("material.emissiveTexture", { s: BaseShader, _: Renderable?, a: Attributes? ->
            s.context.textureBinder.bind(a?.get(TextureAttribute.Emissive).let { emissive -> when(emissive) { is TextureAttribute -> emissive.textureDescription; else -> null } }) }, false)

        val reflectionColor = setter<Color>("material.reflectionColor", { _: BaseShader, _: Renderable?, a: Attributes? ->
            a?.get(ColorAttribute.Reflection).let { reflection -> when(reflection) { is ColorAttribute -> reflection.color; else -> Color.WHITE } } }, false)

        val ambientColor = setter<Color>("material.ambientColor", { _: BaseShader, _: Renderable?, a: Attributes? ->
            a?.get(ColorAttribute.Ambient).let { ambient -> when(ambient) { is ColorAttribute -> ambient.color; else -> Color.BLACK } } }, false)

        val normalTexture = setter("material.normalTexture", { s: BaseShader, _: Renderable?, a: Attributes? ->
            s.context.textureBinder.bind(a?.get(TextureAttribute.Normal).let { normal -> when(normal) { is TextureAttribute -> normal.textureDescription; else -> null } }) }, false)

        val environmentCubemap = setter("environmentCubemap", { s: BaseShader, _: Renderable?, a: Attributes? ->
            s.context.textureBinder.bind(a?.get(CubemapAttribute.EnvironmentMap).let { map -> when(map) { is CubemapAttribute -> map.textureDescription; else -> null } }) }, false)

        abstract class Setter(uniformName: String, val global: Boolean) : SetterGDX {
            override fun isGlobal(shader: BaseShader, inputID: Int) = global

            val uniform = Uniform(uniformName)

            fun add(shader: BaseShader) {
                shader.register(uniform, this)
            }
        }

        fun <T : Any> setter(uniformName: String, f: (BaseShader, Renderable?, Attributes?) -> T, global: Boolean) = object : Setter(uniformName, global) {
            override fun set(shader: BaseShader, inputID: Int, renderable: Renderable?, attributes: Attributes?) {
                val value = f(shader, renderable, attributes)
                when(value) {
                    is Float -> shader.set(inputID, value)
                    is Int -> shader.set(inputID, value)
                    is Vector2 -> shader.set(inputID, value)
                    is Vector3 -> shader.set(inputID, value)
                    is Pair<*, *> -> {
                        @Suppress("USELESS_CAST")
                        if(value.first is Vector3 && value.second is Float) {
                            val v = value.first as Vector3
                            val a = value.second as Float
                            shader.set(inputID, v.x, v.y, v.z, a)
                        }
                    }
                    is Matrix3 -> shader.set(inputID, value)
                    is Matrix4 -> shader.set(inputID, value)
                    is Color -> shader.set(inputID, value)
                    else -> throw Exception("No shader setter found for type " + value.javaClass.simpleName)
                }
            }
        }

    }}

