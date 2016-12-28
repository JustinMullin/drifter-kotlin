package xyz.jmullin.drifter.pathfinding

import java.util.*

/**
 * A generic A* implementation for simple pathfinding in arbitrary discrete spaces.
 */
@Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
open class Pathfinder<State>(
        val heuristic: (State, State) -> Float,
        val neighbors: (State) -> Collection<State>,
        val cost: (State, State) -> Float) {

    data class PathEntry<State>(val state: State, val estimatedCost: Float = 0f) : Comparable<PathEntry<State>> {
        override fun compareTo(other: PathEntry<State>) = estimatedCost.compareTo(other.estimatedCost)
    }

    data class Path<out State>(val steps: List<State>)

    fun findPath(graph: Collection<State>, start: State, goal: State): Path<State> {
        val frontier = PriorityQueue<PathEntry<State>>()
        frontier.add(PathEntry(start, 0f))

        var cameFrom = mapOf<State, State?>(Pair(start, null))
        var costSoFar = mapOf(Pair(start, 0f))

        while (frontier.isNotEmpty()) {
            val (c, p) = frontier.poll()
            val current = c!!

            if(current == goal) break

            for (next in neighbors(current)) {
                val newCost = costSoFar[current]!! + cost(current, next)
                if(!costSoFar.contains(next) || newCost < costSoFar[next]!!) {
                    costSoFar += Pair(next, newCost)
                    val priority = newCost + heuristic(goal, next)
                    frontier.offer(PathEntry(next, priority))
                    cameFrom += Pair(next, current)
                }
            }
        }

        return reconstructPath(goal, cameFrom.filter { a -> true })
    }

    fun reconstructPath(goal: State, cameFrom: Map<State, State?>): Path<State> {
        var current = goal
        var steps = listOf(goal)

        while (cameFrom[current] != null) {
            current = cameFrom[current]!!
            steps += current
        }

        return Path(steps.reversed())
    }
}