package xyz.jmullin.drifter.assets.delegates

import com.badlogic.gdx.graphics.Texture
import xyz.jmullin.drifter.assets.DrifterAssets
import kotlin.reflect.KProperty

abstract class AssetDelegate<T>(var assetName: String?, val assets: DrifterAssets) {
    constructor(assets: DrifterAssets): this(null, assets)

    var value: T? = null

    abstract fun path(name: String): String
    abstract val type: Class<T>

    fun safeAssetName(): String = assetName ?: throw Exception("No asset name provided for delegate $this.")

    init {
        if(assetName != null) {
            loadAsset()
        }
    }

    fun populateName(name: String) {
        assetName = name
        loadAsset()
    }

    fun loadAsset() {
        assets.manager.load(path(safeAssetName()), type)
    }

    operator fun getValue(requestor: Any?, property: KProperty<*>): T {
        return value ?: getAsset()
    }

    fun getAsset(): T {
        return assets.manager.get(path(safeAssetName()), type).apply {
            value = this
        }
    }
}