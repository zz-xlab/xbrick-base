package cn.zzstc.lzm.common.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import com.alibaba.android.arouter.launcher.ARouter
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (useEvent()){
            EventBus.getDefault().register(this)
        }
        ARouter.getInstance().inject(this)
        setup()
    }

    abstract fun setup()

    override fun onDestroy() {
        super.onDestroy()
        if (useEvent()){
            EventBus.getDefault().unregister(this)
        }
    }

    open fun useEvent():Boolean{
        return false
    }
}
