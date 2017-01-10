package xyz.jmullin.drifter.assets.delegates

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem
import xyz.jmullin.drifter.assets.DrifterAssets

class ParticleEffectDelegate(particleSystem: ParticleSystem, assetName: String?, assets: DrifterAssets): AssetDelegate<ParticleEffect>(assetName, assets) {
    constructor(particleSystem: ParticleSystem, assets: DrifterAssets) : this(particleSystem, null, assets)

    val parameters = ParticleEffectLoader.ParticleEffectLoadParameter(particleSystem.batches)

    override fun path(name: String) = "particle/$name.ptx"
    override val type = ParticleEffect::class.java

    override fun loadAsset() {
        assets.manager.load(path(safeAssetName()), type, parameters)
    }

    override fun getAsset(): ParticleEffect {
        return assets.manager.get<ParticleEffect>(path(safeAssetName()))
    }
}