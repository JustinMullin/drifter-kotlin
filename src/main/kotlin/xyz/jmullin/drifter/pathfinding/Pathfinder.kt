package xyz.jmullin.drifter.pathfinding

import java.util.*

/**
 * A generic A* implementation for simple pathfinding in arbitrary discrete spaces.
 */
@Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
open class Pathfinder<State>(
        val heuristic: (State) -> Float,
        val neighbors: (State) -> Collection<Pair<State, Float>>) {

    data class PathEntry<State>(val state: State, val estimatedCost: Float = 0f) : Comparable<PathEntry<State>> {
        override fun compareTo(other: PathEntry<State>) = estimatedCost.compareTo(other.estimatedCost)
    }

    data class Path<out State>(val steps: List<State>)

    fun findPath(start: State, isGoal: (State) -> Boolean): Path<State> {
        val frontier = PriorityQueue<PathEntry<State>>()
        frontier.add(PathEntry(start, 0f))

        val cameFrom = mutableMapOf<State, State?>(start to null)
        val costSoFar = mutableMapOf(start to 0f)
        var goalState: State? = null

        while (frontier.isNotEmpty()) {
            val (c, p) = frontier.poll()
            val current = c!!

            if(isGoal(current)) {
                goalState = current
                break
            }
            for ((next, cost) in neighbors(current)) {
                val newCost = costSoFar[current]!! + cost
                if(!costSoFar.contains(next) || newCost < costSoFar.getValue(next)) {
                    costSoFar += Pair(next, newCost)
                    val priority = newCost + heuristic(next)
                    frontier.offer(PathEntry(next, priority))
                    cameFrom += Pair(next, current)
                }
            }
        }

        return reconstructPath(goalState!!, cameFrom)
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
