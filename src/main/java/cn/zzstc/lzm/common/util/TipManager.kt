package cn.zzstc.lzm.common.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.SystemClock
import android.view.Gravity
import android.widget.Toast
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 提示管理类
 */
object TipManager {
    fun showToast(context: Context?, message: CharSequence?) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun showQMUIDialog(
        context: Activity?,
        msg: String?,
        type: Int,
        autoClose: Boolean
    ): QMUITipDialog {
        val tipDialog = QMUITipDialog.Builder(context)
            .setIconType(type)
            .setTipWord(msg)
            .create()
        if (autoClose) {
            showAutoCloseDialog(tipDialog)
        } else {
            tipDialog.show()
        }
        return tipDialog
    }

    fun createLoading(context: Context?, msg: String?): QMUITipDialog {
        return QMUITipDialog.Builder(context)
            .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
            .setTipWord(msg)
            .create()
    }

    private fun showAutoCloseDialog(tipDialog: QMUITipDialog) {
        tipDialog.show()
        GlobalScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                SystemClock.sleep(1500)
            }
            tipDialog.dismiss()
        }
    }
}