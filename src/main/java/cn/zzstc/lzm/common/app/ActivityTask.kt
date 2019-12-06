package cn.zzstc.lzm.common.app

import android.app.Activity
import java.util.*
import kotlin.properties.Delegates

class ActivityTask private constructor() {
    /**
     * 添加Activity到堆栈
     *
     * @param activity 入栈的 activity
     */
    fun addActivity(activity: Activity?) {
        mActivityStack!!.add(activity)
    }

    /**
     * 从栈里删除activity
     *
     * @param activity 被删除的 activity
     */
    fun remove(activity: Activity?) {
        if (activity != null && mActivityStack != null) {
            mActivityStack!!.remove(activity)
        }
    }

    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     */
    val topActivity: Activity?
        get() = if (mActivityStack == null || mActivityStack!!.empty()) {
            null
        } else mActivityStack!!.lastElement()

    /**
     * 获取前一个Activity（堆栈中最后一个压入的）
     */
    val lastActivity: Activity?
        get() = if (mActivityStack == null || mActivityStack!!.size < 2) {
            null
        } else mActivityStack!!.get(mActivityStack!!.size - 2)

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    fun killTopActivity() {
        mActivityStack!!.remove(mActivityStack!!.lastElement())
        mActivityStack!!.lastElement()!!.finish()
    }

    /**
     * 结束指定 BaseActivity
     */
    fun killActivity(activity: Activity?) {
        mActivityStack!!.remove(activity)
        activity!!.finish()
    }

    /**
     * 结束指定类名的Activity
     */
    @Synchronized
    fun killActivity(vararg calsses: Class<*>) {
        if (mActivityStack == null || mActivityStack!!.isEmpty()) return
        val activities: MutableList<Activity?> = ArrayList()
        for (cls in calsses) {
            for (activity in mActivityStack!!) {
                if (activity!!::class.java == cls) {
                    activities.add(activity)
                }
            }
        }
        for (activity in activities) {
            killActivity(activity)
        }
    }

    /**
     * 结束所有Activity
     */
    fun killAllActivity() {
        if (mActivityStack == null || mActivityStack!!.isEmpty()) {
            return
        }
        var i = 0
        val size = mActivityStack!!.size
        while (i < size) {
            if (null != mActivityStack!!.get(i)) {
                mActivityStack!!.get(i)!!.finish()
            }
            i++
        }
        mActivityStack!!.clear()
    }

    /**
     * 结束除了当前的其他所有Activity
     *
     * @param activity 保留的 BaseActivity
     */
    fun killOthersActivity(activity: Activity?) {
        if (activity == null) {
            return
        }
        var i = 0
        val size = mActivityStack!!.size
        while (i < size) {
            if (null != mActivityStack!![i] && activity !== mActivityStack!![i]
            ) {
                mActivityStack!![i]!!.finish()
            }
            i++
        }
        mActivityStack!!.clear()
        mActivityStack!!.add(activity)
    }

    /**
     * 退出应用程序
     */
    fun AppExit() {
        try {
            killAllActivity()
            System.exit(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        /**
         * Actvity 存储栈
         */
        private var mActivityStack: Stack<Activity>? = null
        /**
         * ActivityTask 单例对象
         */
        private var mAppManager: ActivityTask? = null

        val instance=ActivityTask()
    }

    init {
        if (mActivityStack == null) {
            mActivityStack = Stack()
        }
    }
}