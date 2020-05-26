package com.esimtek.esim.network

import com.esimtek.esim.EsimApplication
import com.esimtek.esim.util.EncryptionUtil
import com.esimtek.esim.util.PreferenceUtil
import okhttp3.OkHttpClient
import okhttp3.Request
import okio.Buffer
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.TimeUnit


class HttpClient {

    companion object {
        const val staffId = "8888"
    }

    private var url by PreferenceUtil("url", "192.168.3.21:59790")
    private var baseUrl = "http://".plus(url).plus("/")
    private val connectTimeOut = 15L
    private val readTimeOut = 15L
    private val writeTimeOut = 15L

    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().client(provideOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build()
    }

    private fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                .readTimeout(readTimeOut, TimeUnit.SECONDS)
                .writeTimeout(writeTimeOut, TimeUnit.SECONDS)
                .authenticator { _, response ->
                    EsimApplication.instance.token = HttpClient().provideRetrofit().create(Api::class.java).getToken(staffId).execute().body()?.data?.signToken
                    return@authenticator addRequestHeader(response.request())
                }.addInterceptor { chain ->
                    chain.proceed(addRequestHeader(chain.request()))
                }.build()
    }

    private fun addRequestHeader(request: Request): Request {
        val url = request.url()
        val timestamp = Calendar.getInstance().timeInMillis.toString()
        val unEncryptBuffer = StringBuffer()
        unEncryptBuffer.append(staffId)
        unEncryptBuffer.append(timestamp)
        unEncryptBuffer.append(EsimApplication.instance.token)
        if (request.method() == "GET") {
            url.queryParameterNames().sorted().forEach {
                unEncryptBuffer.append(it)
                url.queryParameterValues(it).forEach { unEncryptBuffer.append(it) }
            }
        }
        if (request.method() == "POST") {
            val postBuffer = Buffer()
            request.body()?.contentType()?.charset(Charset.forName("UTF-8"))
            request.body()?.writeTo(postBuffer)
            unEncryptBuffer.append(postBuffer.readString(Charset.forName("UTF-8")))
        }
        val signatureBuffer = StringBuffer()
        unEncryptBuffer.toList().sorted().forEach { signatureBuffer.append(it) }
        return request.newBuilder()
                .header("Content-Type", "application/json")
                .header("staffId", staffId)
                .header("onlyKey", EsimApplication.instance.uuid)
                .header("userId", EsimApplication.instance.logged?.data?.userId.toString())
                .header("timestamp", timestamp)
                .header("signature", EncryptionUtil.encrypt(signatureBuffer.toString(), "MD5"))
                .build()
    }
}