package xyz.jmullin.drifter.entity

import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.math.Intersector
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.math.collision.BoundingBox
import com.badlogic.gdx.math.collision.Ray
import xyz.jmullin.drifter.extensions.V3

/**
 * 3-dimensional entity, contains scaffolding on top of Entity for tracking 3d position.
 * An Entity3D can be added/removed from a Layer3D, which will take care of calling update/render periodically.
 */
@Suppress("UNUSED_PARAMETER")
open class Entity3D : EntityContainer3D, Entity() {

    // Implicits for local context
    fun self() = this
    override fun layer() = parent?.layer()

    override var children = emptyList<Entity3D>()
    override var mouseLocked: Vector2? = null

    /**
     * This entity's parent container, or None if it's not attached.
     */
    var parent: EntityContainer3D? = null

    /**
     * World position of the entity.
     */
    var position = V3(0f)

    /**
     * Optional bounding box for this entity to determine simple collision.
     */
    var boundingBox: BoundingBox? = null

    /**
     * Called to determine if a ray (typically cast from the camera) intersects with this object.
     * Default implementation checks for intersection with the entity's bounding box, if present.
     *
     * @param ray Ray cast to determine the hit point.
     * @return Some containing the point at which the entity was hit, if there was a collision.
     *         None if there was no collision.
     */
    fun hitPoint(ray: Ray): Vector3? {
        val intersection = V3(0f, 0f, 0f)
        return boundingBox?.let { bounds ->
            if(Intersector.intersectRayBounds(ray, bounds, intersection)) intersection
            else null
        }
    }

    // 3D oriented mouse events
    fun touchDown(v: Vector3, button: Int) = true
    fun touchUp(v: Vector3, button: Int) = false
    fun touchDragged(v: Vector3) = false
    fun mouseMoved(v: Vector3) = false

    /**
     * Called by the parent container when this entity is added to it.  Override to perform some
     * action after the parent container has been assigned.
     *
     * @param container The new parent container.
     */
    fun create(container: EntityContainer3D) {
        parent = container
    }

    /**
     * Called by the parent container on each frame to render this entity.
     *
     * @param batch Active ModelBatch to use in rendering.
     * @param environment Environment to render within.
     */
    open fun render(batch: ModelBatch, environment: Environment) {
        renderChildren(batch, environment)
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
}