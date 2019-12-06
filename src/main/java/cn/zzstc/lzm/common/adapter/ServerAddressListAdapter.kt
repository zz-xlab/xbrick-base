package cn.zzstc.lzm.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding


class ListAdapter<T>(var context: Context, var layoutId: Int,var variableId:Int, var datas: List<T>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: ViewDataBinding? = if (convertView == null) {
            DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, parent, false)
        } else {
            DataBindingUtil.getBinding(convertView)
        }
        binding!!.setVariable(variableId, datas[position])
        return binding.root
    }

    override fun getItem(position: Int): Any = datas[position]!!

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = datas.size

}