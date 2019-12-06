package cn.zzstc.lzm.common.data.bean

import java.io.Serializable

data class WebParam(
    var url:String="",
    var htmlSeg:String="",
    var topBar: Boolean = true,
    var title: String,
    var cache: Boolean = true,
    var allowReload: Boolean = true,
    var showBack: Boolean = true,
    var rightText:String="",
    var rightIcon:Int=0,
    var rightAction:WebRightBtnAction=WebRightBtnAction.None
):Serializable

enum class WebRightBtnAction{
    None,Share,CallPhone,
}