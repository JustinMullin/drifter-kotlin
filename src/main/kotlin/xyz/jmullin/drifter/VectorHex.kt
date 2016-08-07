package xyz.jmullin.drifter

import xyz.jmullin.drifter.extensions.*

data class VectorHex(val q: Float, val r: Float, val s: Float) {
    init {
        assert(q + r + s == 0f, { "Hex coordinates don't meet q+r+s = 0 equality invariant." })
    }

    constructor(q: Float, s: Float): this(q, -q-s, s)
    constructor(q: Int, r: Int, s: Int): this(q*1f, r*1f, s*1f)

    operator fun plus(o: VectorHex) = VectorHex(q + o.q, r + o.r, s + o.s)
    operator fun minus(o: VectorHex) = VectorHex(q - o.q, r - o.r, s - o.s)
    operator fun times(o: VectorHex) = VectorHex(q * o.q, r * o.r, s * o.s)
    operator fun div(o: VectorHex) = VectorHex(q / o.q, r / o.r, s / o.s)

    fun inverse() = VectorHex(-q, -r, -s)
    fun fixZeroes() = VectorHex(if(q == 0.0f) 0.0f else q, if(r == 0.0f) 0.0f else r, if(s == 0.0f) 0.0f else s)

    fun toV() = V2(3f.sqrt() * (q + r / 2), 3 / 2 * r)

    fun snap() = {
        val rQ = q.round()
        val rR = r.round()
        val rS = s.round()

        if ((rQ - q).abs() > (rR - r).abs() && (rQ - q).abs() > (rS - s).abs()) {
            VectorHex(-rR-rS, rR, rS)
        } else if ((rR - r).abs() > (rS - s).abs()) {
            VectorHex(rQ, -rQ-rS, rS)
        } else {
            VectorHex(rQ, rR, -rQ-rR)
        }
    }
}