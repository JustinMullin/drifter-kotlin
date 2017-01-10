package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.application.DrifterGame
import xyz.jmullin.drifter.extensions.GdxAlias.gameSizeOverride

object GdxAlias {
    var gameSizeOverride: Vector2? = null

    fun fixGameSize(v: Vector2) {
        gameSizeOverride = v
    }

    fun resetGameSize() {
        gameSizeOverride = null
    }
}

/**
 * Short aliases and convenience methods for interacting with Gdx globals.
 */
fun game() = Gdx.app.applicationListener as DrifterGame
fun mouseX() = Gdx.input.x
fun mouseY() = gameH() - Gdx.input.y
fun mouseV() = V2(mouseX(), mouseY())
fun rawMouseV() = V2(Gdx.input.x, Gdx.input.y)
fun mouseVelocity() = V2(Gdx.input.deltaX, Gdx.input.deltaY)
fun gameW() = gameSizeOverride?.xI ?: Gdx.graphics.width
fun gameH() = gameSizeOverride?.yI ?: Gdx.graphics.height
fun gameWRaw() = gameSizeOverride?.xI ?: Gdx.graphics.backBufferWidth
fun gameHRaw() = gameSizeOverride?.yI ?: Gdx.graphics.backBufferHeight
fun gameSize() = V2(gameW(), gameH())
fun gameSizeRaw() = V2(gameWRaw(), gameHRaw())
fun gameFps() = Gdx.graphics.framesPerSecond