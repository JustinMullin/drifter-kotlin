package xyz.jmullin.drifter.application

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.profiling.GLErrorListener
import com.badlogic.gdx.graphics.profiling.GLProfiler
import xyz.jmullin.drifter.assets.DrifterAssets

open class DrifterGame(val name: String, val assets: DrifterAssets) : Game() {
    var debugGl = false
    var devMode = false
    var skipCutscenes = false

    override fun create() {
        assets.load()
        assets.finishLoading()
        assets.populateAtlas()

        if(devMode) {
            GLProfiler.enable()
            GLProfiler.listener = GLErrorListener { error ->
                if(debugGl) GlError("GLProfiler: Got GL error " + GLProfiler.resolveErrorNumber(error)).printStackTrace()
            }
        }
    }

    class GlError(message: String) : Throwable(message)
}