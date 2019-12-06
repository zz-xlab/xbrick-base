package cn.zzstc.lzm.common.net

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cn.zzstc.lzm.common.connector.ITokenProvider
import cn.zzstc.lzm.common.data.NetResp
import cn.zzstc.lzm.common.data.model.ServerAddressModel
import cn.zzstc.lzm.common.net.NetworkManager.CLIENT_KEY
import cn.zzstc.lzm.common.net.NetworkManager.TOKEN_KEY
import cn.zzstc.lzm.common.route.ConnectorPath
import com.alibaba.android.arouter.launcher.ARouter
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.Response
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

object NetworkManager {
    const val TOKEN_KEY = "token"
    const val CLIENT_KEY = "ws-client"
    fun init() {
        ServerAddressModel().init()

        FuelManager.instance.basePath = ServerAddressModel().getActiveAddress().url
        FuelManager.instance.addResponseInterceptor(tokenInterceptor())
        FuelManager.instance.addResponseInterceptor(logInterceptor())
        initCommonHeader()
    }

    fun switchBaseUrl(baseUrl: String) {
        FuelManager.instance.basePath = baseUrl
    }

    inline fun <reified T : Any> get(
        url: String,
        params: Map<String, String>
    ): LiveData<NetResp<T>> {
        return doRequest(url.httpGet(parameters = params.toList()))
    }

    inline fun <reified T : Any> put(url: String, body: Any): LiveData<NetResp<T>> {
        return doRequest(url.httpPut().jsonBody(body))
    }

    inline fun <reified T : Any> post(url: String, body: Any): LiveData<NetResp<T>> {
        return doRequest(url.httpPost().jsonBody(body))
    }

    inline fun <reified T : Any> delete(url: String, body: Any): LiveData<NetResp<T>> {
        return doRequest(url.httpDelete().jsonBody(body))
    }

    inline fun <reified T : Any> doRequest(request: Request): LiveData<NetResp<T>> {
        var result: NetResp<T> by Delegates.notNull()
        var resp = MutableLiveData<NetResp<T>>()
        GlobalScope.launch(Dispatchers.Main) {
            request.executionOptions.responseValidator = {
                true
            }
            withContext(Dispatchers.IO) {
                var respObj = request.responseObject<NetResp<T>>()
                if (respObj.third.component2() == null) {
                    val respData = respObj.third.get()
                    respData.httpCode = respObj.second.statusCode
                    result = respData
                } else {
                    respObj.third.component2()!!.printStackTrace()
                    result = NetResp("未知错误", -1, null, -1)
                }
            }
            resp.value = result

        }
        return resp
    }

    inline fun <reified T : Any> postSync(url: String, body: Any): NetResp<T> {
        return doRequestSync(url.httpPost().jsonBody(body))
    }

    inline fun <reified T : Any> doRequestSync(request: Request): NetResp<T> {
        var result: NetResp<T> by Delegates.notNull()
        GlobalScope.launch(Dispatchers.Main) {
            request.executionOptions.responseValidator = {
                true
            }
            val respObj = request.responseObject<NetResp<T>>()
            if (respObj.third.component2() == null) {
                val respData = respObj.third.get()
                respData.httpCode = respObj.second.statusCode
                result = respData
            } else {
                respObj.third.component2()!!.printStackTrace()
                result = NetResp("未知错误", -1, null, -1)
            }
        }
        return result
    }

}


private fun initCommonHeader() {
    val tokenProvider: ITokenProvider? =
        ARouter.getInstance().build(ConnectorPath.TOKEN_SERVICE).navigation() as ITokenProvider?
    if (null != tokenProvider) {
        FuelManager.instance.baseHeaders =
            mutableMapOf(
                TOKEN_KEY to tokenProvider.getToken(),
                CLIENT_KEY to tokenProvider.clientType()
            )
    }
}


fun tokenInterceptor() = { next: (Request, Response) -> Response ->
    { req: Request, resp: Response ->
        val tokenProvider: ITokenProvider? =
            ARouter.getInstance().build(ConnectorPath.TOKEN_SERVICE).navigation() as ITokenProvider?
        if (null != tokenProvider) {
            for (url in tokenProvider.ignoreUrls()) {
                if (req.url.path.contains(url)) {
                    next(req, resp)
                }
            }
            if (resp.statusCode == 401) {
                initCommonHeader()
            }
        }
        next(req, resp)
    }
}

fun logInterceptor() = { next: (Request, Response) -> Response ->
    { req: Request, resp: Response ->
        Logger.d("$req")
        Logger.d("$resp")
        next(req, resp)
    }
}


object NetConst {
    const val BASE_V1 = "business/v1/"
    const val BASE_V2 = "business/v2/"
    const val BASE_V3 = "business/v3/"
    const val BASE_EC_V1 = "ec/v1/"
    const val BASE_EC_V2 = "ec/v2/"
    const val BASE_EC_V3 = "ec/v3/"
}