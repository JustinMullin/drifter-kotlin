package xyz.jmullin.drifter.assets.delegates

import com.badlogic.gdx.graphics.g2d.NinePatch
import com.badlogic.gdx.graphics.g2d.Sprite
import xyz.jmullin.drifter.assets.DrifterAssets
import xyz.jmullin.drifter.error.DrifterAssetsException

class NinePatchDelegate(assetName: String?, assets: DrifterAssets) : AssetDelegate<NinePatch>(assetName, assets) {
    constructor(assets: DrifterAssets) : this(null, assets)

    override fun path(name: String) = ""
    override val type = NinePatch::class.java

    private var left: Int = 0
    private var right: Int = 0
    private var top: Int = 0
    private var bottom: Int = 0

    override fun loadAsset() {
        // Do nothing
    }

    override fun getAsset(): NinePatch {
        if(assets.primaryAtlas == null) {
            throw DrifterAssetsException(
                "No texture atlas loaded to pull sprite '${safeAssetName()}' from. " +
                    "At least one TextureAtlas is required to load Sprites.")
        }

        return assets.primaryAtlas?.createSprite(safeAssetName())?.let{ NinePatch(it, left, right, top, bottom) }
            ?: throw DrifterAssetsException("Failed to load sprite '${safeAssetName()}.' Is the sprite present in the texture atlas?")
    }

    fun left(n: Int) = apply { this.left = n }
    fun right(n: Int) = apply { this.right = n }
    fun top(n: Int) = apply { this.top = n }
    fun bottom(n: Int) = apply { this.bottom = n }
    fun margin(n: Int) = margin(n, n, n, n)
    fun margin(left: Int, right: Int, top: Int, bottom: Int) = apply {
        this.left = left
        this.right = right
        this.top = top
        this.bottom = bottom
    }
}
