package cn.zzstc.lzm.common.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder


val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()