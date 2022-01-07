package com.ly.wanandroid.config.http

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

val globalJson = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

class JsonConverterFactory : Converter.Factory() {
    private val mediaType = "application/json; charset=UTF-8".toMediaType()

//    private val utf8 = Charset.forName("UTF-8")

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        return Converter<ResponseBody, Any> {
            it.use { json ->
                globalJson.decodeFromString(Json.serializersModule.serializer(type), json.string())
            }
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody> {
        return Converter<Any, RequestBody> {
            globalJson.encodeToString(Json.serializersModule.serializer(type), it)
                .toRequestBody(mediaType)
        }
    }

    override fun stringConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<*, String> {
        return Converter<Any, String> {
            globalJson.encodeToString(Json.serializersModule.serializer(type), it)
        }
    }
}