package cn.zzstc.lzm.common.ui.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import org.greenrobot.eventbus.EventBus

abstract class BaseFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (useEvent()){
            EventBus.getDefault().register(this)
        }
        setup()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (useEvent()){
            EventBus.getDefault().unregister(this)
        }
    }

    abstract fun setup()

    open fun useEvent()=false
}