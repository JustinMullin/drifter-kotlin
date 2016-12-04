package xyz.jmullin.drifter.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.utils.BufferUtils
import com.badlogic.gdx.utils.ScreenUtils

object Screenshot {
    var suffixIndex = 1

    fun save(baseFilename: String) {
        var file = Gdx.files.external("$baseFilename.png")

        while(file.exists()) {
            suffixIndex += 1
            file = Gdx.files.external("$baseFilename-$suffixIndex.png")
        }

        val pixels = ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.backBufferWidth, Gdx.graphics.backBufferHeight, true)

        val pixmap = Pixmap(Gdx.graphics.backBufferWidth, Gdx.graphics.backBufferHeight, Pixmap.Format.RGBA8888)
        BufferUtils.copy(pixels, 0, pixmap.pixels, pixels.size)
        log("Saving screenshot to ${file.file().absolutePath}.")
        PixmapIO.writePNG(file, pixmap)
        pixmap.dispose()
    }
}