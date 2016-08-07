package xyz.jmullin.drifter.assets

/**
 * Information on the location and file type of an asset to be loaded into the application.
 *
 * @param pathPrefix Prefix from the assets root where the asset can be found.
 * @param fileSuffix File extension to use in loading the asset file.
 */
data class AssetType(val pathPrefix: String, val fileSuffix: String)