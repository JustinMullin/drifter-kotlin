package xyz.jmullin.drifter.entity

import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import xyz.jmullin.drifter.application.DrifterInput
import xyz.jmullin.drifter.extensions.*

/**
 * General purpose container for Entity3Ds, allows attachment and management of children entities and passes
 * through render and update events to attached children appropriately.
 */
interface EntityContainer3D : DrifterInput {
    // Implicit layer for local context
    fun layer(): Layer3D?

    /**
     * List of all attached children.
     */
    var children: List<Entity3D>

    /**
     * If defined, the position the cursor should be locked to for picking purposes.
     */
    var mouseLocked: Vector2?

    /**
     * Remove an entity from this container and any of its children.
     *
     * @param e Entity to remove.
     */
    fun remove(e: Entity3D) {
        children = children.filterNot { it.equals(e) }
        children.forEach { it.remove(e) }
        e.parent = null
    }

    /**
     * Adds an entity to the container and sets parent/child relationships as necessary.
     *
     * @param e Entity to add.
     * @return The added entity.
     */
    fun add(e: Entity3D): Entity3D {
        children += e
        e.parent = this
        e.create(this)
        return e
    }

    /**
     * Draws all attached children entities.
     *
     * @param batch Active SpriteBatch to use in drawing.
     */
    fun renderChildren(batch: ModelBatch, environment: Environment) {
        children.forEach { it.render(batch, environment) }
    }

    /**
     * Updates all attached children.
     *
     * @param delta Time elapsed since the last update tick.
     */
    fun updateChildren(delta: Float) {
        children.forEach { it.update(delta) }
    }

    // Input events are aggregated through this container's children and coalesced to a single hit result.

    fun mouseEvent(v: Vector2, event: (Entity3D, Vector3) -> Boolean): Boolean {
        val camera = layer()?.camera

        if(camera != null) {
            val pickOrigin = mouseLocked ?: V2(mouseX(), gameH()-mouseY())
            val hits = children.map { e ->
                e.hitPoint(camera.getPickRay(pickOrigin.x, pickOrigin.y))?.let { Pair(e, it) }
            }.filterNotNull()
            val closest = hits.sortedBy { pair -> (camera.position - pair.second).len() }
            return closest.find { pair -> event(pair.first, pair.second) } != null
        }
        return false
    }

    override fun touchDown(v: Vector2, pointer: Int, button: Int): Boolean = mouseEvent(v, { e, v -> e.touchDown(v, button) })
    override fun touchUp(v: Vector2, pointer: Int, button: Int): Boolean = mouseEvent(v, { e, v -> e.touchUp(v, button) })
    override fun touchDragged(v: Vector2, pointer: Int): Boolean = mouseEvent(v, {e, v -> e.touchDragged(v) })
    override fun mouseMoved(v: Vector2): Boolean = mouseEvent(v, {e, v -> e.mouseMoved(v) })

    override fun keyDown(keycode: Int): Boolean = children.find { it.keyDown(keycode) } != null
    override fun keyUp(keycode: Int): Boolean = children.find { it.keyUp(keycode) } != null
    override fun keyTyped(character: Char): Boolean = children.find { it.keyTyped(character) } != null
    override fun scrolled(amount: Int): Boolean = children.find { it.scrolled(amount) } != null
}
