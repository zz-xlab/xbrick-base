package cn.zzstc.lzm.common.route


/**
 * 登录模块
 */
object UserPath {
    //登录页
    const val LOGIN = "/user/login"
    //忘记密码
    const val FORGET_PWD = "/user/forget_pwd"

}

/**公共服务
 */
object ConnectorPath {
    //token服务
    const val TOKEN_SERVICE="/user/token_service"

    const val ADDRESS_SERVER="/common/address_service"

}

/**
 * 公共路径页面
 */
object CommonPath{
    const val MAIN_PAGE="/common/index"
}

/**
 * 基础服务页面
 */
object BasePath{
    const val LAB_PAGE="/base/xlab"
    const val LAB_SERVER_ADDRESS="/base/server_address"
    const val LAB_SCAN_QR="/base/scan_qr"
    const val BASE_WEB_PAGE="/base/web"
}
