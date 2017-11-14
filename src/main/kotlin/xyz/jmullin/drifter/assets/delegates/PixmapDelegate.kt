package xyz.jmullin.drifter.assets.delegates

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Pixmap
import xyz.jmullin.drifter.assets.DrifterAssets

class PixmapDelegate(assetName: String?, assets: DrifterAssets) : AssetDelegate<Pixmap>(assetName, assets) {
    constructor(assets: DrifterAssets) : this(null, assets)

    override fun path(name: String) = "image/$name.png"
    override val type = Pixmap::class.java

    override fun loadAsset() {
        // Do nothing
    }

    override fun getAsset(): Pixmap {
        return Pixmap(Gdx.files.internal(path(safeAssetName())))
    }
}