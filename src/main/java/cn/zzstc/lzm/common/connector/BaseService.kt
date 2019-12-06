package cn.zzstc.lzm.common.connector

import cn.zzstc.lzm.common.data.entity.ServerAddress
import com.alibaba.android.arouter.facade.template.IProvider

interface ITokenProvider : IProvider {
    fun getToken(): String
    fun refreshToken(): String
    fun ignoreUrls(): Array<String>
    fun clientType():String
}


interface IServerAddressProvider: IProvider {
    fun addresses():List<ServerAddress>
}