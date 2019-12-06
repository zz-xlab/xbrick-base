package cn.zzstc.lzm.common.binding

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import cn.zzstc.lzm.common.data.entity.ServerAddress

object BaseBindingAdapter {
    @JvmStatic
    @BindingAdapter("android:activeAddress")
    fun activeAddress(view: TextView, allAddress: List<ServerAddress>) {
        allAddress.forEach {
            if (it.active){
                view.text = it.url
            }
        }
    }

    @JvmStatic
    @BindingAdapter("android:src")
    fun setImageResource(imageView: ImageView, resource: Int) {
        imageView.setImageResource(resource)
    }
}