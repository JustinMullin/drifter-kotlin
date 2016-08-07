package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.Gdx
import xyz.jmullin.drifter.extensions.*

/**
 * Short aliases and convenience methods for interacting with Gdx globals.
 */
fun mouseX() = Gdx.input.x
fun mouseY() = gameH() - Gdx.input.y
fun mouseV() = V2(mouseX(), mouseY())
fun rawMouseV() = V2(Gdx.input.x, Gdx.input.y)
fun mouseVelocity() = V2(Gdx.input.deltaX, Gdx.input.deltaY)
fun gameW() = Gdx.graphics.width
fun gameH() = Gdx.graphics.height
fun gameSize() = V2(gameW(), gameH())
fun gameFps() = Gdx.graphics.framesPerSecond