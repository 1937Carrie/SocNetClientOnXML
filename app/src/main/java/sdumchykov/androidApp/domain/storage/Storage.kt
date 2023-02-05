package sdumchykov.androidApp.domain.storage

interface Storage {
    fun save(_key: String, _value: String)
    fun save(_key: String, _value: Int)
    fun save(_key: String, _value: Boolean)
    fun save(_key: String, _value: Long)
    fun save(_key: String, _value: Double)
    fun save(_key: String, _value: Float)
    fun getString(_key: String, default: String = ""): String?
    fun getFloat(_key: String): Float
    fun getBoolean(_key: String): Boolean
    fun getBoolean(_key: String, _def: Boolean): Boolean
    fun getInt(_key: String): Int
    fun getInt(_key: String, default: Int): Int
    fun getLong(_key: String, default: Long): Long
    fun getLong(_key: String): Long
    fun removePrefValue(key: String)
    fun contains(_key: String): Boolean
    fun clearPrefs()
}