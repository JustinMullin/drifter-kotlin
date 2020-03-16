package xyz.jmullin.drifter.assets.delegates

import xyz.jmullin.drifter.animation.Animation
import xyz.jmullin.drifter.assets.DrifterAssets
import xyz.jmullin.drifter.error.DrifterAssetsException

class AnimationDelegate(assetName: String?, assets: DrifterAssets) : AssetDelegate<Animation>(assetName, assets) {
    constructor(assets: DrifterAssets) : this(null, assets)

    override fun path(name: String) = "sprite/$name.png"
    override val type = Animation::class.java

    override fun loadAsset() {
        // Do nothing
    }

    override fun getAsset(): Animation {
        if(assets.primaryAtlas == null) {
            throw DrifterAssetsException(
                "No texture atlas loaded to pull animation '${safeAssetName()}' from. " +
                    "At least one TextureAtlas is required to load Sprites.")
        }

        val regex = "${safeAssetName()}(\\d+)".toRegex()
        return assets.primaryAtlas?.let { atlas ->
            val regionNames = atlas.regions?.map { it.name } ?: throw DrifterAssetsException("No regions for texture atlas!")
            val frames = regionNames.mapNotNull { name ->
                val matches = regex.matchEntire(name)
                matches?.groupValues?.getOrElse(1) { null }?.toInt()
            }.sorted()
            Animation(frames.map { atlas.createSprite(safeAssetName() + it) })
        } ?: throw DrifterAssetsException("Failed to load sprite '${safeAssetName()}.' Is the sprite present in the texture atlas?")
    }
}
