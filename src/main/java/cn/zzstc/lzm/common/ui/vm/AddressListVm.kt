package cn.zzstc.lzm.common.ui.vm

import androidx.databinding.ObservableArrayList
import androidx.lifecycle.ViewModel
import cn.zzstc.lzm.common.BR
import cn.zzstc.lzm.common.BaseApp
import cn.zzstc.lzm.common.R
import cn.zzstc.lzm.common.adapter.ListAdapter
import cn.zzstc.lzm.common.data.entity.AddressType
import cn.zzstc.lzm.common.data.entity.ServerAddress
import cn.zzstc.lzm.common.data.model.ServerAddressModel

class AddressListVm :ViewModel(){


    private val serverAddressModel= ServerAddressModel()
    var adapter:ListAdapter<ServerAddress>?=null

    var serverAddressList = ObservableArrayList<ServerAddress>()

    init {
        serverAddressList.addAll(serverAddressModel.getAll())
        adapter= ListAdapter(BaseApp.instance(), R.layout.item_server_address,BR.address,serverAddressList)
    }

    fun save(newAddress: CharSequence) {
        serverAddressModel.insert(ServerAddress(newAddress.toString(),AddressType.Other,false))
        serverAddressList.clear()
        serverAddressList.addAll(serverAddressModel.getAll())
        adapter!!.notifyDataSetChanged()
    }

    fun removeAddress(index:Int){
        serverAddressModel.deleteById(serverAddressList[index])
        serverAddressList.removeAt(index)
        adapter!!.notifyDataSetChanged()
    }

}