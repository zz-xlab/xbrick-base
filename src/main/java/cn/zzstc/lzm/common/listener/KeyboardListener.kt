package cn.zzstc.lzm.common.listener

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewTreeObserver
import cn.zzstc.lzm.common.util.DisplayUtil

class KeyboardUtil : ViewTreeObserver.OnGlobalLayoutListener {
    var view: View? = null
    var listener: KeyboardListener? = null
    var visibleThreshold = 0
    var r = Rect()
    var currentState = false

    fun listen(activity: Activity, listener: KeyboardListener) {
        visibleThreshold = Math.round(DisplayUtil.dip2px(activity, 100f))
        this.listener = listener
        view = activity.window.decorView
        view?.viewTreeObserver?.addOnGlobalLayoutListener(this)

    }

    fun removeListener() {
        view?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        view!!.getWindowVisibleDisplayFrame(r)
        var diff = view!!.rootView.height - r.height()
        var isOpen = diff > visibleThreshold
        if (isOpen != currentState) {
            listener?.keyboardChange(isOpen)
        }
        currentState = isOpen
    }
}

interface KeyboardListener {
    fun keyboardChange(isOpen: Boolean)
}