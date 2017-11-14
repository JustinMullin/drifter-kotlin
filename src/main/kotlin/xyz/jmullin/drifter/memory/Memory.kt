package xyz.jmullin.drifter.memory

import com.badlogic.gdx.Gdx
import xyz.jmullin.drifter.extensions.drifter
import xyz.jmullin.drifter.memory.Memory.prefs

object Memory {
    val prefs by lazy { Gdx.app.getPreferences(drifter().name)!! }
}

fun getString(key: String, default: String? = null): String {
    return if(default == null) prefs.getString(key) else prefs.getString(key, default)
}

fun getInt(key: String, default: Int? = null): Int {
    return if(default == null) prefs.getInteger(key) else prefs.getInteger(key, default)
}

fun getFloat(key: String, default: Float? = null): Float {
    return if(default == null) prefs.getFloat(key) else prefs.getFloat(key, default)
}

fun getLong(key: String, default: Long? = null): Long {
    return if(default == null) prefs.getLong(key) else prefs.getLong(key, default)
}

fun getBoolean(key: String, default: Boolean? = null): Boolean {
    return if(default == null) prefs.getBoolean(key) else prefs.getBoolean(key, default)
}

fun forget(key: String) {
    prefs.remove(key)
    prefs.flush()
}

fun clearMemory() {
    prefs.clear()
    prefs.flush()
}

fun putString(key: String, value: String) {
    prefs.putString(key, value)
    prefs.flush()
}

fun putInt(key: String, value: Int) {
    prefs.putInteger(key, value)
    prefs.flush()
}

fun putFloat(key: String, value: Float) {
    prefs.putFloat(key, value)
    prefs.flush()
}

fun putLong(key: String, value: Long) {
    prefs.putLong(key, value)
    prefs.flush()
}

fun putBoolean(key: String, value: Boolean) {
    prefs.putBoolean(key, value)
    prefs.flush()
}