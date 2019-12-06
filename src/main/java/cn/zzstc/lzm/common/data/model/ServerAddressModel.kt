package cn.zzstc.lzm.common.data.model

import cn.zzstc.lzm.common.connector.IServerAddressProvider
import cn.zzstc.lzm.common.data.SpAccessor
import cn.zzstc.lzm.common.data.dao.CommonDb
import cn.zzstc.lzm.common.data.entity.AddressType
import cn.zzstc.lzm.common.data.entity.ServerAddress
import cn.zzstc.lzm.common.net.NetworkManager
import cn.zzstc.lzm.common.route.ConnectorPath
import com.alibaba.android.arouter.launcher.ARouter

class ServerAddressModel {
    fun init() {
        if (!SpAccessor.getBool(SpAccessor.SERVER_ADDRESS_INITED, false)) {
            SpAccessor.put(SpAccessor.SERVER_ADDRESS_INITED, true)
            val addressProvider =
                ARouter.getInstance().build(ConnectorPath.ADDRESS_SERVER).navigation() as IServerAddressProvider?
            if (addressProvider == null) {
                CommonDb.get().getServerAddressDao().insert(
                    ServerAddress(
                        "http://test.louzhangmen.cn:8000/",
                        AddressType.Debug,
                        true
                    )
                )
            } else {
                CommonDb.get().getServerAddressDao().insertAll(addressProvider.addresses())
            }
        }
    }

    fun getActiveAddressIndex(): Int {
        var index = 0
        for (serverAddress in getAll()) {
            if (serverAddress.active) {
                return index
            }
            index++
        }
        return index
    }

    fun getActiveAddress(): ServerAddress {
        for (serverAddress in getAll()) {
            if (serverAddress.active) {
                return serverAddress
            }
        }
        return ServerAddress("", AddressType.Other, true)
    }

    fun insert(address: ServerAddress) = CommonDb.get().getServerAddressDao().insert(address)

    fun check(): Boolean = CommonDb.get().getServerAddressDao().getActive(true).size == 1

    fun getAll(): List<ServerAddress> = CommonDb.get().getServerAddressDao().getAll()

    fun deleteById(address:ServerAddress)=CommonDb.get().getServerAddressDao().deleteById(address.url)

    fun switchActiveAddress(activeIndex: Int) {
        var allAddresses = getAll()
        for (i in allAddresses.indices) {
            allAddresses[i].active = i == activeIndex
            CommonDb.get().getServerAddressDao().update(allAddresses[i])
            if (allAddresses[i].active) {
                NetworkManager.switchBaseUrl(allAddresses[i].url)
            }
        }
    }
}