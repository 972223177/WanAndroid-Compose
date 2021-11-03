package com.ly.chatcompose.utils

import android.content.Context
import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * SpUtils代理，不支持Set<String>类型
 * 用法：
 *      var author by preference("linyu")
 * @param defaultValue 默认参必填
 * @param key 保存时用的key,不传时，就默认使用代理参数的名称
 */
fun <T> preference(defaultValue: T, key: String? = null): SpDelegate<T> =
    SpDelegate(defaultValue, key)

object SpUtils {


    val mmkvInstance: MMKV by lazy {
        MMKV.defaultMMKV()
    }

    fun init(context: Context) {
        MMKV.initialize(context)
    }

    fun init(context: Context, rootDir: String) {
        MMKV.initialize(context, rootDir)
    }

    fun create(name: String) {
        MMKV.mmkvWithID(name)
    }


    fun put(key: String, value: Int): Boolean = mmkvInstance.encode(key, value)

    fun put(key: String, value: Long): Boolean = mmkvInstance.encode(key, value)

    fun put(key: String, value: String): Boolean = mmkvInstance.encode(key, value)

    fun put(key: String, value: Boolean): Boolean = mmkvInstance.encode(key, value)

    fun put(key: String, value: Float): Boolean = mmkvInstance.encode(key, value)

    fun put(key: String, value: Double): Boolean = mmkvInstance.encode(key, value)

    fun put(key: String, value: ByteArray): Boolean = mmkvInstance.encode(key, value)

    fun put(key: String, value: Parcelable): Boolean =
        mmkvInstance.encode(key, value)

    fun put(key: String, value: Set<String>): Boolean =
        mmkvInstance.encode(key, value)

    fun get(key: String, defaultValue: Int): Int =
        mmkvInstance.decodeInt(key, defaultValue)

    fun get(key: String, defaultValue: Long): Long =
        mmkvInstance.decodeLong(key, defaultValue)

    fun get(key: String, defaultValue: String = ""): String =
        mmkvInstance.decodeString(key, defaultValue) ?: defaultValue

    fun get(key: String, defaultValue: Boolean): Boolean =
        mmkvInstance.decodeBool(key, defaultValue)


    fun get(key: String, defaultValue: Float): Float =
        mmkvInstance.decodeFloat(key, defaultValue)

    fun get(key: String, defaultValue: Double): Double =
        mmkvInstance.decodeDouble(key, defaultValue)

    fun get(key: String, defaultValue: ByteArray = ByteArray(0)): ByteArray =
        mmkvInstance.decodeBytes(key, defaultValue) ?: defaultValue

    inline fun <reified T : Parcelable> get(key: String): Parcelable? =
        mmkvInstance.decodeParcelable(key, T::class.java)

    inline fun <reified T : Parcelable> get(key: String, defaultValue: T): Parcelable =
        mmkvInstance.decodeParcelable(key, T::class.java, defaultValue) ?: defaultValue

    fun get(key: String, defaultValue: Set<String> = emptySet()): Set<String> =
        mmkvInstance.decodeStringSet(key, defaultValue) ?: defaultValue

    fun contains(key: String): Boolean = mmkvInstance.contains(key)

    fun remove(key: String) = mmkvInstance.removeValueForKey(key)

    fun fileSize(id: String): Long = mmkvInstance.totalSize()
}

interface ISpUtils {

}

class SpDelegate<T>(private val default: T, private val key: String? = null) :
    ReadWriteProperty<Any?, T> {

    private var value: T = default

    override operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(findProperName(property))
    }

    override operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(findProperName(property), value)
    }

    @Suppress("IMPLICIT_CAST_TO_ANY", "UNCHECKED_CAST")
    private fun findPreference(key: String): T = when (default) {
        is String -> SpUtils.get(key, default)
        is Long -> SpUtils.get(key, default)
        is Int -> SpUtils.get(key, default)
        is Float -> SpUtils.get(key, default)
        is Double -> SpUtils.get(key, default)
        is Boolean -> SpUtils.get(key, default)
        is ByteArray -> SpUtils.get(key, default)
        is Parcelable -> SpUtils.get(key, default)
        else -> throw  IllegalArgumentException("Unsupported type")
    } as T

    private fun putPreference(key: String, value: T) {
        when (value) {
            is String -> SpUtils.put(key, value)
            is Long -> SpUtils.put(key, value)
            is Int -> SpUtils.put(key, value)
            is Float -> SpUtils.put(key, value)
            is Double -> SpUtils.put(key, value)
            is Boolean -> SpUtils.put(key, value)
            is ByteArray -> SpUtils.put(key, value)
            is Parcelable -> SpUtils.put(key, value)
            else -> throw  IllegalArgumentException("Unsupported type")
        }
    }


    private fun findProperName(property: KProperty<*>) =
        if (key.isNullOrEmpty()) property.name else key


}




