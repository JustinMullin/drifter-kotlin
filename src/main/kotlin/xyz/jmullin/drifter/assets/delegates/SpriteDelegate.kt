package xyz.jmullin.drifter.assets.delegates

import com.badlogic.gdx.graphics.g2d.Sprite
import xyz.jmullin.drifter.assets.DrifterAssets
import xyz.jmullin.drifter.error.DrifterAssetsException

class SpriteDelegate(assetName: String?, assets: DrifterAssets) : AssetDelegate<Sprite>(assetName, assets) {
    constructor(assets: DrifterAssets) : this(null, assets)

    override fun path(name: String) = ""
    override val type = Sprite::class.java

    override fun loadAsset() {
        // Do nothing
    }

    override fun getAsset(): Sprite {
        if(assets.primaryAtlas == null) {
            throw DrifterAssetsException(
                "No texture atlas loaded to pull sprite '${safeAssetName()}' from. " +
                    "At least one TextureAtlas is required to load Sprites.")
        }

        return assets.primaryAtlas?.createSprite(safeAssetName())
            ?: throw DrifterAssetsException("Failed to load sprite '${safeAssetName()}.' Is the sprite present in the texture atlas?")
    }
}
