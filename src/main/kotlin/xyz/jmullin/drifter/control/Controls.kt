package xyz.jmullin.drifter.control

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.Controllers
import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.extensions.FloatMath
import xyz.jmullin.drifter.extensions.V2
import xyz.jmullin.drifter.extensions.abs

class Controls(var controller: Controller?) {
    class ButtonMapping(vararg val bindings: ButtonBinding) {
        var down = false
        var justPressed = false

        val pressed: Boolean get() = bindings.map { it.pressed() }.any { it }

        fun update() {
            justPressed = false
            if(pressed) {
                if(!down) {
                    justPressed = true
                }
                down = true
            } else {
                down = false
            }
        }
    }

    class AxisMapping(vararg val bindings: AxisBinding) {
        val value: Float get() = bindings.map { it.value() }.maxBy(Float::abs) ?: 0f
    }

    interface ButtonBinding {
        fun pressed(): Boolean
    }

    interface AxisBinding {
        fun value(): Float
    }

    inner class XboxButtonBinding(val code: Int) : ButtonBinding {
        override fun pressed() = controller?.getButton(code) ?: false
    }

    inner class KeyboardButtonBinding(val code: Int) : ButtonBinding {
        override fun pressed() = Gdx.input.isKeyPressed(code)
    }

    inner class XboxAxisBinding(val code: Int, val deadZone: Float, val flip: Boolean) : AxisBinding {
        override fun value(): Float {
            val axis = controller?.getAxis(code) ?: 0f
            return if(FloatMath.abs(axis) <= deadZone) 0f else (if(flip) -axis else axis)
        }
    }

    inner class KeyboardAxisBinding(val negativeCode: Int, val positiveCode: Int) : AxisBinding {
        override fun value(): Float {
            var sum = 0f
            if(Gdx.input.isKeyPressed(negativeCode)) sum -= 1f
            if(Gdx.input.isKeyPressed(positiveCode)) sum += 1f
            return sum
        }
    }

    fun update() {
        buttonMappings.forEach(ButtonMapping::update)
    }

    private fun xB(code: Int) = XboxButtonBinding(code)
    private fun kB(code: Int) = KeyboardButtonBinding(code)
    private fun xA(code: Int, deadZone: Float=0f, flip: Boolean = false) = XboxAxisBinding(code, deadZone, flip)
    private fun kA(negativeCode: Int, positiveCode: Int) = KeyboardAxisBinding(negativeCode, positiveCode)

    private val jumpMapping = ButtonMapping(xB(Xbox.A), kB(Input.Keys.SPACE))
    private val attackMapping = ButtonMapping(xB(Xbox.X), kB(Input.Keys.F))
    private val optionsMapping = ButtonMapping(xB(Xbox.Y), kB(Input.Keys.D))
    private val diveMapping = ButtonMapping(xB(Xbox.B), kB(Input.Keys.V))
    private val nextMapping = ButtonMapping(xB(Xbox.R_BUMPER), kB(Input.Keys.O))
    private val previousMapping = ButtonMapping(xB(Xbox.L_BUMPER), kB(Input.Keys.U))

    private val moveXMapping = AxisMapping(xA(Xbox.L_STICK_HORIZONTAL_AXIS), kA(Input.Keys.LEFT, Input.Keys.RIGHT), kA(Input.Keys.A, Input.Keys.D))
    private val moveYMapping = AxisMapping(xA(Xbox.L_STICK_VERTICAL_AXIS, flip=true), kA(Input.Keys.DOWN, Input.Keys.UP), kA(Input.Keys.S, Input.Keys.W))

    private val rotateXMapping = AxisMapping(xA(Xbox.R_STICK_HORIZONTAL_AXIS), kA(Input.Keys.J, Input.Keys.L))
    private val rotateYMapping = AxisMapping(xA(Xbox.R_STICK_VERTICAL_AXIS, flip=true), kA(Input.Keys.K, Input.Keys.I))

    private val forwardMapping = AxisMapping(xA(Xbox.R_TRIGGER))
    private val backMapping = AxisMapping(xA(Xbox.L_TRIGGER))

    val buttonMappings = listOf(jumpMapping, attackMapping, optionsMapping, diveMapping, nextMapping, previousMapping)

    val jumpPressed: Boolean get() = jumpMapping.pressed
    val attackPressed: Boolean get() = attackMapping.pressed
    val optionsPressed: Boolean get() = optionsMapping.pressed
    val divePressed: Boolean get() = diveMapping.pressed
    val nextPressed: Boolean get() = nextMapping.pressed
    val previousPressed: Boolean get() = previousMapping.pressed

    val jumpJustPressed: Boolean get() = jumpMapping.justPressed
    val attackJustPressed: Boolean get() = attackMapping.justPressed
    val optionsJustPressed: Boolean get() = optionsMapping.justPressed
    val diveJustPressed: Boolean get() = diveMapping.justPressed
    val nextJustPressed: Boolean get() = nextMapping.justPressed
    val previousJustPressed: Boolean get() = previousMapping.justPressed

    val moveX: Float get() = moveXMapping.value
    val moveY: Float get() = moveYMapping.value
    val moveV: Vector2 get() = V2(moveX, moveY)
    val rotateX: Float get() = rotateXMapping.value
    val rotateY: Float get() = rotateYMapping.value
    val rotate: Vector2 get() = V2(rotateX, rotateY)
    val forward: Float get() = (forwardMapping.value + 1f) / 2f
    val back: Float get() = (backMapping.value + 1f) / 2f

    companion object {
        object Xbox {
            val A = 0
            val B = 1
            val X = 2
            val Y = 3
            val GUIDE = 0
            val L_BUMPER = 8
            val R_BUMPER = 9
            val BACK = 5
            val START = 4
            val DPAD_UP = 0
            val DPAD_DOWN = 1
            val DPAD_LEFT = 2
            val DPAD_RIGHT = 3

            val L_TRIGGER = 4
            val R_TRIGGER = 5
            val L_STICK_HORIZONTAL_AXIS = 0
            val L_STICK_VERTICAL_AXIS = 1
            val R_STICK_HORIZONTAL_AXIS = 3
            val R_STICK_VERTICAL_AXIS = 4
        }
    }
}
