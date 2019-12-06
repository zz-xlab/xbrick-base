package cn.zzstc.lzm.common.ui

import android.view.View
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import cn.zzstc.lzm.common.R
import cn.zzstc.lzm.common.databinding.ActivityServerAddressBinding
import cn.zzstc.lzm.common.route.BasePath
import cn.zzstc.lzm.common.ui.vm.AddressListVm
import cn.zzstc.lzm.common.util.TipManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.qmuiteam.qmui.widget.dialog.QMUIDialog
import com.qmuiteam.qmui.widget.dialog.QMUIDialog.EditTextDialogBuilder
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog
import kotlinx.android.synthetic.main.activity_lab.labTopbar


@Route(path = BasePath.LAB_SERVER_ADDRESS)
class ServerAddressActivity : BaseActivity() {

    private lateinit var binding: ActivityServerAddressBinding
    private lateinit var addressListVm: AddressListVm

    override fun setup() {
        addressListVm = ViewModelProviders.of(this).get(AddressListVm::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_server_address)
        binding.addressVm=addressListVm
        binding.activity=this

        labTopbar.setTitle(R.string.manage_server_address)
        labTopbar.addLeftBackImageButton().setOnClickListener { finish() }
    }

    fun click(view:View){

        when(view.id){
            R.id.fabLabAddAddress->{
                val builder = EditTextDialogBuilder(this)
                builder.setTitle(R.string.add_server_address)
                    .setPlaceholder(R.string.add_server_address)
                    .addAction(QMUIDialogAction(this,R.string.cancel,QMUIDialogAction.ActionListener {dialog, _ -> dialog.dismiss()}))
                    .addAction(QMUIDialogAction(this,R.string.confirm,QMUIDialogAction.ActionListener {dialog, _ ->
                        dialog.dismiss()
                        var newAddress=builder.editText.text.trim()
                        if (newAddress.isNotBlank()&&newAddress.contains("http")){
                            binding.addressVm!!.save(newAddress)
                            TipManager.showQMUIDialog(this,getString(R.string.save_success),QMUITipDialog.Builder.ICON_TYPE_SUCCESS,true)
                        }else{
                            TipManager.showQMUIDialog(this,getString(R.string.not_saved),QMUITipDialog.Builder.ICON_TYPE_FAIL,true)
                        }
                    })).create().show()
            }
        }
    }

    fun addressLongClick(parent: AdapterView<*>, view: View, position:Int, id:Long ):Boolean{
        QMUIDialog.MessageDialogBuilder(this).setTitle(R.string.tip)
            .setMessage(R.string.delete_address_confirm)
            .addAction(QMUIDialogAction(this,R.string.cancel,QMUIDialogAction.ActionListener { dialog, _ -> dialog.dismiss() }))
            .addAction(QMUIDialogAction(this,R.string.delete,QMUIDialogAction.ACTION_PROP_NEGATIVE,
                QMUIDialogAction.ActionListener { dialog, index -> dialog.dismiss()
                    addressListVm.removeAddress(index)
                    dialog.dismiss()
                })).create().show()

        return true
    }

}
