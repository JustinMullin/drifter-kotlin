package xyz.jmullin.drifter.application

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import xyz.jmullin.drifter.debug.Screenshot
import xyz.jmullin.drifter.extensions.game

class DebugInput() : DrifterInput {
    override fun keyDown(keycode: Int): Boolean {
        if(game().devMode) when (keycode) {
            Input.Keys.ESCAPE -> Gdx.app.exit()
            Input.Keys.F12 -> Screenshot.save("screenshots/${game().name}")
        }

        return super.keyDown(keycode)
    }
}