package xyz.jmullin.drifter.assets.delegates

import xyz.jmullin.drifter.assets.DrifterAssets
import xyz.jmullin.drifter.assets.DrifterAssetsException
import kotlin.reflect.KProperty

abstract class AssetDelegate<T>(var assetName: String?, val assets: DrifterAssets) {
    constructor(assets: DrifterAssets): this(null, assets)

    var value: T? = null

    abstract fun path(name: String): String
    abstract val type: Class<T>

    fun safeAssetName(): String = assetName ?: throw DrifterAssetsException("No asset name provided for delegate $this.")

    fun populateName(name: String) {
        assetName = name
        loadAsset()
    }

    open fun loadAsset() {
        assets.manager.load(path(safeAssetName()), type)
    }

    operator fun getValue(requestor: Any?, property: KProperty<*>): T {
        return value ?: getAsset()
    }

    open fun getAsset(): T {
        return assets.manager.get(path(safeAssetName()), type).apply {
            value = this
        }
    }
}