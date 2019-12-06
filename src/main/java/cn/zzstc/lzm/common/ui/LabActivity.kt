package cn.zzstc.lzm.common.ui

import android.content.DialogInterface
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cn.zzstc.lzm.common.R
import cn.zzstc.lzm.common.data.bean.WebParam
import cn.zzstc.lzm.common.databinding.ActivityLabBinding
import cn.zzstc.lzm.common.route.BasePath
import cn.zzstc.lzm.common.ui.fragment.WebViewFragment
import cn.zzstc.lzm.common.ui.vm.LabVm
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import kotlinx.android.synthetic.main.activity_lab.*

@Route(path = BasePath.LAB_PAGE)
class LabActivity : BaseActivity() {
    private lateinit var binding: ActivityLabBinding
    private lateinit var labViewModel: LabVm

    override fun setup() {
        labViewModel = ViewModelProviders.of(this).get(LabVm::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_lab)
        binding.labActivity = this
        binding.labVm = labViewModel
        labTopbar.setTitle(R.string.x_lab)
        labTopbar.addLeftBackImageButton().setOnClickListener { finish() }
    }

    fun click(view: View) {
        when (view.id) {
            R.id.tvLabCurrentUrl -> {
                QMUIDialog.CheckableDialogBuilder(this)
                    .setCheckedIndex(binding.labVm!!.activeAddressIndex())
                    .addItems(binding.labVm!!.getAddressUrls()) { _: DialogInterface?, which: Int ->
                        binding.labVm!!.switchActiveAddress(which)
                    }.create().show()
            }
            R.id.rlLabMgntUrl -> {
                ARouter.getInstance().build(BasePath.LAB_SERVER_ADDRESS).navigation()
            }
            R.id.rlLabScanQr -> {
                ARouter.getInstance().build(BasePath.LAB_SCAN_QR).navigation()
            }
            R.id.rlLabOpenH5 -> {
                val builder = QMUIDialog.EditTextDialogBuilder(this)
                builder.setTitle(getString(R.string.open_web_page))
                    .addAction(
                        QMUIDialogAction(
                            this,
                            R.string.cancel,
                            QMUIDialogAction.ActionListener { dialog, _ -> dialog.dismiss() })
                    )
                    .addAction(
                        QMUIDialogAction(this, R.string.confirm,
                            QMUIDialogAction.ActionListener { dialog, _ ->
                                dialog.dismiss()
                                val address = builder.editText.text.trim()
                                ARouter.getInstance().build(BasePath.BASE_WEB_PAGE)
                                    .withSerializable(
                                        WebViewFragment.PARAM_KEY,
                                        WebParam(address.toString(), "", true, "H5测试", true, true)
                                    ).navigation()
                            })
                    ).create().show()
                builder.editText.setText("https://www.github.com")
            }
            R.id.rlLabShare -> {

            }
            R.id.rlLabPush -> {

            }
            R.id.rlLabNavigation -> {

            }
            R.id.tvLabPushClientId -> {

            }
        }
    }
}
