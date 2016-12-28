package xyz.jmullin.drifter.assets.delegates

import com.badlogic.gdx.assets.loaders.BitmapFontLoader
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import xyz.jmullin.drifter.assets.DrifterAssets

class FontDelegate(assetName: String?, assets: DrifterAssets) : AssetDelegate<BitmapFont>(assetName, assets) {
    constructor(assets: DrifterAssets) : this(null, assets)

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
}