package xyz.jmullin.drifter.entity

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.application.DrifterInput

/**
 * General purpose container for Entity2Ds, allows attachment and management of children entities and passes
 * through render, update and input events to attached children appropriately.
 */
interface EntityContainer2D : DrifterInput {
    // Implicit layer for local context
    fun layer(): Layer2D?

    /**
     * List of all attached children.
     */
    var children: List<Entity2D>

    /**
     * Remove an entity from this container and any of its children.
     *
     * @param e Entity to remove.
     */
    fun remove(e: Entity2D) {
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
    fun add(e: Entity2D): Entity2D {
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
    fun renderChildren(batch: SpriteBatch) {
        children = children.sortedBy { -it.depth }

        children.forEach { it.render(batch) }
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

    override fun touchDown(v: Vector2, pointer: Int, button: Int): Boolean {
        return children.find { it.containsPoint(v) && it.touchDown(v, pointer, button) } != null
    }

    override fun touchUp(v: Vector2, pointer: Int, button: Int): Boolean {
        return children.find { it.containsPoint(v) && it.touchUp(v, pointer, button) } != null
    }

    override fun touchDragged(v: Vector2, pointer: Int): Boolean {
        return children.find { it.containsPoint(v) && it.touchDragged(v, pointer) } != null
    }

    override fun mouseMoved(v: Vector2): Boolean {
        return children.find { it.containsPoint(v) && it.mouseMoved(v) } != null
    }

    override fun scrolled(amount: Int): Boolean {
        return children.find { it.scrolled(amount) } != null
    }
}
