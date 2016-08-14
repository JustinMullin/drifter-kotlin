package xyz.jmullin.drifter.geometry

import com.badlogic.gdx.math.Vector2
import xyz.jmullin.drifter.extensions.*

data class VectorHex(val q: Float, val r: Float, val s: Float) {
    init {
        assert(q + r + s == 0f, { "Hex coordinates don't meet q+r+s = 0 equality invariant." })
    }

    constructor(q: Float, s: Float): this(q, -q-s, s)
    constructor(q: Int, s: Int): this(q*1f, s*1f)
    constructor(q: Int, r: Int, s: Int): this(q*1f, r*1f, s*1f)

    operator fun plus(o: VectorHex) = VectorHex(q + o.q, r + o.r, s + o.s)
    operator fun minus(o: VectorHex) = VectorHex(q - o.q, r - o.r, s - o.s)
    operator fun times(o: VectorHex) = VectorHex(q * o.q, r * o.r, s * o.s)
    operator fun div(o: VectorHex) = VectorHex(q / o.q, r / o.r, s / o.s)

    fun inverse() = VectorHex(-q, -r, -s)
    fun fixZeroes() = VectorHex(if(q == 0.0f) 0.0f else q, if(r == 0.0f) 0.0f else r, if(s == 0.0f) 0.0f else s)

    fun manhattanTo(o: VectorHex) = ((q - o.q).abs() + (r - o.r).abs() + (s - o.s).abs()) / 2f
    fun neighbors() = directions.map { plus(it) }

    fun toV() = V2(3f/2f * q, 3f.sqrt() * (r + q/2f))

    fun hexCorner(cornerIndex: Int): Vector2 {
        val angle = (Pi / 3f) * cornerIndex
        return V2(angle.cos(), angle.sin())
    }

    fun cornerOffsets(size: Vector2) = (0..5).map { size * hexCorner(it) }

    fun snap(): VectorHex {
        val rQ = q.round()
        val rR = r.round()
        val rS = s.round()

        return if ((rQ - q).abs() > (rR - r).abs() && (rQ - q).abs() > (rS - s).abs()) {
            Vh(-rR-rS, rR, rS)
        } else if ((rR - r).abs() > (rS - s).abs()) {
            Vh(rQ, -rQ-rS, rS)
        } else {
            Vh(rQ, rR, -rQ-rR)
        }
    }

    override fun equals(other: Any?): Boolean {
        if(other !is VectorHex) return false
        return q.fEq(other.q) && r.fEq(other.r) && s.fEq(other.s)
    }

    override fun hashCode(): Int {
        return 23 + (q+0f).hashCode()*31 + (r+0f).hashCode()*31*31 + (s+0f).hashCode()*31*31*31
    }

    companion object {
        val directions = listOf(
                Vh(+1f, -1f, 0f), Vh(+1f, 0f, -1f), Vh(0f, +1f, -1f),
                Vh(-1f, +1f, 0f), Vh(-1f, 0f, +1f), Vh(0f, -1f, +1f)
        )
    }
}