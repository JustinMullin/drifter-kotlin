package xyz.jmullin.drifter.assets.delegates

import com.badlogic.gdx.audio.Sound
import xyz.jmullin.drifter.assets.DrifterAssets

class SoundDelegate(assetName: String?, assets: DrifterAssets): AssetDelegate<Sound>(assetName, assets) {
    constructor(assets: DrifterAssets) : this(null, assets)

    override fun path(name: String) = "sound/$name.wav"
    override val type = Sound::class.java
}