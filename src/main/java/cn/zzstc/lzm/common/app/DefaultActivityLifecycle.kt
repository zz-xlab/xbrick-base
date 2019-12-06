package cn.zzstc.lzm.common.app

import android.app.Activity
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager


class DefaultActivityLifecycle internal constructor(fragmentLifeCycles: List<FragmentManager.FragmentLifecycleCallbacks>) :
    ActivityLifecycleCallbacks {
    private val mFragmentLifeCycles: List<FragmentManager.FragmentLifecycleCallbacks> =
        fragmentLifeCycles

    override fun onActivityCreated(
        activity: Activity,
        savedInstanceState: Bundle?
    ) {
        ActivityTask.instance.addActivity(activity)
        registerFragmentCallbacks(activity)
    }

    override fun onActivityStarted(activity: Activity?) {}
    override fun onActivityResumed(activity: Activity?) {}

    override fun onActivityPaused(activity: Activity?) {}
    override fun onActivityStopped(activity: Activity?) {}
    override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {}
    override fun onActivityDestroyed(activity: Activity?) {
        ActivityTask.instance.remove(activity)
    }

    //注册框架外部, 开发者扩展的 BaseFragment 生命周期逻辑
    private fun registerFragmentCallbacks(activity: Activity) {
        for (fragmentLifecycle in mFragmentLifeCycles) {
            if (activity is FragmentActivity) {
                activity.supportFragmentManager
                    .registerFragmentLifecycleCallbacks(fragmentLifecycle, true)
            }
        }
    }

}