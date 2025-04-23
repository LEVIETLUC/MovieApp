package com.example.movieapp.network

import com.example.movieapp.network.AuthInterceptor
import com.example.movieapp.util.Constants.TMDB_API_KEY
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class AuthInterceptorTest {

    @Test
    fun `interceptor adds api_key query parameter`() {
        // Arrange
        val originalUrl = HttpUrl.Builder()
            .scheme("https")
            .host("api.themoviedb.org")
            .addPathSegment("3")
            .addPathSegment("movie")
            .addPathSegment("550")
            .build()

        val originalRequest = Request.Builder()
            .url(originalUrl)
            .build()

        val chain = mock<Interceptor.Chain>()
        whenever(chain.request()).thenReturn(originalRequest)

        val response = mock<Response>()
        whenever(chain.proceed(any())).thenReturn(response)

        val interceptor = AuthInterceptor()

        // Act
        interceptor.intercept(chain)

        // Assert
        val argumentCaptor = argumentCaptor<Request>()
        verify(chain).proceed(argumentCaptor.capture())
        val modifiedRequest = argumentCaptor.firstValue
        val modifiedUrl = modifiedRequest.url

        assertEquals(TMDB_API_KEY, modifiedUrl.queryParameter("api_key"))
    }
}
