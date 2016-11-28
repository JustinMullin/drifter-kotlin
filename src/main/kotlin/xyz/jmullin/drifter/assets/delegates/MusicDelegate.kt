package xyz.jmullin.drifter.assets.delegates

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.Texture
import xyz.jmullin.drifter.assets.DrifterAssets

class MusicDelegate(assetName: String?, assets: DrifterAssets): AssetDelegate<Music>(assetName, assets) {
    constructor(assets: DrifterAssets) : this(null, assets)

    override fun path(name: String) = "music/$name.ogg"
    override val type = Music::class.java
}