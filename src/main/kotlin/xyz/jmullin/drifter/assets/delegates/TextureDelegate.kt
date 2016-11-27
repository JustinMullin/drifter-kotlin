package xyz.jmullin.drifter.assets.delegates

import com.badlogic.gdx.graphics.Texture
import xyz.jmullin.drifter.assets.DrifterAssets
import kotlin.reflect.KProperty

class TextureDelegate(assetName: String?, assets: DrifterAssets): AssetDelegate<Texture>(assetName, assets) {
    constructor(assets: DrifterAssets) : this(null, assets)

    override fun path(name: String) = "texture/$name.png"
    override val type = Texture::class.java
}