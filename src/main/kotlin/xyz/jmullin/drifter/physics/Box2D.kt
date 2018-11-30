package xyz.jmullin.drifter.physics

import com.badlogic.gdx.physics.box2d.BodyDef

fun bodyDef(f: BodyDef.() -> Unit): BodyDef {
    return BodyDef().apply { f(this) }
}