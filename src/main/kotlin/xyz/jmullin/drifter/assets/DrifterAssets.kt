package xyz.jmullin.drifter.assets

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.utils.reflect.ClassReflection
import xyz.jmullin.drifter.assets.delegates.*

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
    private val atlasPath = atlasName?.let { "atlas/$it.atlas" }

    val animation: AnimationDelegate get() = AnimationDelegate(this)
    val font: FontDelegate get() = FontDelegate(this)
    val music: MusicDelegate get() = MusicDelegate(this)
    val skin: SkinDelegate get() = SkinDelegate(this)
    val sound: SoundDelegate get() = SoundDelegate(this)
    val sprite: SpriteDelegate get() = SpriteDelegate(this)
    val pixmap: PixmapDelegate get() = PixmapDelegate(this)
    val textureAtlas: TextureAtlasDelegate get() = TextureAtlasDelegate(this)
    val texture: TextureDelegate get() = TextureDelegate(this)

    fun animation(name: String) = AnimationDelegate(name, this)
    fun font(name: String) = FontDelegate(name, this)
    fun music(name: String) = MusicDelegate(name, this)
    fun skin(name: String) = SkinDelegate(name, this)
    fun sound(name: String) = SoundDelegate(name, this)
    fun sprite(name: String) = SpriteDelegate(name, this)
    fun pixmap(name: String) = PixmapDelegate(name, this)
    fun textureAtlas(name: String) = TextureAtlasDelegate(name, this)
    fun texture(name: String, extension: String="png") = TextureDelegate(name, extension, this)

    /**
     * Retrieves a list of fields from ''this''.
     */
    private fun fields() = ClassReflection.getDeclaredFields(this.javaClass).asList()

    /**
     * Triggers loading of all assets defined in ''this''.
     */
    fun load() {
        atlasPath?.let { path ->
            manager.load(path, TextureAtlas::class.java)
        }

        for(field in fields()) {
            field.isAccessible = true
            val delegate = field.get(this)

            if(delegate is AssetDelegate<*>) {
                if(delegate.assetName == null) {
                    delegate.assetName = field.name.replace("\$delegate", "")
                }
                delegate.loadAsset()
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
    fun populateAtlas() {
        primaryAtlas = atlasPath.let { path ->
            manager.get(path, TextureAtlas::class.java)
        }
    }

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
