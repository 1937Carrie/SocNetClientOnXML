package sdumchykov.androidApp.data.storage

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import sdumchykov.androidApp.domain.storage.Storage
import sdumchykov.androidApp.domain.utils.Constants.SHARED_PREFERENCES_KEY
import javax.inject.Inject

class SharedPreferencesStorage @Inject constructor(@ApplicationContext private val context: Context) :
    Storage {

    override fun save(_key: String, _value: String) {
        val prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()

        prefsEdit.putString(_key, _value)
        prefsEdit.apply()
    }


    override fun save(_key: String, _value: Int) {
        val prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()
        prefsEdit.putInt(_key, _value)
        prefsEdit.apply()
    }


    override fun save(_key: String, _value: Boolean) {
        val prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()

        prefsEdit.putBoolean(_key, _value)
        prefsEdit.apply()
    }


    override fun save(_key: String, _value: Long) {
        val prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()

        prefsEdit.putLong(_key, _value)
        prefsEdit.apply()
    }


    override fun save(_key: String, _value: Double) {
        val prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()

        prefsEdit.putFloat(_key, _value.toFloat())
        prefsEdit.apply()
    }


    override fun save(_key: String, _value: Float) {
        val prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()

        prefsEdit.putFloat(_key, _value)
        prefsEdit.apply()
    }


    override fun getString(_key: String, default: String): String? {
        val prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        return prefs.getString(_key, default)
    }


    override fun getFloat(_key: String): Float {
        val prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        return prefs.getFloat(_key, 0f)
    }


    override fun getBoolean(_key: String): Boolean {
        val prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        return prefs.getBoolean(_key, false)
    }


    override fun getBoolean(_key: String, _def: Boolean): Boolean {
        val prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        return prefs.getBoolean(_key, _def)
    }


    override fun getInt(_key: String): Int {
        val prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        return prefs.getInt(_key, 0)
    }


    override fun getInt(_key: String, default: Int): Int {
        val prefs = context.getSharedPreferences(
            SHARED_PREFERENCES_KEY,
            Context.MODE_PRIVATE
        )
        return prefs.getInt(_key, default)
    }

    override fun getLong(_key: String, default: Long): Long {
        val prefs = context.getSharedPreferences(
            SHARED_PREFERENCES_KEY,
            Context.MODE_PRIVATE
        )
        return prefs.getLong(_key, default)
    }

    override fun getLong(_key: String): Long {
        val prefs = context.getSharedPreferences(
            SHARED_PREFERENCES_KEY,
            Context.MODE_PRIVATE
        )
        return prefs.getLong(_key, 0L)
    }

    override fun removePrefValue(key: String) {
        val prefs = context.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()
        prefsEdit.remove(key)
        prefsEdit.apply()
    }

    override fun contains(_key: String): Boolean {
        val prefs = context.getSharedPreferences(
            SHARED_PREFERENCES_KEY,
            Context.MODE_PRIVATE
        )
        return prefs.contains(_key)
    }

    override fun clearPrefs() {
        val prefs =
            context.getSharedPreferences(
                SHARED_PREFERENCES_KEY,
                Context.MODE_PRIVATE
            )
        prefs.edit().clear().apply()
    }
}