package xyz.jmullin.drifter.entity

import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.application.DrifterInput
import xyz.jmullin.drifter.rendering.RenderStage

/**
 * General purpose container for Entity2Ds, allows attachment and management of children entities and passes
 * through render, update and input events to attached children appropriately.
 */
interface EntityContainer2D : DrifterInput {
    var paused: Boolean
    var hidden: Boolean

    // Containing layer for local context
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
        children = children.filterNot { it == e }
        children.forEach { it.remove(e) }
        e.parent = null
    }

    /**
     * Adds an entity to the container and sets parent/child relationships as necessary.
     *
     * @param e Entity to add.
     * @return The added entity.
     */
    fun <T : Entity2D> add(e: T, init: Entity2D.() -> Unit = {}): T {
        children += e
        e.parent = this
        e.init()
        e.create(this)
        return e
    }

    /**
     * Draws all attached children entities.
     *
     * @param stage Active render stage to draw to.
     */
    fun renderChildren(stage: RenderStage) {
        if(!hidden) {
            children.sortedBy { -it.depth }.forEach { it.render(stage) }
        }
    }

    /**
     * Updates all attached children.
     *
     * @param delta Time elapsed since the last update tick.
     */
    fun updateChildren(delta: Float) {
        if(!paused) {
            children.sortedBy { -it.priority }.forEach { it.update(delta) }
        }
    }

    /**
     * Pauses the container, preventing update of its children.
     */
    fun pause() {
        paused = true
        children.forEach { it.pause() }
    }

    /**
     * Resumes the container if paused, allowing update of its children.
     */
    fun resume() {
        paused = false
        children.forEach { it.resume() }
    }

    /**
     * Hides the container, preventing rendering of its children.
     */
    fun hide() {
        hidden = true
        children.forEach { it.hide() }
    }

    /**
     * Shows the container if hidden, allowing rendering of its children.
     */
    fun show() {
        hidden = false
        children.forEach { it.show() }
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
