package com.ly.wanandroid.utils

import android.content.Context
import android.os.Parcelable
import com.tencent.mmkv.MMKV
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * SpUtils代理，不支持Set<String>类型
 * 用法：
 *      var author by preference(default = "ly", key = "author", saveId = "test")
 * @param defaultValue 默认参必填
 * @param key 保存时用的key,不传时，就默认使用代理参数的名称,选填
 * @param saveId 如果有值就与默认的区别存储，取值之前一定要先确认是否有必要填saveId，选填
 */
fun <T> preference(defaultValue: T, key: String? = null, saveId: String? = null): SpDelegate<T> =
    SpDelegate(defaultValue, saveId, key)

object SpUtils {

    fun init(context: Context) {
        MMKV.initialize(context)
    }

    fun init(context: Context, rootDir: String) {
        MMKV.initialize(context, rootDir)
    }

    fun create(name: String?): MMKV =
        if (name.isNullOrEmpty()) MMKV.defaultMMKV() else MMKV.mmkvWithID(name)


    fun put(key: String, value: Int, saveId: String? = null): Boolean =
        create(saveId).encode(key, value)

    fun put(key: String, value: Long, saveId: String? = null): Boolean =
        create(saveId).encode(key, value)

    fun put(key: String, value: String, saveId: String? = null): Boolean =
        create(saveId).encode(key, value)

    fun put(key: String, value: Boolean, saveId: String? = null): Boolean =
        create(saveId).encode(key, value)

    fun put(key: String, value: Float, saveId: String? = null): Boolean =
        create(saveId).encode(key, value)

    fun put(key: String, value: Double, saveId: String? = null): Boolean =
        create(saveId).encode(key, value)

    fun put(key: String, value: ByteArray, saveId: String? = null): Boolean =
        create(saveId).encode(key, value)

    fun put(key: String, value: Parcelable, saveId: String? = null): Boolean =
        create(saveId).encode(key, value)

    fun put(key: String, value: Set<String>, saveId: String? = null): Boolean =
        create(saveId).encode(key, value)

    fun get(key: String, defaultValue: Int, saveId: String? = null): Int =
        create(saveId).decodeInt(key, defaultValue)

    fun get(key: String, defaultValue: Long, saveId: String? = null): Long =
        create(saveId).decodeLong(key, defaultValue)

    fun get(key: String, defaultValue: String = "", saveId: String? = null): String =
        create(saveId).decodeString(key, defaultValue) ?: defaultValue

    fun get(key: String, defaultValue: Boolean, saveId: String? = null): Boolean =
        create(saveId).decodeBool(key, defaultValue)


    fun get(key: String, defaultValue: Float, saveId: String? = null): Float =
        create(saveId).decodeFloat(key, defaultValue)

    fun get(key: String, defaultValue: Double, saveId: String? = null): Double =
        create(saveId).decodeDouble(key, defaultValue)

    fun get(
        key: String,
        defaultValue: ByteArray = ByteArray(0),
        saveId: String? = null
    ): ByteArray =
        create(saveId).decodeBytes(key, defaultValue) ?: defaultValue

    inline fun <reified T : Parcelable> get(key: String, saveId: String? = null): Parcelable? =
        create(saveId).decodeParcelable(key, T::class.java)

    inline fun <reified T : Parcelable> get(
        key: String,
        defaultValue: T,
        saveId: String? = null
    ): Parcelable =
        create(saveId).decodeParcelable(key, T::class.java, defaultValue) ?: defaultValue

    fun get(
        key: String,
        defaultValue: Set<String> = emptySet(),
        saveId: String? = null
    ): Set<String> =
        create(saveId).decodeStringSet(key, defaultValue) ?: defaultValue

    fun contains(key: String, saveId: String? = null): Boolean = create(saveId).contains(key)

    fun remove(key: String, saveId: String?) = create(saveId).removeValueForKey(key)

    fun fileSize(id: String, saveId: String? = null): Long = create(saveId).totalSize()
}

class SpDelegate<T>(
    private val default: T,
    private val saveId: String? = null,
    private val key: String? = null
) :
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
        is String -> SpUtils.get(key, default, saveId)
        is Long -> SpUtils.get(key, default, saveId)
        is Int -> SpUtils.get(key, default, saveId)
        is Float -> SpUtils.get(key, default, saveId)
        is Double -> SpUtils.get(key, default, saveId)
        is Boolean -> SpUtils.get(key, default, saveId)
        is ByteArray -> SpUtils.get(key, default, saveId)
        is Parcelable -> SpUtils.get(key, default, saveId)
        else -> throw  IllegalArgumentException("Unsupported type")
    } as T

    private fun putPreference(key: String, value: T) {
        when (value) {
            is String -> SpUtils.put(key, value, saveId)
            is Long -> SpUtils.put(key, value, saveId)
            is Int -> SpUtils.put(key, value, saveId)
            is Float -> SpUtils.put(key, value, saveId)
            is Double -> SpUtils.put(key, value, saveId)
            is Boolean -> SpUtils.put(key, value, saveId)
            is ByteArray -> SpUtils.put(key, value, saveId)
            is Parcelable -> SpUtils.put(key, value, saveId)
            else -> throw  IllegalArgumentException("Unsupported type")
        }
    }


    private fun findProperName(property: KProperty<*>) =
        if (key.isNullOrEmpty()) property.name else key


}




