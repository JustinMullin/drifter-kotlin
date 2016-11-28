package xyz.jmullin.drifter.assets.delegates

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import xyz.jmullin.drifter.assets.DrifterAssets

class TextureAtlasDelegate(assetName: String?, assets: DrifterAssets): AssetDelegate<TextureAtlas>(assetName, assets) {
    constructor(assets: DrifterAssets) : this(null, assets)

    override fun path(name: String) = "atlas/$name.atlas"
    override val type = TextureAtlas::class.java
}