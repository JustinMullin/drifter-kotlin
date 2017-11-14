package xyz.jmullin.drifter.control

import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerAdapter
import com.badlogic.gdx.controllers.Controllers
import xyz.jmullin.drifter.debug.log

abstract class DropInControllerManager {
    abstract fun playerJoined(id: Int, controller: Controller)
    abstract fun playerLeft(id: Int, controller: Controller)
    abstract fun playerRejoined(id: Int, controller: Controller)

    private var slots = Array<ControllerSlot>(8, { Empty })

    private val firstAvailable: Int get() = slots.indexOfFirst { it == Empty || it == Inactive }

    val adapter = object : ControllerAdapter() {
        override fun connected(controller: Controller?) {
            controller?.let {
                val num = firstAvailable + 1
                val existing = slots[firstAvailable]
                slots[firstAvailable] = Active(controller)
                when(existing) {
                    is Inactive -> {
                        log("Player $num reconnected (${controller.name}).")
                        playerRejoined(num, controller)
                    }
                    is Empty -> {
                        log("Player $num connected (${controller.name}).")
                        playerJoined(num, controller)
                    }
                }
            }

            super.connected(controller)
        }

        override fun disconnected(controller: Controller?) {
            controller?.let {
                val index = slots.indexOf(Active(controller))
                val num = index + 1
                slots[index] = Inactive
                log("Player $num disconnected (${controller.name}).")
                playerLeft(num, controller)
            }

            super.disconnected(controller)
        }
    }

    init {
        Controllers.addListener(adapter)
        Controllers.getControllers().forEach(adapter::connected)
    }

    private interface ControllerSlot
    private data class Active(val controller: Controller) : ControllerSlot
    private object Inactive : ControllerSlot
    private object Empty : ControllerSlot
}