package xyz.jmullin.drifter.geometry

import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.extensions.V2

enum class Direction(val v: Vector2) {
    Up(V2(0, 1)), Down(V2(0, -1)), Left(V2(-1, 0)), Right(V2(1, 0))
}