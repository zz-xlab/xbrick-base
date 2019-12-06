package cn.zzstc.lzm.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import cn.zzstc.lzm.common.R
import com.tencent.smtt.export.external.interfaces.IX5WebSettings
import com.tencent.smtt.sdk.ValueCallback
import com.tencent.smtt.sdk.WebSettings
import com.tencent.smtt.sdk.WebView
import com.tencent.smtt.sdk.WebViewClient


class ProgressWebView @JvmOverloads constructor(
    context: Context?,
    attr: AttributeSet? = null,
    var3: Int = 0,
    var4: Boolean = false
) : WebView(context, attr, var3, var4) {
    private var  mProgressBar: ProgressBar = ProgressBar(getContext(), null, android.R.attr.progressBarStyleHorizontal)

    init {
        mProgressBar.layoutParams=LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            6)
        mProgressBar.progressDrawable = resources.getDrawable(R.drawable.layer_list_progress_webview)

        webViewClient = object:WebViewClient(){
            override fun shouldOverrideUrlLoading(view:WebView, url:String ):Boolean {
                view.loadUrl(url)
                return true
            }
        }
        addView(mProgressBar)
        val webSetting: WebSettings = this.settings
        webSetting.javaScriptEnabled = true
        webSetting.javaScriptCanOpenWindowsAutomatically = true
        webSetting.allowFileAccess = true
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(true)
        webSetting.builtInZoomControls = true
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(true)
        webSetting.loadWithOverviewMode = true
        webSetting.setAppCacheEnabled(true)
        webSetting.databaseEnabled = true
        webSetting.domStorageEnabled = true
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE)
        webSetting.pluginState = WebSettings.PluginState.ON_DEMAND
        webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH)
        webSetting.cacheMode = WebSettings.LOAD_NO_CACHE
        webSetting.setSupportZoom(false)
    }

    fun setProgressBar(newProgress: Int) {
        mProgressBar.progress = newProgress
        if (newProgress == 100) {
            mProgressBar.visibility = View.GONE
        } else if (mProgressBar.visibility == View.GONE) {
            mProgressBar.visibility = View.VISIBLE
        }
    }

    fun callJs(valueCallback: ValueCallback<String>?=null, method:String, vararg params:String){
        evaluateJavascript("JavaScript:$method(${params.joinToString()})",valueCallback)
    }
}