package xyz.jmullin.drifter.assets.delegates

import com.badlogic.gdx.assets.loaders.BitmapFontLoader
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import xyz.jmullin.drifter.assets.DrifterAssets

class FontDelegate(assetName: String?, assets: DrifterAssets) : AssetDelegate<BitmapFont>(assetName, assets) {
    constructor(assets: DrifterAssets) : this(null, assets)

    var size = 12
    var spaceX = 0
    var spaceY = 0
    var borderColor: Color = Color.WHITE
    var borderWidth: Float = 0f
    var sourceName: String? = null
    val parameters = BitmapFontLoader.BitmapFontParameter()

    override fun path(name: String) = "font/$name.fnt"

    override val type = BitmapFont::class.java

    fun filter(minFilter: Texture.TextureFilter, magFilter: Texture.TextureFilter): FontDelegate {
        return apply { parameters.minFilter = minFilter; parameters.magFilter = magFilter }
    }

    fun minFilter(filter: Texture.TextureFilter): FontDelegate {
        return apply { parameters.minFilter = filter }
    }

    fun magFilter(filter: Texture.TextureFilter): FontDelegate {
        return apply { parameters.magFilter = filter }
    }

    fun borderColor(color: Color): FontDelegate {
        return apply { borderColor = color }
    }

    fun borderWidth(width: Float): FontDelegate {
        return apply { borderWidth = width }
    }

    fun size(size: Int): FontDelegate {
        return apply { this.size = size }
    }

    fun spaceX(spaceX: Int): FontDelegate {
        return apply { this.spaceX = spaceX }
    }

    fun spaceY(spaceY: Int): FontDelegate {
        return apply { this.spaceY = spaceY }
    }

    fun source(sourceName: String): FontDelegate {
        return apply { this.sourceName = sourceName }
    }
}
