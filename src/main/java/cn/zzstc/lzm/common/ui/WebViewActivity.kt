package cn.zzstc.lzm.common.ui

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import cn.zzstc.lzm.common.R
import cn.zzstc.lzm.common.data.bean.WebParam
import cn.zzstc.lzm.common.event.WebFragmentBackEvent
import cn.zzstc.lzm.common.route.BasePath
import cn.zzstc.lzm.common.ui.fragment.WebViewFragment
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


@Route(path = BasePath.BASE_WEB_PAGE)
class WebViewActivity : BaseActivity() {

    @Autowired(name=WebViewFragment.PARAM_KEY)
    lateinit var webParam:WebParam

    override fun setup() {
        setContentView(R.layout.activity_web_view)
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        val fragment = WebViewFragment(webParam)
        fragmentTransaction.replace(R.id.fl_browser_container, fragment)
        fragmentTransaction.commit()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onBackEvent(event: WebFragmentBackEvent){
        finish()
    }

    override fun useEvent(): Boolean =true
}
