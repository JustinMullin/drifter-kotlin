package xyz.jmullin.drifter.entity

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.extensions.*
import xyz.jmullin.drifter.extensions.V2

/**
 * 2-dimensional entity, contains scaffolding on top of Entity for tracking 2d position and orientation.
 * An Entity2D can be added/removed from a Layer2D, which will take care of calling update/render periodically.
 */
open class Entity2D : EntityContainer2D, Entity() {
    // Implicits for local context
    fun self() = this
    override fun layer() = parent?.layer()

    override var children = emptyList<Entity2D>()

    /**
     * This entity's parent container, or None if it's not attached.
     */
    var parent: EntityContainer2D? = null

    /**
     * World position of the entity.
     */
    val position = V2(0, 0)
    /**
     * World size of the entity.
     */
    val size = V2(0, 0)
    /**
     * Depth of the entity, used for Z sorting (entities with lower depth will appear on top
     * of entities with higher depth)
     */
    var depth = 0

    // Convenience methods for referring to position and size components directly
    val x: Float get() = position.x
    val y: Float get() = position.y
    val width: Float get() = size.x
    val height: Float get() = size.y
    val _bounds = Rectangle()
    val bounds: Rectangle get() = _bounds.set(x, y, width, height)

    /**
     * Called by the parent container when this entity is added to it.  Override to perform some
     * action after the parent container has been assigned.
     *
     * @param container The new parent container.
     */
    fun create(container: EntityContainer2D) {}

    /**
     * Called by the parent container on each frame to render this entity.
     *
     * @param batch Active SpriteBatch to use in rendering.
     */
    open fun render(batch: SpriteBatch) {
        renderChildren(batch)
    }

    /**
     * Called by the parent container at each tick to update this entity.
     *
     * @param delta Time in seconds elapsed since the last update tick.
     */
    override fun update(delta: Float) {
        updateChildren(delta)

        super.update(delta)
    }

    /**
     * Remove this entity from its parent container, if any.
     */
    fun remove() {
        layer()?.remove(this)
        parent = null
    }

    /**
     * Replace this entity with another, substituting its parent, position and size.
     *
     * @param e Entity to replace this with.
     */
    fun replaceWith(e: Entity2D) {
        layer()?.let {
            it.add(e)
            e.position.set(position)
            e.size.set(size)
            remove()
        }
    }

    /**
     * Returns true if the given point (in world coordinates) intersects with this entity.
     * By default this is determined using the entity's bounds (position and size) and checks
     * for collisions with children as well, returning true if any children intersect.
     *
     * @param v The point to check.
     * @return True if the point is contained in this entity
     */
    fun containsPoint(v: Vector2): Boolean = bounds.contains(v) || children.find { it.containsPoint(v) } != null

    /**
     * Unprojects a screen coordinate into the world space of the parent layer.
     *
     * @param v Point to unproject.
     * @return The corresponding world space coordinate.
     */
    fun unproject(v: Vector2) = layer()?.viewport?.unproject(v.cpy()) ?: V2(0, 0)
}