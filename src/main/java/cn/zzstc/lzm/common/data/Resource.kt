package cn.zzstc.lzm.common.data

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData

data class NetResp<T : Any>(val message: String, val code: Int, val `data`: T?,var httpCode:Int){
    fun success()=httpCode/100!=4&&httpCode/100!=5
}

data class Resource<out T>(val state: ResourceState, val data: T?, val message: String? = "",val code: Int=-1) {
    companion object {
        fun <T> success(data: T?, msg: String): Resource<T> {
            return Resource(ResourceState.Success, data, msg,0)
        }

        fun <T> error(msg: String, data: T?,code: Int): Resource<T> {
            return Resource(ResourceState.Failure, data, msg,code)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(ResourceState.Loading, data)
        }
    }

}

enum class ResourceState {
    Loading, Failure, Success
}

abstract class NetworkBoundResource<ResultType : Any, RequestType : Any>
@MainThread constructor() {

    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null)
        val dbSource = loadFromCache()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource)
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    setValue(Resource.success(newData,"success"))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val netResp = loadFromNet()
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        result.addSource(netResp) { response ->
            result.removeSource(netResp)
            result.removeSource(dbSource)
            if (response.success()) {
                saveNetResult(processResponse(response))
                result.addSource(loadFromCache()) { newData ->
                    setValue(Resource.success(newData,response.message))
                }
            } else {
                onFetchFailed()
                result.addSource(dbSource) { newData ->
                    setValue(Resource.error(response.message, newData,response.code))
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: NetResp<ResultType>) =response.data!!

    @WorkerThread
    protected abstract fun saveNetResult(resp: ResultType)

    @MainThread
    protected fun shouldFetch(data: ResultType?): Boolean=true

    @MainThread
    protected abstract fun loadFromCache(): LiveData<ResultType>

    @MainThread
    protected abstract fun loadFromNet(): LiveData<NetResp<ResultType>>
}