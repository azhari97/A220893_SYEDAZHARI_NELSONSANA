package com.example.a220893_nelson_lab2.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//    private val logging = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
//        override fun log(message: String) {
//            val trimmed = message.trim()
//
//            // Check if the message is a JSON Object or Array
//            if (trimmed.startsWith("{") && trimmed.endsWith("}")) {
//                runCatching { println(JSONObject(trimmed).toString(4)) }
//                    .onFailure { println(message) }
//            } else if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
//                runCatching { println(JSONArray(trimmed).toString(4)) }
//                    .onFailure { println(message) }
//            } else {
//                // Log non-JSON lines (headers, URLs, status codes) normally
//                println(message)
//            }
//        }
//    }).apply {
//        level = HttpLoggingInterceptor.Level.BODY
//    }
object RetrofitClient {

    private val logging = HttpLoggingInterceptor().apply {
        // LEVEL OPTIONS:
        // .Level.BASIC -> Logs request method, URL, and response code
        // .Level.HEADERS -> Logs headers + basic
        // .Level.BODY -> Logs everything (URL, Headers, and the full JSON Body)
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private const val BASE_URL = "https://api.currentsapi.services/"
    val newsApiService: NewsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApiService::class.java)
    }
}