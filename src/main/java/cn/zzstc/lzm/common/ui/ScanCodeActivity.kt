package cn.zzstc.lzm.common.ui

import android.content.Context
import android.os.Vibrator
import android.view.View
import android.view.WindowManager
import cn.bingoogolapple.qrcode.core.QRCodeView
import cn.zzstc.lzm.common.R
import cn.zzstc.lzm.common.event.QrCodeEvent
import cn.zzstc.lzm.common.route.BasePath
import cn.zzstc.lzm.common.util.TipManager.showToast
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_scan_code.*
import org.greenrobot.eventbus.EventBus

@Route(path = BasePath.LAB_SCAN_QR)
class ScanCodeActivity : BaseActivity(), QRCodeView.Delegate,
    View.OnClickListener {
    override fun onStart() {
        super.onStart()
        zbvLabQrCode.startCamera() // 打开后置摄像头开始预览，但是并未开始识别
        zbvLabQrCode.startSpotAndShowRect() // 显示扫描框，并且延迟0.1秒后开始识别
    }

    override fun onStop() {
        zbvLabQrCode.stopCamera() // 关闭摄像头预览，并且隐藏扫描框
        super.onStop()
    }

    override fun onDestroy() {
        zbvLabQrCode.onDestroy() // 销毁二维码扫描控件
        super.onDestroy()
    }

    private fun vibrate() {
        val vibrator =
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(200)
    }

    override fun onClick(view: View) {
        val i = view.id
        if (i == R.id.top_light) {
            isLightOn = if (!isLightOn) {
                zbvLabQrCode.openFlashlight()
                true
            } else {
                zbvLabQrCode.closeFlashlight()
                false
            }
        } else if (i == R.id.top_back) {
            finish()
        }
    }

    override fun onScanQRCodeSuccess(result: String) {
        vibrate()
        EventBus.getDefault().post(QrCodeEvent(result))
        showToast(this,"扫描成功")
        finish()
    }

    override fun onCameraAmbientBrightnessChanged(isDark: Boolean) {
        var tipText = zbvLabQrCode.scanBoxView.tipText
        val ambientBrightnessTip = "\n环境过暗，请打开闪光灯"
        if (isDark) {
            if (!tipText.contains(ambientBrightnessTip)) {
                zbvLabQrCode.scanBoxView.tipText = tipText + ambientBrightnessTip
            }
        } else {
            if (tipText.contains(ambientBrightnessTip)) {
                tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip))
                zbvLabQrCode.scanBoxView.tipText = tipText
            }
        }
    }

    override fun onScanQRCodeOpenCameraError() {
        showToast(this, getString(R.string.open_camera_error))
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {}
    override fun setup() {
        setContentView(R.layout.activity_scan_code)
        val window = window
        // Translucent status bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        zbvLabQrCode.setDelegate(this)
        findViewById<View>(R.id.top_light).setOnClickListener(this)
        findViewById<View>(R.id.top_back).setOnClickListener(this)
    }

    companion object {
        private var isLightOn = false
    }
}