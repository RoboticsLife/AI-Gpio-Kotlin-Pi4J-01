package network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import runtime.setup.Settings
import java.util.concurrent.TimeUnit

object InternetConnection {

    private val okHttpClient: OkHttpClient? = null

    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient()
            .newBuilder()
            .readTimeout(Settings.NETWORK_OKHTTP_DEFAULT_READ_TIMEOUT_SEC, TimeUnit.SECONDS)
            .connectTimeout(Settings.NETWORK_OKHTTP_DEFAULT_CONNECT_TIMEOUT_SEC, TimeUnit.SECONDS)
            .writeTimeout(Settings.NETWORK_OKHTTP_DEFAULT_WRITE_TIMEOUT_SEC, TimeUnit.SECONDS)
            //add interceptors if need
            .build()
    }

    fun getWeatherClient(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient ?: getOkHttpClient())
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    fun getAINetworkClient(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient ?: getOkHttpClient())
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }
 }