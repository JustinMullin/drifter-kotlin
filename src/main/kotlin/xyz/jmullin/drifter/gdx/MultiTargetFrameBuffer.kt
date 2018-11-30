package xyz.jmullin.drifter.gdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.GL30
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.glutils.GLOnlyTextureData
import com.badlogic.gdx.utils.BufferUtils
import com.badlogic.gdx.utils.Disposable
import kotlin.jvm.JvmOverloads;

class MultiTargetFrameBuffer(val width: Int,
                             val height: Int,
                             private val targets: List<String>) : Disposable {

    private var colorTextures = listOf<Texture>()

    private var framebufferHandle: Int = 0

    init {
        build()
    }

    private fun createColorTexture(min: Texture.TextureFilter, mag: Texture.TextureFilter, internalformat: Int, format: Int,
                                   type: Int): Texture {
        val data = GLOnlyTextureData(width, height, 0, internalformat, format, type)
        val result = Texture(data)
        result.setFilter(min, mag)
        result.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
        return result
    }

    private fun createDepthTexture(): Texture {
        val data = GLOnlyTextureData(width, height, 0, GL30.GL_DEPTH_COMPONENT32F, GL30.GL_DEPTH_COMPONENT, GL30.GL_FLOAT)
        val result = Texture(data)
        result.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest)
        result.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat)
        return result
    }

    private fun disposeColorTexture(colorTexture: Texture) {
        colorTexture.dispose()
    }

    private fun build() {
        val gl = Gdx.gl20

        colorTextures = listOf()

        framebufferHandle = gl.glGenFramebuffer()
        gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebufferHandle)

        val textures = targets.map {
            createColorTexture(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest,
                GL30.GL_RGB16F, GL30.GL_RGB, GL30.GL_FLOAT)
        }

        textures.forEach { colorTextures += it }

        textures.forEachIndexed { i, texture ->
            gl.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0 + i, GL30.GL_TEXTURE_2D,
                texture.textureObjectHandle, 0)
        }

        val buffer = BufferUtils.newIntBuffer(targets.size)
        textures.forEachIndexed { i, _ ->
            buffer.put(GL30.GL_COLOR_ATTACHMENT0 + i)
        }
        buffer.position(0)
        Gdx.gl30.glDrawBuffers(targets.size, buffer)

        gl.glBindRenderbuffer(GL20.GL_RENDERBUFFER, 0)
        gl.glBindTexture(GL20.GL_TEXTURE_2D, 0)

        val result = gl.glCheckFramebufferStatus(GL20.GL_FRAMEBUFFER)

        gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, 0)

        if (result != GL20.GL_FRAMEBUFFER_COMPLETE) {
            for (colorTexture in colorTextures)
                disposeColorTexture(colorTexture)

            gl.glDeleteFramebuffer(framebufferHandle)

            if (result == GL20.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT)
                throw IllegalStateException("frame buffer couldn't be constructed: incomplete attachment")
            if (result == GL20.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS)
                throw IllegalStateException("frame buffer couldn't be constructed: incomplete dimensions")
            if (result == GL20.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT)
                throw IllegalStateException("frame buffer couldn't be constructed: missing attachment")
            if (result == GL20.GL_FRAMEBUFFER_UNSUPPORTED)
                throw IllegalStateException("frame buffer couldn't be constructed: unsupported combination of formats")
            throw IllegalStateException("frame buffer couldn't be constructed: unknown error " + result)
        }
    }

    override fun dispose() {
        val gl = Gdx.gl20

        for (textureAttachment in colorTextures) {
            disposeColorTexture(textureAttachment)
        }

        gl.glDeleteFramebuffer(framebufferHandle)
    }

    fun bind() {
        Gdx.gl20.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebufferHandle)
    }

    fun begin() {
        bind()
        setFrameBufferViewport()
    }

    private fun setFrameBufferViewport() {
        Gdx.gl20.glViewport(0, 0, colorTextures.first().width, colorTextures.first().height)
    }

    @JvmOverloads fun end(x: Int = 0, y: Int = 0, width: Int = Gdx.graphics.backBufferWidth, height: Int = Gdx.graphics.backBufferHeight) {
        unbind()
        Gdx.gl20.glViewport(x, y, width, height)
    }

    fun getColorBufferTextures() = colorTextures

    fun getColorBufferTexture(index: Int) = colorTextures[index]

    val depth: Int
        get() = colorTextures.first().depth

    fun unbind() {
        Gdx.gl20.glBindFramebuffer(GL20.GL_FRAMEBUFFER, 0)
    }
}
