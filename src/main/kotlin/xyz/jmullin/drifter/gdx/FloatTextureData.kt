/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package xyz.jmullin.drifter.gdx

import java.nio.FloatBuffer

import com.badlogic.gdx.Application.ApplicationType
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Pixmap.Format
import com.badlogic.gdx.graphics.TextureData
import com.badlogic.gdx.utils.BufferUtils
import com.badlogic.gdx.utils.GdxRuntimeException

/** A [TextureData] implementation which should be used to create float textures.  */
class FloatTextureData(w: Int, h: Int) : TextureData {

    internal var width = 0
    internal var height = 0
    internal var isPrepared = false
    var buffer: FloatBuffer? = null
        internal set

    init {
        this.width = w
        this.height = h
    }

    override fun getType(): TextureData.TextureDataType {
        return TextureData.TextureDataType.Custom
    }

    override fun isPrepared(): Boolean {
        return isPrepared
    }

    override fun prepare() {
        if (isPrepared) throw GdxRuntimeException("Already prepared")
        this.buffer = BufferUtils.newFloatBuffer(width * height * 4)
        isPrepared = true
    }

    override fun consumeCustomData(target: Int) {
        if (Gdx.app.type == ApplicationType.Android || Gdx.app.type == ApplicationType.iOS
            || Gdx.app.type == ApplicationType.WebGL) {

            if (!Gdx.graphics.supportsExtension("OES_texture_float"))
                throw GdxRuntimeException("Extension OES_texture_float not supported!")

            // GLES and WebGL defines texture format by 3rd and 8th argument,
            // so to get a float texture one needs to supply GL_RGBA and GL_FLOAT there.
            Gdx.gl.glTexImage2D(target, 0, GL20.GL_RGBA, width, height, 0, GL20.GL_RGBA, GL20.GL_FLOAT, buffer)

        } else {
            // TODO: figure out what's the actual extension required here
//            if (!Gdx.graphics.supportsExtension("GL_ARB_texture_float") &&
//                !Gdx.graphics.supportsExtension("APPLE_float_pixels"))
//                throw GdxRuntimeException("Extension GL_ARB_texture_float and APPLE_float_pixels not supported!")

            val GL_RGBA32F = 34836 // this is a const from GL 3.0, used only on desktops

            // in desktop OpenGL the texture format is defined only by the third argument,
            // hence we need to use GL_RGBA32F there (this constant is unavailable in GLES/WebGL)
            Gdx.gl.glTexImage2D(target, 0, GL_RGBA32F, width, height, 0, GL20.GL_RGBA, GL20.GL_FLOAT, buffer)
        }
    }

    override fun consumePixmap(): Pixmap {
        throw GdxRuntimeException("This TextureData implementation does not return a Pixmap")
    }

    override fun disposePixmap(): Boolean {
        throw GdxRuntimeException("This TextureData implementation does not return a Pixmap")
    }

    override fun getWidth(): Int {
        return width
    }

    override fun getHeight(): Int {
        return height
    }

    override fun getFormat(): Format {
        return Format.RGBA8888 // it's not true, but FloatTextureData.getFormat() isn't used anywhere
    }

    override fun useMipMaps(): Boolean {
        return false
    }

    override fun isManaged(): Boolean {
        return true
    }
}
