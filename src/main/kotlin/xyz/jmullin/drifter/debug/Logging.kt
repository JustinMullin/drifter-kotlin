package xyz.jmullin.drifter.debug

import com.badlogic.gdx.Gdx
import xyz.jmullin.drifter.extensions.game

fun log(message: String) {
    if(game().devMode) println(message)
}