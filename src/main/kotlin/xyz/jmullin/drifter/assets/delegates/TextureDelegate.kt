package xyz.jmullin.drifter.assets.delegates

import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.Texture.TextureWrap
import com.badlogic.gdx.graphics.Texture.TextureFilter
import xyz.jmullin.drifter.assets.DrifterAssets

class TextureDelegate(assetName: String?, val extension: String, assets: DrifterAssets): AssetDelegate<Texture>(assetName, assets) {
    constructor(assets: DrifterAssets) : this(null, "png", assets)

    val parameters = TextureLoader.TextureParameter()

    override fun path(name: String) = "texture/$name.$extension"
    override val type = Texture::class.java

    override fun loadAsset() {
        assets.manager.load(path(safeAssetName()), type, parameters)
    }

    fun format(format: Pixmap.Format): TextureDelegate {
        return apply { parameters.format = format }
    }

    fun genMipMaps(genMipmaps: Boolean): TextureDelegate {
        return apply { parameters.genMipMaps = genMipmaps }
    }

    fun filters(min: TextureFilter, mag: TextureFilter): TextureDelegate {
        return apply { parameters.minFilter = min; parameters.magFilter = mag }
    }

    fun minFilter(filter: TextureFilter): TextureDelegate {
        return apply { parameters.minFilter = filter }
    }

    fun magFilter(filter: TextureFilter): TextureDelegate {
        return apply { parameters.magFilter = filter }
    }

    fun wrap(u: TextureWrap, v: TextureWrap): TextureDelegate {
        return apply { parameters.wrapU = u; parameters.wrapV = v }
    }

    fun wrapU(u: TextureWrap): TextureDelegate {
        return apply { parameters.wrapU = u }
    }

    fun wrapV(v: TextureWrap): TextureDelegate {
        return apply { parameters.wrapV = v }
    }
}