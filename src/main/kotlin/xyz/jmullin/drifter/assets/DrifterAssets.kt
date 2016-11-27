package xyz.jmullin.drifter.assets

import com.badlogic.gdx.Audio
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.TextureLoader
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.reflect.ClassReflection
import com.badlogic.gdx.utils.reflect.Field
import xyz.jmullin.drifter.assets.delegates.FontDelegate
import xyz.jmullin.drifter.assets.delegates.TextureDelegate

/**
 * Used for loading and storing an assets library.
 *
 * '''To use:'''
 *
 * Create a class or object which extends from DrifterAssets, and define one field per asset to load.
 * Assets will be loaded from the classpath based on field type, using the class mapping defined in
 * DrifterAssets.PrefixMap to determine the relative path and file extension to load from.  File names
 * must match the field name specified except in the case of Animation, which takes a suffix indicating the
 * number of frames to load and looks for all files named with that pattern from 1 to N.
 *
 * Call load() to begin loading assets.  To perform inline loading, consult the manager member (see [[AssetManager]] for
 * more information), otherwise simply call finishLoading() to force the application to block until load is complete.
 *
 * After load is completed, call populate() to dump the loaded assets into their respective fields.  After this
 * point these fields can be used to reference assets.
 *
 * @param atlasName Optional filename of the TextureAtlas to load sprites from.
 */
open class DrifterAssets(atlasName: String? = null) {
    val manager = AssetManager()

    var primaryAtlas: TextureAtlas? = null
    val atlasPath = atlasName?.let { "atlas/$it.atlas" }

    val texture = TextureDelegate(this)
    val font = FontDelegate(this)

    fun texture(name: String) = TextureDelegate(name, this)
    fun font(name: String) = TextureDelegate(name, this)

    /**
     * Retrieves a list of fields from ''this''.
     */
    fun fields() = ClassReflection.getDeclaredFields(this.javaClass)

    /**
     * Triggers loading of all assets defined in ''this''.
     */
    fun load() {
        atlasPath?.let { path ->
            manager.load(path, TextureAtlas::class.java)
        }

        for(field in fields()) {
            println("Loading ${field.name} / ${field.type}")
            if(PrefixMap.contains(field.type)) {
                manager.load(getAssetPath(field), field.type)
            }
        }
    }

    /**
     * Forces the asset manager to complete load of any in-process assets.
     */
    fun finishLoading() {
        manager.finishLoading()
    }

    /**
     * Populates the fields of ''this'' with the loaded assets.
     */
    fun populate() {
        primaryAtlas = atlasPath.let { path ->
            manager.get(path, TextureAtlas::class.java)
        }

        for(field in fields()) {
            if(PrefixMap.contains(field.type)) {
                val path = getAssetPath(field)

                field.isAccessible = true
                field.set(this, manager.get(path, field.type))
            }
        }

        for(field in fields()) {
            if(field.type == Sprite::class.java) {
                if(primaryAtlas == null) {
                    throw DrifterAssetsException("No texture atlas loaded to pull sprite '${field.name}' from. At least one TextureAtlas is required to load Sprites.")
                }

                field.isAccessible = true
                val sprite = primaryAtlas?.createSprite(field.name) ?: throw DrifterAssetsException("Failed to load sprite '${field.name}.' Is the sprite present in the texture atlas?")
                field.set(this, sprite)
            }
            /*if(field.type == Animation::class.java) {
                val pattern = """([a-zA-Z]+)(\d+)""".r

                field.name match {
                    case pattern(baseName, frames) ->
                    var sprites = Vector[Sprite]()

                    for(i <- 1 to frames.toInt) {
                    sprites :+= primaryAtlas.get.createSprite(baseName, i)
                }

                    field.setAccessible(true)
                    field.set(this, new Animation(sprites))
                }
            }*/
        }
    }

    fun getAssetPath(field: Field) = PrefixMap[field.type]?.pathPrefix + field.name + PrefixMap[field.type]?.fileSuffix

    fun getAssetPath(t: Class<*>, n: String) = PrefixMap[t]?.pathPrefix + n + PrefixMap[t]?.fileSuffix

    fun dispose() {
        primaryAtlas?.dispose()
    }

    companion object DrifterAssets {
        /**
         * Map of classes to asset types.  Currently the assumption is a single type of asset will always correspond
         * to a single well-defined path and file extension.
         */
        val PrefixMap = mapOf<Class<*>, AssetType>(
            Texture::class.java to AssetType("texture/", ".png"),
            BitmapFont::class.java to AssetType("font/", ".fnt"),
            Sound::class.java to AssetType("sound/", ".wav"),
            Music::class.java to AssetType("music/", ".ogg"),
            Skin::class.java to AssetType("skin/", ".json"),
            TextureAtlas::class.java to AssetType("atlas/", ".atlas")
        )
    }
}
