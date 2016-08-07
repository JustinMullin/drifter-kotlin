package xyz.jmullin.drifter.debug

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.VertexAttributes.Usage
import com.badlogic.gdx.graphics.g3d.Environment
import com.badlogic.gdx.graphics.g3d.ModelBatch
import com.badlogic.gdx.graphics.g3d.ModelInstance
import com.badlogic.gdx.graphics.g3d.Material
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder
import xyz.jmullin.drifter.entity.Entity3D

/**
 * Draws a debug axis display to orient models in 3d space
 *
 * @param gridMin Min unit coordinate to draw grid lines at
 * @param gridMax Max unit coordinate to draw grid lines at
 * @param gridStep Size of each grid cell
 * @param axisLength Distance to draw axis segments from origin
 */
class AxisDisplay(val axisLength: Float? = 100f, val gridMin: Float = -10f, val gridMax: Float = 10f, val gridStep: Float = 1f) : Entity3D() {

    fun initModel(): ModelInstance {
        val modelBuilder = ModelBuilder()
        modelBuilder.begin()

        val gridBuilder = modelBuilder.part("grid", GL20.GL_LINES, (Usage.Position or Usage.ColorUnpacked).toLong(), Material())
        gridBuilder.setColor(Color.LIGHT_GRAY)

        for (t in gridMin.toInt()..gridMax.toInt() step gridStep.toInt()) {
            val tF = t.toFloat()
            gridBuilder.line(tF, 0f, gridMin, tF, 0f, gridMax)
            gridBuilder.line(gridMin, 0f, tF, gridMax, 0f, tF)
        }

        axisLength?.let { length ->
            val axisBuilder = modelBuilder.part("axes", GL20.GL_LINES, (Usage.Position or Usage.ColorUnpacked).toLong(), Material())
            axisBuilder.setColor(Color.RED)
            axisBuilder.line(0f, 0f, 0f, length, 0f, 0f)
            axisBuilder.setColor(Color.GREEN)
            axisBuilder.line(0f, 0f, 0f, 0f, length, 0f)
            axisBuilder.setColor(Color.BLUE)
            axisBuilder.line(0f, 0f, 0f, 0f, 0f, length)
        }

        val model = modelBuilder.end()

        return ModelInstance(model)
    }

    val instance: ModelInstance = initModel()

    override fun render(batch: ModelBatch, environment: Environment) {
        batch.render(instance)

        super.render(batch, environment)
    }
}
