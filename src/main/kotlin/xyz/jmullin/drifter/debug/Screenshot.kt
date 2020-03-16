package xyz.jmullin.drifter.debug

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.PixmapIO
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.BufferUtils
import com.badlogic.gdx.utils.ScreenUtils

object Screenshot {
    var suffixIndex = 1

    fun save(baseFilename: String, overwrite: Boolean = true, bounds: Rectangle? = null) {
        var file = Gdx.files.external("$baseFilename.png")

        if (!overwrite) while(file.exists()) {
            suffixIndex += 1
            file = Gdx.files.external("$baseFilename-$suffixIndex.png")
        }

        val pixels = bounds?.let {
            ScreenUtils.getFrameBufferPixels(bounds.x.toInt(), bounds.y.toInt(), bounds.width.toInt(), bounds.height.toInt(), true)
        } ?: ScreenUtils.getFrameBufferPixels(0, 0, Gdx.graphics.backBufferWidth, Gdx.graphics.backBufferHeight, true)

        val pixmap = bounds?.let {
            Pixmap(bounds.width.toInt(), bounds.height.toInt(), Pixmap.Format.RGBA8888)
        } ?: Pixmap(Gdx.graphics.backBufferWidth, Gdx.graphics.backBufferHeight, Pixmap.Format.RGBA8888)

        BufferUtils.copy(pixels, 0, pixmap.pixels, pixels.size)
        log("Saving screenshot${bounds?.let { " of $it" } ?: ""} to ${file.file().absolutePath}.")
        PixmapIO.writePNG(file, pixmap)
        pixmap.dispose()
    }
}
