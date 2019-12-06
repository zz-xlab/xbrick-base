package cn.zzstc.lzm.common.ui.vm

import android.content.pm.PackageManager
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import cn.zzstc.lzm.common.BR
import cn.zzstc.lzm.common.BaseApp
import cn.zzstc.lzm.common.R
import cn.zzstc.lzm.common.adapter.ListAdapter
import cn.zzstc.lzm.common.const.Const
import cn.zzstc.lzm.common.data.bean.ModuleInfo
import cn.zzstc.lzm.common.data.entity.ServerAddress
import cn.zzstc.lzm.common.data.model.ServerAddressModel

class LabVm : ViewModel() {
    var serverAddressList = ObservableArrayList<ServerAddress>()
    var clientId = ObservableField<String>()
    var adapter:ListAdapter<ModuleInfo>?=null
    private val serverAddressModel= ServerAddressModel()
    private var modules=ObservableArrayList<ModuleInfo>()


    init {
        clientId.set("暂无")
        serverAddressList.addAll(serverAddressModel.getAll())
        val appInfo = BaseApp.instance().packageManager.getApplicationInfo(BaseApp.instance().packageName, PackageManager.GET_META_DATA)
        if (appInfo.metaData != null) {
            for (key in appInfo.metaData.keySet()) {
                if (BaseApp.instance().resources.getString(R.string.module_info) == appInfo.metaData[key]) {
                    modules.add(readModuleInfo(key))
                }
            }
        }
        adapter= ListAdapter(BaseApp.instance(),R.layout.item_module_info,BR.moduleInfo,modules)
        adapter!!.notifyDataSetChanged()
    }

    private fun readModuleInfo(key:String):ModuleInfo{
        val info=ModuleInfo()
        val clazz: Class<*> = Class.forName(key)
        for(field in clazz.declaredFields){
            when (field.name) {
                Const.MODULE_NAME->{
                    info.moduleName= field.get(clazz) as String
                }
                Const.IS_DEBUG->{
                    info.debug= field.get(clazz) as Boolean
                }
                Const.VERSION_NAME->{
                    info.verName= field.get(clazz) as String
                }
                Const.BUILD_TYPE->{
                    info.buildType= field.get(clazz) as String
                }
                Const.BUILD_HASH->{
                    info.buildHash= field.get(clazz) as String
                }
            }
        }
        return info
    }

    fun getAddressUrls():Array<String>{
        return Array(serverAddressList.size,init = {
            serverAddressList[it].url
        })
    }
    fun activeAddressIndex():Int=serverAddressModel.getActiveAddressIndex()

    fun switchActiveAddress(activeIndex:Int){
        serverAddressModel.switchActiveAddress(activeIndex)
        serverAddressList.clear()
        serverAddressList.addAll(serverAddressModel.getAll())
    }
}