package xyz.jmullin.drifter.extensions

/*
 * Copyright 2015 Michael Rozumyanskiy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
val Pi: Float = Math.PI.toFloat()
val E: Float = Math.E.toFloat()
val Epsilon = 0.00000001f

fun Float.sin(): Float = FloatMath.sin(this)
fun Float.cos(): Float = FloatMath.cos(this)
fun Float.tan(): Float = FloatMath.tan(this)
fun Float.asin(): Float = FloatMath.asin(this)
fun Float.acos(): Float = FloatMath.acos(this)
fun Float.atan(): Float = FloatMath.atan(this)
fun Float.toRadians(): Float = FloatMath.toRadians(this)
fun Float.toDegrees(): Float = FloatMath.toDegrees(this)
fun Float.exp(): Float = FloatMath.exp(this)
fun Float.log(): Float = FloatMath.log(this)
fun Float.log10(): Float = FloatMath.log10(this)
fun Float.sqrt(): Float = FloatMath.sqrt(this)
fun Float.cbrt(): Float = FloatMath.cbrt(this)
fun Float.IEEEremainder(divisor: Float): Float = FloatMath.IEEEremainder(this, divisor)
fun Float.ceil(): Float = FloatMath.ceil(this)
fun Float.floor(): Float = FloatMath.floor(this)
fun Float.rint(): Float = FloatMath.rint(this)
fun Float.atan2(x: Float): Float = FloatMath.atan2(this, x)
fun Float.pow(exp: Float): Float = FloatMath.pow(this, exp)
fun Float.round(): Int = Math.round(this)
fun Float.abs(): Float = Math.abs(this)
fun Float.ulp(): Float = Math.ulp(this)
fun Float.signum(): Float = Math.signum(this)
fun Float.sinh(): Float = FloatMath.sinh(this)
fun Float.cosh(): Float = FloatMath.cosh(this)
fun Float.tanh(): Float = FloatMath.tanh(this)
fun Float.expm1(): Float = FloatMath.expm1(this)
fun Float.log1p(): Float = FloatMath.log1p(this)
fun Float.copySign(sign: Float): Float = Math.copySign(this, sign)
fun Float.exponent(): Int = Math.getExponent(this)
fun Float.next(direction: Float): Float = FloatMath.nextAfter(this, direction)
fun Float.next(direction: Double): Float = Math.nextAfter(this, direction)
fun Float.nextUp(): Float = Math.nextUp(this)
fun Float.scalb(scaleFactor: Int): Float = Math.scalb(this, scaleFactor)
fun Float.clamp(min: Float, max: Float): Float = Math.max(min, Math.min(this, max))
fun Float.fEq(o: Float): Boolean = Math.abs(this-o) <= Epsilon
fun Float.lerp(b: Float, alpha: Float): Float = this + (b-this) * alpha
fun Float.lerpRelative(b: Float, c: Float): Float = FloatMath.lerp(this, b, FloatMath.alpha(this, b, c))
fun Float.alpha(b: Float, c: Float): Float = (c - this) / (b - this)

object FloatMath {
    fun abs(value: Float): Float = Math.abs(value)
    fun min(a: Float, b: Float): Float = Math.min(a, b)
    fun max(a: Float, b: Float): Float = Math.max(a, b)
    fun round(value: Float): Float = Math.round(value.toDouble()).toFloat()

    fun sin(value: Float): Float = Math.sin(value.toDouble()).toFloat()
    fun cos(value: Float): Float = Math.cos(value.toDouble()).toFloat()
    fun tan(value: Float): Float = Math.tan(value.toDouble()).toFloat()
    fun sqrt(value: Float): Float = Math.sqrt(value.toDouble()).toFloat()
    fun acos(value: Float): Float = Math.acos(value.toDouble()).toFloat()
    fun asin(value: Float): Float = Math.asin(value.toDouble()).toFloat()
    fun atan(value: Float): Float = Math.atan(value.toDouble()).toFloat()
    fun atan2(x: Float, y: Float): Float = Math.atan2(x.toDouble(), y.toDouble()).toFloat()
    fun pow(x: Float, y: Float): Float = Math.pow(x.toDouble(), y.toDouble()).toFloat()
    fun ceil(x: Float): Float = Math.ceil(x.toDouble()).toFloat()
    fun floor(x: Float): Float = Math.floor(x.toDouble()).toFloat()
    fun toRadians(angdeg: Float): Float = Math.toRadians(angdeg.toDouble()).toFloat()
    fun toDegrees(angrad: Float): Float = Math.toDegrees(angrad.toDouble()).toFloat()
    fun exp(x: Float): Float = Math.exp(x.toDouble()).toFloat()
    fun log(x: Float): Float = Math.log(x.toDouble()).toFloat()
    fun log10(x: Float): Float = Math.log10(x.toDouble()).toFloat()
    fun cbrt(x: Float): Float = Math.cbrt(x.toDouble()).toFloat()
    fun IEEEremainder(x: Float, y: Float): Float = Math.IEEEremainder(x.toDouble(), y.toDouble()).toFloat()
    fun rint(x: Float): Float = Math.rint(x.toDouble()).toFloat()
    fun sinh(x: Float): Float = Math.sinh(x.toDouble()).toFloat()
    fun cosh(x: Float): Float = Math.cosh(x.toDouble()).toFloat()
    fun tanh(x: Float): Float = Math.tanh(x.toDouble()).toFloat()
    fun expm1(x: Float): Float = Math.expm1(x.toDouble()).toFloat()
    fun log1p(x: Float): Float = Math.log1p(x.toDouble()).toFloat()
    fun nextAfter(start: Float, direction: Float): Float = Math.nextAfter(start, direction.toDouble())
    fun clamp(value: Float, min: Float, max: Float): Float = Math.max(min, Math.min(value, max))

    fun lerp(a: Float, b: Float, alpha: Float): Float = a + (b-a) * alpha
    fun lerpRelative(a: Float, b: Float, c: Float): Float = lerp(a, b, alpha(a, b, c))
    fun alpha(a: Float, b: Float, c: Float): Float = (c - a) / (b - a)
}