package com.theswitchbot.common.net

import com.alibaba.fastjson.JSONObject
import com.theswitchbot.common.logger.Logger
import com.theswitchbot.common.net.dto.NetResponse
import retrofit2.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import okhttp3.Request
import okio.Timeout

class CatchingCallAdapterFactory(val errorHandler: ErrorHandler? = DefaultErrorHandler()) : CallAdapter.Factory() {

    // 用于配置全局的异常处理逻辑
    interface ErrorHandler {
        fun onBizError(errcode: Int, msg: String)
        fun onOtherError(throwable: Throwable)
    }

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != Call::class.java) return null
        check(returnType is ParameterizedType)

        // 取 Call 里边一层泛型参数
        val innerType: Type = getParameterUpperBound(0, returnType)
        // 如果不是 NetResponse 则不由本 CallAdapter.Factory 处理
        if (getRawType(innerType) != NetResponse::class.java) return null

        // 获取后续代理
        val delegate: CallAdapter<*, *> = retrofit
            .nextCallAdapter(this, returnType, annotations)

        return CatchingCallAdapter(
            innerType,
            retrofit,
            delegate,
            errorHandler
        )
    }

    class CatchingCallAdapter(
        val dataType: Type,
        val retrofit: Retrofit,
        val delegate: CallAdapter<*, *>,
        val errorHandler: ErrorHandler?
    ) : CallAdapter<Any, Call<Any>> {
        override fun responseType(): Type
                = delegate.responseType()
        override fun adapt(call: Call<Any>): Call<Any>
                = CatchingCall(call, dataType as ParameterizedType, errorHandler)
    }

    class CatchingCall(
        private val delegate: Call<Any>,
        private val wrapperType: ParameterizedType,
        private val errorHandler: ErrorHandler?
    ) : Call<Any> {

        override fun enqueue(callback: Callback<Any>): Unit = delegate.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                // 无论请求响应成功还是失败都回调 Response.success
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body is String){
                        try {
                            val resp=JSONObject.parseObject(body,NetResponse::class.java)
                            if (resp.statusCode!=BizStatus.success.code){
                                errorHandler?.onBizError(resp.statusCode,resp.message)
                            }
                        }catch (e:Exception){
                            e.printStackTrace()
                        }
                    }
                    callback.onResponse(this@CatchingCall, Response.success(body))
                } else {
                    val throwable = HttpException(response)
                    errorHandler?.onOtherError(throwable)
                    callback.onResponse(
                        this@CatchingCall,
                        Response.success(NetResponse.otherError(throwable))
                    )
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                errorHandler?.onOtherError(t)
                callback.onResponse(
                    this@CatchingCall,
                    Response.success(NetResponse.otherError(t))
                )
            }
        })

        override fun clone(): Call<Any> =
            CatchingCall(delegate, wrapperType, errorHandler)
        override fun execute(): Response<Any> =
            throw UnsupportedOperationException()
        override fun isExecuted(): Boolean = delegate.isExecuted
        override fun cancel(): Unit = delegate.cancel()
        override fun isCanceled(): Boolean = delegate.isCanceled
        override fun request(): Request = delegate.request()
        override fun timeout(): Timeout = delegate.timeout()
    }
}

class DefaultErrorHandler:CatchingCallAdapterFactory.ErrorHandler{
    private val TAG=DefaultErrorHandler::class.simpleName
    override fun onBizError(errcode: Int, msg: String) {
        Logger.e(TAG,"网络请求发生异常，错误码：${errcode}   msg:${msg}")
    }

    override fun onOtherError(throwable: Throwable) {
        throwable.printStackTrace()
        Logger.e(TAG,"网络请求发生异常:${throwable.message}")
    }

}