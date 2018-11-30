package xyz.jmullin.drifter.control

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerAdapter
import xyz.jmullin.drifter.extensions.FloatMath

open class Controls(val assigned: Controller?, val mappings: List<Mapping>) {
    val controllerAdapter = object : ControllerAdapter() {
        override fun buttonDown(controller: Controller?, buttonCode: Int): Boolean {
            if(controller == assigned) {
                mappings.forEach { mapping ->
                    if (mapping is ButtonMapping) {
                        mapping.bindings.forEach {
                            if (it is XboxButtonBinding && it.code == buttonCode) mapping.press()
                        }
                    }
                }
            }
            return true
        }

        override fun buttonUp(controller: Controller?, buttonCode: Int): Boolean {
            if(controller == assigned) {
                mappings.forEach { mapping ->
                    if(mapping is ButtonMapping) {
                        mapping.bindings.forEach {
                            if(it is XboxButtonBinding && it.code == buttonCode) mapping.release()
                        }
                    }
                }
            }
            return true
        }

        override fun axisMoved(controller: Controller?, axisCode: Int, value: Float): Boolean {
            if(controller == assigned) {
                mappings.forEach { mapping ->
                    if (mapping is AxisMapping) {
                        mapping.bindings.forEach {
                            when (it) {
                                is XboxAxisBinding -> if (it.code == axisCode) {
                                    mapping.move(if(FloatMath.abs(value) <= it.deadZone) 0f else (if(it.flip) -value else value))
                                }
                            }
                        }
                    }
                }
            }
            return true
        }
    }

    val inputAdapter = object : InputAdapter() {
        override fun keyDown(keycode: Int): Boolean {
            mappings.forEach { mapping ->
                if (mapping is ButtonMapping) {
                    mapping.bindings.forEach {
                        if (it is KeyboardButtonBinding && it.code == keycode) mapping.press()
                    }
                }
                if(mapping is AxisMapping) {
                    mapping.bindings.forEach {
                        if (it is KeyboardAxisBinding && it.positiveCode == keycode) mapping.move(1f)
                        if (it is KeyboardAxisBinding && it.negativeCode == keycode) mapping.move(-1f)
                    }
                }
            }
            return true
        }

        override fun keyUp(keycode: Int): Boolean {
            mappings.forEach { mapping ->
                if (mapping is ButtonMapping) {
                    mapping.bindings.forEach {
                        if (it is KeyboardButtonBinding && it.code == keycode) mapping.release()
                    }
                }
                if(mapping is AxisMapping) {
                    mapping.bindings.forEach {
                        if (it is KeyboardAxisBinding && (it.negativeCode == keycode || it.positiveCode == keycode)) {
                            if(Gdx.input.isKeyPressed(it.negativeCode)) mapping.move(-1f)
                            else if(Gdx.input.isKeyPressed(it.positiveCode)) mapping.move(1f)
                            else mapping.move(0f)
                        }
                    }
                }
            }
            return true
        }
    }

    init {
        assigned?.addListener(controllerAdapter)
    }
}

fun xB(code: Int): ButtonBinding {
    return XboxButtonBinding(code)
}
fun kB(code: Int): ButtonBinding {
    return KeyboardButtonBinding(code)
}
fun xA(code: Int, deadZone: Float=0f, flip: Boolean = false): AxisBinding {
    return XboxAxisBinding(code, deadZone, flip)
}
fun kA(negativeCode: Int, positiveCode: Int): AxisBinding {
    return KeyboardAxisBinding(negativeCode, positiveCode)
}

object Xbox {
    val A = 11
    val B = 12
    val X = 13
    val Y = 14
    val L_BUMPER = 8
    val R_BUMPER = 9

    val L_TRIGGER = 4
    val R_TRIGGER = 5
    val L_STICK_VERTICAL_AXIS = 1
    val L_STICK_HORIZONTAL_AXIS = 0
    val R_STICK_VERTICAL_AXIS = 3
    val R_STICK_HORIZONTAL_AXIS = 2

    val GUIDE = 0
    val BACK = 5
    val START = 4
    val DPAD_UP = 0
    val DPAD_DOWN = 1
    val DPAD_LEFT = 2
    val DPAD_RIGHT = 3
}

interface Mapping
class ButtonMapping(val onPress: () -> Unit,
                    val onRelease: () -> Unit,
                    vararg val bindings: ButtonBinding) : Mapping {
    fun press() { onPress() }
    fun release() { onRelease() }
}
class AxisMapping(val onMove: (Float) -> Unit,
                  vararg val bindings: AxisBinding) : Mapping {
    fun move(value: Float) { onMove(value) }
}

interface ButtonBinding
interface AxisBinding

private class XboxButtonBinding(val code: Int) : ButtonBinding
private class KeyboardButtonBinding(val code: Int) : ButtonBinding

private class XboxAxisBinding(val code: Int,
                              val deadZone: Float,
                              val flip: Boolean) : AxisBinding

private class KeyboardAxisBinding(val negativeCode: Int,
                                  val positiveCode: Int) : AxisBinding
