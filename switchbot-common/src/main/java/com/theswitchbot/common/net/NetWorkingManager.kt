package com.theswitchbot.common.net

import com.theswitchbot.common.util.gson
import me.jessyan.retrofiturlmanager.RetrofitUrlManager
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Collections

/**
 * 使用网络请求前务必在token业务模块中对token拦截器进行实现
 * 并调用init方法对网络模块进行初始化，此方法需尽早进行初始化
 */
object NetWorkingManager {
    private val serviceCache =Collections.synchronizedMap(HashMap<String,Any?>())
    private lateinit var retrofit:Retrofit

    //无拦截器的httpClient 用于请求重试
    lateinit var noInterceptorClient: OkHttpClient


    fun init(globalUrl:String,tokenInterceptor:Interceptor){
//        val loggingInterceptor = HttpLoggingInterceptor {
//                message -> Logger.d("OkHttp", message)
//        }
//        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okhttpClient = RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
//            .addInterceptor(loggingInterceptor)
            .addInterceptor(StaticHeaderInterceptor())
            .addInterceptor(tokenInterceptor)
            .build()
        RetrofitUrlManager.getInstance().setGlobalDomain(globalUrl)
        retrofit = Retrofit.Builder().client(okhttpClient).baseUrl(globalUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(CatchingCallAdapterFactory())
            .build()

        noInterceptorClient = RetrofitUrlManager.getInstance().with(OkHttpClient.Builder())
//            .addInterceptor(loggingInterceptor)
            .addInterceptor(StaticHeaderInterceptor())
            .build()
    }

    /**
     * 创建service实例并缓存
     */
    @Synchronized
    fun <T> create(service:Class<T>):T{
        return if (serviceCache[service.name]==null){
            val instance=retrofit.create(service)
            serviceCache[service.name]=instance
            instance
        }else{
            serviceCache[service.name] as T
        }
    }
}

class StaticHeaderInterceptor:Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val originalResponse = chain.proceed(request)
        return originalResponse.newBuilder()
            .header("Content-Type", "application/json")
            .build()
    }
}