package xyz.jmullin.drifter.gdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Gdx.gl30
import com.badlogic.gdx.graphics.*
import com.badlogic.gdx.graphics.GL20.GL_NONE
import com.badlogic.gdx.graphics.glutils.GLOnlyTextureData
import com.badlogic.gdx.utils.GdxRuntimeException
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL32

class FrameBufferCubemapDepthOnly(val width: Int, val height: Int) {

    private var currentSide: Int = 0
    private var framebufferHandle = 0
    private var texture: Cubemap = Cubemap(2, 2, 2, Pixmap.Format.RGB888)

    init {
        build()
    }

    private fun createDepthTexture(): Cubemap {
        val data = GLOnlyTextureData(width, height, 0, GL30.GL_DEPTH_COMPONENT32F, GL30.GL_DEPTH_COMPONENT, GL30.GL_FLOAT)
        return Cubemap(data, data, data, data, data, data)
    }

    private fun build() {
        val gl = Gdx.gl20

        texture = createDepthTexture()
        texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)

        framebufferHandle = gl.glGenFramebuffer()
        gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebufferHandle)

        GL32.glFramebufferTexture(GL20.GL_FRAMEBUFFER, GL20.GL_DEPTH_ATTACHMENT, texture.textureObjectHandle, 0)

        org.lwjgl.opengl.GL11.glDrawBuffer(GL11.GL_NONE)
        gl30.glReadBuffer(GL_NONE)

        gl.glBindRenderbuffer(GL20.GL_RENDERBUFFER, 0)
        gl.glBindTexture(GL20.GL_TEXTURE_CUBE_MAP, 0)

        gl.glBindFramebuffer(GL20.GL_FRAMEBUFFER, 0)

        val result = gl.glCheckFramebufferStatus(GL20.GL_FRAMEBUFFER)

        if (result != GL20.GL_FRAMEBUFFER_COMPLETE) {
            disposeTexture(texture)

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

    private fun disposeTexture(texture: Cubemap) {
        texture.dispose()
    }

    fun bind() {
        currentSide = -1
        Gdx.gl20.glBindFramebuffer(GL20.GL_FRAMEBUFFER, framebufferHandle)
    }

    fun begin() {
        bind()
        setFrameBufferViewport()
    }

    private fun setFrameBufferViewport() {
        Gdx.gl20.glViewport(0, 0, texture.width, texture.height)
    }

    fun end() {
        end(0, 0, Gdx.graphics.width, Gdx.graphics.height)
    }

    fun end(x: Int, y: Int, width: Int, height: Int) {
        unbind()
        Gdx.gl20.glViewport(x, y, width, height)
    }

    fun unbind() {
        Gdx.gl20.glBindFramebuffer(GL20.GL_FRAMEBUFFER, 0)
    }

    fun getTexture(): Cubemap {
        return texture
    }

    fun nextSide(): Boolean {
        if (currentSide > 5) {
            throw GdxRuntimeException("No remaining sides.")
        } else if (currentSide == 5) {
            return false
        }

        currentSide++
        bindSide(side)
        return true
    }

    private fun bindSide(side: Cubemap.CubemapSide?) {
        Gdx.gl20.glFramebufferTexture2D(GL20.GL_FRAMEBUFFER, GL20.GL_DEPTH_ATTACHMENT, side?.glEnum ?: 0, texture.textureObjectHandle, 0)
    }

    val side: Cubemap.CubemapSide?
        get() = if (currentSide < 0) null else Cubemap.CubemapSide.values()[currentSide]
}
