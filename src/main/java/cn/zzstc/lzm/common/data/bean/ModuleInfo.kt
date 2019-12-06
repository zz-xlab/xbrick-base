package cn.zzstc.lzm.common.data.bean

/**
 *
 *
 *
 * @author zhouyufei
 * @since 2019-01-23 11:08
 */

data class ModuleInfo(
    var verName: String="",
    var buildHash: String="",
    var moduleName: String="",
    var debug:Boolean=true,
    var buildType:String="debug"
)