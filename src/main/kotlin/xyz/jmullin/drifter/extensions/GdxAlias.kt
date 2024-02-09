package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.application.DrifterGame

/**
 * Short aliases and convenience methods for interacting with Gdx globals.
 */
fun drifter() = Gdx.app.applicationListener as DrifterGame
fun mouseX() = Gdx.input.x
fun mouseY() = gameH() - Gdx.input.y
fun mouseV() = V2(mouseX(), mouseY())
fun rawMouseV() = V2(Gdx.input.x, Gdx.input.y)
fun mouseVelocity() = V2(Gdx.input.deltaX, Gdx.input.deltaY)
fun moveMouse(v: Vector2) = Gdx.input.setCursorPosition(v.xI, v.yI)
fun gameW() = Gdx.graphics.width
fun gameH() = Gdx.graphics.height
fun gameWRaw() = Gdx.graphics.backBufferWidth
fun gameHRaw() = Gdx.graphics.backBufferHeight
fun gameSize() = V2(gameW(), gameH())
fun gameSizeRaw() = V2(gameWRaw(), gameHRaw())
fun gameBounds() = Rect(gameSize())
fun gameBoundsRaw() = Rect(gameSizeRaw())
fun gameFps() = Gdx.graphics.framesPerSecond
