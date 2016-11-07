package xyz.jmullin.drifter.entity

import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport

/**
 * Abstracts a renderable layer of the screen with some viewport.  Generally layers will create
 * entities as children to update and render.  A layer can be hidden (visible=false, in which case
 * render will not be performed) and/or inactive (active=false, in which case update will not be
 * performed).
 */
interface Layer : InputProcessor {
    val index: Int
    val viewportSize: Vector2
    var viewport: Viewport?

    fun update(delta: Float)
    fun render()
    fun dispose()
    fun resize(newSize: Vector2)
}