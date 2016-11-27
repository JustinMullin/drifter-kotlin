package xyz.jmullin.drifter.assets.delegates

import com.badlogic.gdx.graphics.g2d.BitmapFont
import xyz.jmullin.drifter.assets.DrifterAssets

class FontDelegate(assetName: String?, assets: DrifterAssets) : AssetDelegate<BitmapFont>(assetName, assets) {
    constructor(assets: DrifterAssets) : this(null, assets)

    override fun path(name: String) = "font/$name.fnt"
    override val type = BitmapFont::class.java
}