package cn.zzstc.lzm.common.app

import android.content.Context
import android.content.pm.PackageManager
import cn.zzstc.lzm.common.R
import java.util.*

class ManifestParser(private val context: Context) {
    private val moduleValue: String = context.getString(R.string.lifecycle_proxy)
    fun parse(): List<ILifecycleInjector> {
        val modules: MutableList<ILifecycleInjector> =
            ArrayList()
        try {
            val appInfo = context.packageManager.getApplicationInfo(
                context.packageName, PackageManager.GET_META_DATA
            )
            if (appInfo.metaData != null) {
                for (key in appInfo.metaData.keySet()) {
                    if (moduleValue == appInfo.metaData[key]) {
                        modules.add(parseModule(key))
                    }
                }
            }
            // 根据 priority 对扫描结果顺序进行调整，确定先初始化什么后初始化什么
            modules.sortWith(Comparator { o1: ILifecycleInjector, o2: ILifecycleInjector -> o1.priority() - o2.priority() })
        } catch (e: PackageManager.NameNotFoundException) {
            throw RuntimeException("Unable to find metadata to parse ConfigModule", e)
        }
        return modules
    }

    companion object {
        private fun parseModule(className: String): ILifecycleInjector {
            val clazz: Class<*>
            clazz = try {
                Class.forName(className)
            } catch (e: ClassNotFoundException) {
                throw IllegalArgumentException(
                    "Unable to find ConfigModule implementation",
                    e
                )
            }
            val module: Any
            module = try {
                clazz.newInstance()
            } catch (e: InstantiationException) {
                throw RuntimeException(
                    "Unable to instantiate ConfigModule implementation for $clazz",
                    e
                )
            } catch (e: IllegalAccessException) {
                throw RuntimeException(
                    "Unable to instantiate ConfigModule implementation for $clazz",
                    e
                )
            }
            if (module !is ILifecycleInjector) {
                throw RuntimeException("Expected instanceof ConfigModule, but found: $module")
            }
            return module
        }
    }

}