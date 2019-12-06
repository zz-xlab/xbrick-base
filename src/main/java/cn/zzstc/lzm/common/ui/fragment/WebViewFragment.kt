package cn.zzstc.lzm.common.ui.fragment

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import cn.zzstc.lzm.common.R
import cn.zzstc.lzm.common.data.bean.WebParam
import cn.zzstc.lzm.common.event.WebBtnEvent
import cn.zzstc.lzm.common.event.WebFragmentBackEvent
import com.tencent.smtt.sdk.WebChromeClient
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import kotlinx.android.synthetic.main.fragment_web_view.*
import org.greenrobot.eventbus.EventBus

class WebViewFragment(params: WebParam) : BaseFragment(R.layout.fragment_web_view) {
    companion object {
        const val PARAM_KEY = "web_param"

    }

    lateinit var params: WebParam

    init {
        val bundle = Bundle()
        bundle.putSerializable(PARAM_KEY, params)
        arguments = bundle
    }

    override fun setup() {
        params = arguments!![PARAM_KEY] as WebParam

        if (!params.topBar) {
            qtbWeb.visibility = View.GONE
        } else {
            if (params.title.isNotEmpty()) {
                qtbWeb.setTitle(params.title)

            }
            if (params.showBack) {
                qtbWeb.addLeftBackImageButton().setOnClickListener {
                    pwvWeb.stopLoading()
                    EventBus.getDefault().post(
                        WebFragmentBackEvent()
                    )
                }
            }
            if (params.rightText.isNotEmpty()) {
                qtbWeb.addRightTextButton(params.rightText, R.id.top_bar_right_button)
                    .setOnClickListener { rightBtnAction() }
            } else if (params.rightIcon != 0) {
                qtbWeb.addRightImageButton(params.rightIcon, R.id.top_bar_right_button)
                    .setOnClickListener { rightBtnAction() }
            }
        }
        if (params.cache) {
            pwvWeb.settings.cacheMode = WebSettings.LOAD_DEFAULT
            pwvWeb.settings.setAppCacheEnabled(true)
            pwvWeb.settings.setAppCacheMaxSize(5 * 1024 * 1024)
            pwvWeb.settings.setAppCachePath(activity!!.cacheDir.absolutePath)
            pwvWeb.settings.databaseEnabled = true
        } else {
            pwvWeb.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            pwvWeb.settings.setAppCacheEnabled(false)
            pwvWeb.settings.databaseEnabled = false
        }

        if (params.allowReload) {
            tvWebLoadFailure.text = getString(R.string.reload_default)
            tvWebLoadFailure.setOnClickListener { load() }
        } else {
            tvWebLoadFailure.text = getString(R.string.load_failure_default)
        }
        pwvWeb.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(p0: WebView?, p1: Int) {
                super.onProgressChanged(p0, p1)
                if(pwvWeb!=null) {
                    pwvWeb.setProgressBar(p1)
                }
            }

            override fun onReceivedTitle(p0: WebView?, p1: String?) {
                super.onReceivedTitle(p0, p1)
                if (pwvWeb!=null&&qtbWeb!=null) {
                    if ((!TextUtils.isEmpty(p1)) && p1.equals("Error 404 Not Found")) {
                        tvWebLoadFailure.visibility = View.VISIBLE
                        pwvWeb.visibility = View.GONE
                    } else {
                        if (params.topBar && params.title.isBlank()) {
                            qtbWeb.setTitle(p1)
                        }
                    }
                }
            }
        }
        load()
    }

    fun addJsInterface(method: Any, mountName: String) {
        pwvWeb.addJavascriptInterface(method, mountName)
    }

    private fun load() {
        tvWebLoadFailure.visibility = View.GONE
        pwvWeb.visibility = View.VISIBLE
        if (params.url.isNotEmpty()) {
            pwvWeb.loadUrl(params.url)
        } else if (params.htmlSeg.isNotEmpty()) {
            pwvWeb.loadDataWithBaseURL(null, params.htmlSeg, "text/html", "utf-8", null)
        }
    }

    private fun rightBtnAction() {
        EventBus.getDefault().post(WebBtnEvent(params.rightAction))
    }

}