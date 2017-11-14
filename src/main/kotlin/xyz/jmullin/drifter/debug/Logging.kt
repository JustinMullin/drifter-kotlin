package xyz.jmullin.drifter.debug

import xyz.jmullin.drifter.extensions.drifter

fun log(message: String) {
    if(drifter().devMode) println(message)
}