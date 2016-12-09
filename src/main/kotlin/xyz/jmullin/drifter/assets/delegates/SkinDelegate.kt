package xyz.jmullin.drifter.assets.delegates

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import xyz.jmullin.drifter.assets.DrifterAssets

class SkinDelegate(assetName: String?, assets: DrifterAssets): AssetDelegate<Skin>(assetName, assets) {
    constructor(assets: DrifterAssets) : this(null, assets)

    override fun path(name: String) = "skin/$name.json"
    override val type = Skin::class.java
}