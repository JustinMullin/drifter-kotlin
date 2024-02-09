package xyz.jmullin.drifter.extensions

import com.badlogic.gdx.math.Quaternion

operator fun Quaternion.times(q: Quaternion): Quaternion = this.cpy().mul(q)
