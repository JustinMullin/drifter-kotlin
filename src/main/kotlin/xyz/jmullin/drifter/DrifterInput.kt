package xyz.jmullin.drifter

import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.entity.InputDefaults
import xyz.jmullin.drifter.extensions.*

/**
 * Input adapter that wraps x,y pairs into vectors.
 */
interface DrifterInput : InputDefaults {
    override fun touchDown(x: Int, y: Int, pointer: Int, button: Int) = touchDown(V2(x, y), pointer, button)
    override fun touchUp(x: Int, y: Int, pointer: Int, button: Int) = touchUp(V2(x, y), pointer, button)
    override fun touchDragged(x: Int, y: Int, pointer: Int): Boolean {
        touchDragged(V2(x, y), pointer)
        return mouseMoved(V2(x, y))
    }
    override fun mouseMoved(x: Int, y: Int) = mouseMoved(V2(x, y))

    fun touchDown(v: Vector2, pointer: Int, button: Int) = false
    fun touchUp(v: Vector2, pointer: Int, button: Int) = false
    fun touchDragged(v: Vector2, pointer: Int) = false
    fun mouseMoved(v: Vector2) = false
}
