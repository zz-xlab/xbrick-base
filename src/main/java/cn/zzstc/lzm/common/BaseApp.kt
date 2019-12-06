package cn.zzstc.lzm.common

import android.app.Application
import android.content.Context
import cn.zzstc.lzm.common.app.AppProxy
import cn.zzstc.lzm.common.app.IAppLifeCycle
import cn.zzstc.lzm.common.net.NetworkManager
import com.alibaba.android.arouter.launcher.ARouter
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy


class BaseApp : Application() {

    private lateinit var appProxy: IAppLifeCycle

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        appProxy = AppProxy(this)
        appProxy.onAttach(base!!)
    }

    override fun onCreate() {
        super.onCreate()
        context = this
        // 配置必须在init之前否则将无效
        // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        if (BuildConfig.IS_DEBUG) {
            ARouter.openLog()
            ARouter.openDebug()
        }
        //debug模式打印所有日志  release模式打印error日志
        Logger.addLogAdapter(object :
            AndroidLogAdapter(PrettyFormatStrategy.newBuilder().tag("LZM").build()) {
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return if (BuildConfig.IS_DEBUG) true else (priority > Logger.WARN)
            }
        })
        ARouter.init(this)
        NetworkManager.init()
        appProxy.onCreate(this)

    }

    override fun onTerminate() {
        super.onTerminate()
        appProxy.onTerminate(this)
    }

    companion object {
        private lateinit var context: Context
        fun instance() = context
    }


}