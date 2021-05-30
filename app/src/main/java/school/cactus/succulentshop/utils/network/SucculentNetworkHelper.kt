package school.cactus.succulentshop.utils.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import school.cactus.succulentshop.BuildConfig
import school.cactus.succulentshop.R
import school.cactus.succulentshop.utils.data.SharedPrefHelper
import school.cactus.succulentshop.utils.handleResponse
import java.io.IOException
import java.util.concurrent.TimeUnit

object SucculentNetworkHelper {
    const val BASE_URL: String = "https://apps.cactus.school"
    private lateinit var retrofit: Retrofit

    fun buildRetrofit() {
        val authorizedRequestInterceptor = AuthorizedRequestInterceptor()

        val interceptor = HttpLoggingInterceptor().apply {
            setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(authorizedRequestInterceptor)
            .addInterceptor(interceptor)
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    fun <Any> createService(clazz: Class<Any>): Any = retrofit.create(clazz)

    private class AuthorizedRequestInterceptor : Interceptor {

        override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
            val requestBuilder = chain.request().newBuilder()
            if (chain.request().url.encodedPathSegments[0] != "auth") {
                requestBuilder.addHeader(
                    "Authorization",
                    "Bearer ${SharedPrefHelper.getJwt()}"
                )
            }
            return chain.proceed(requestBuilder.build())
        }
    }
}

