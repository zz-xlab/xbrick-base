package cn.zzstc.lzm.common.app

import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.content.Context
import androidx.fragment.app.FragmentManager


class AppProxy(application: Application) : IAppLifeCycle {
    private var mAppLifeCycles: List<IAppLifeCycle> = ArrayList()
    private var mActivityLifeCycles: MutableList<ActivityLifecycleCallbacks> = ArrayList()
    private var mFragmentLifecycleCallbacks: List<FragmentManager.FragmentLifecycleCallbacks> = ArrayList()

    init {
        val injectors =
            ManifestParser(application).parse()
        for (injector in injectors) {
            injector.injectAppLifeCycle(application, mAppLifeCycles)
            injector.injectActivityLifeCycle(application, mActivityLifeCycles)
            injector.injectFragmentLifeCycle(application, mFragmentLifecycleCallbacks)
        }
        mActivityLifeCycles.add(DefaultActivityLifecycle(mFragmentLifecycleCallbacks))
    }

    override fun onAttach(base: Context) {
        for (appLifeCycle in mAppLifeCycles) {
            appLifeCycle.onAttach(base)
        }
    }

    override fun onCreate(application: Application) {
        for (appLifeCycle in mAppLifeCycles) {
            appLifeCycle.onCreate(application)
        }
        for (callbacks in mActivityLifeCycles) {
            application.registerActivityLifecycleCallbacks(callbacks)
        }
    }

    override fun onTerminate(application: Application) {
        for (appLifeCycle in mAppLifeCycles) {
            appLifeCycle.onTerminate(application)
        }

        for (callbacks in mActivityLifeCycles) {
            application.unregisterActivityLifecycleCallbacks(callbacks)
        }
    }
}