package xyz.jmullin.drifter.gl

import com.badlogic.gdx.graphics.GL20.*

enum class Blend(val gl: Int) {
    Zero                   (GL_ZERO),
    One                    (GL_ONE),
    SourceColor            (GL_SRC_COLOR),
    OneMinusSourceColor    (GL_ONE_MINUS_SRC_COLOR),
    DestColor              (GL_DST_COLOR),
    OneMinusDestColor      (GL_ONE_MINUS_DST_COLOR),
    SourceAlpha            (GL_SRC_ALPHA),
    OneMinusSourceAlpha    (GL_ONE_MINUS_SRC_ALPHA),
    DestAlpha              (GL_DST_ALPHA),
    OneMinusDestAlpha      (GL_ONE_MINUS_DST_ALPHA),
    ConstantColor          (GL_CONSTANT_COLOR),
    OneMinusConstantColor  (GL_ONE_MINUS_CONSTANT_COLOR),
    ConstantAlpha          (GL_CONSTANT_ALPHA),
    OneMinusConstantAlpha  (GL_ONE_MINUS_CONSTANT_ALPHA);

    companion object {
        val Alpha = SourceAlpha to OneMinusSourceAlpha
        val Additive = SourceAlpha to One
    }
}