package xyz.jmullin.drifter.gdx

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureFilter
import com.badlogic.gdx.graphics.Texture.TextureWrap
import com.badlogic.gdx.graphics.glutils.FrameBuffer

/** This is a [FrameBuffer] variant backed by a float texture.  */
class FloatFrameBuffer(width: Int, height: Int, hasDepth: Boolean) : FrameBuffer(Pixmap.Format.RGBA8888, width, height, hasDepth) {

    override fun createTexture(attachmentSpec: FrameBufferTextureAttachmentSpec?): Texture {
        val data = FloatTextureData(width, height)
        val result = Texture(data)
        if (Gdx.app.type == ApplicationType.Desktop || Gdx.app.type == ApplicationType.Applet)
            result.setFilter(TextureFilter.Linear, TextureFilter.Linear)
        else
        // no filtering for float textures in OpenGL ES
            result.setFilter(TextureFilter.Nearest, TextureFilter.Nearest)
        result.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge)
        return result
    }

    override fun disposeColorTexture(colorTexture: Texture) {
        colorTexture.dispose()
    }
}
