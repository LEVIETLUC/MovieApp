package com.example.movieapp.network


import com.example.movieapp.util.Constants.TMDB_API_KEY
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val url = original.url.newBuilder()
            .addQueryParameter("api_key", TMDB_API_KEY)
            .build()
        val req = original.newBuilder().url(url).build()
        return chain.proceed(req)
    }
}