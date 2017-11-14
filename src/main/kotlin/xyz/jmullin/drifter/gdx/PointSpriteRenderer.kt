package xyz.jmullin.drifter.gdx

import com.badlogic.gdx.graphics.g3d.particles.ParallelArray
import com.badlogic.gdx.graphics.g3d.particles.ParticleChannels
import com.badlogic.gdx.graphics.g3d.particles.ParticleControllerComponent
import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch
import com.badlogic.gdx.graphics.g3d.particles.renderers.ParticleControllerRenderer
import com.badlogic.gdx.graphics.g3d.particles.renderers.PointSpriteControllerRenderData

class PointSpriteRenderer() : ParticleControllerRenderer<PointSpriteControllerRenderData, PointSpriteParticleBatch>(PointSpriteControllerRenderData()) {

    constructor(batch: PointSpriteParticleBatch) : this() {
        setBatch(batch)
    }

    override fun allocateChannels() {
        renderData.positionChannel = controller.particles.addChannel<ParallelArray.FloatChannel>(ParticleChannels.Position)
        renderData.regionChannel = controller.particles.addChannel(ParticleChannels.TextureRegion, ParticleChannels.TextureRegionInitializer.get())
        renderData.colorChannel = controller.particles.addChannel(ParticleChannels.Color, ParticleChannels.ColorInitializer.get())
        renderData.scaleChannel = controller.particles.addChannel(ParticleChannels.Scale, ParticleChannels.ScaleInitializer.get())
        renderData.rotationChannel = controller.particles.addChannel(ParticleChannels.Rotation2D, ParticleChannels.Rotation2dInitializer.get())
    }

    override fun isCompatible(batch: ParticleBatch<*>): Boolean {
        return batch is PointSpriteParticleBatch
    }

    override fun copy(): ParticleControllerComponent {
        return PointSpriteRenderer(batch)
    }

}