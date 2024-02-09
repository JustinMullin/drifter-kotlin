package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.Actor

var Actor.position get() = V2(width, height)
                   set(v: Vector2) { setPosition(v.x, v.y) }
