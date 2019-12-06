package cn.zzstc.lzm.common.app

import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import androidx.fragment.app.FragmentManager


interface ILifecycleInjector{
    /**
     * inject module application calls to global application'lifecycle。
     * after all module life cycle added to appLifeCycles，they will be register to global application
     *
     * @param application   global application
     * @param appLifeCycles global application's lifecycle list。
     */
    fun injectAppLifeCycle(
        application: Application?,
        appLifeCycles: List<IAppLifeCycle?>?
    )

    /**
     * after all module life cycle added to activityLifecycle，they will be register to global application
     *
     * @param application                global application
     * @param activityLifecycleCallbacks global application's activityLifecycle list。
     */
    fun injectActivityLifeCycle(
        application: Application?,
        activityLifecycleCallbacks: List<ActivityLifecycleCallbacks?>?
    )

    /**
     * after all module life cycle added to fragmentLifecycleCallbacks，they will be register to global application
     *
     * @param application                global application
     * @param fragmentLifecycleCallbacks global application's fragmentLifecycle list。
     */
    fun injectFragmentLifeCycle(
        application: Application?,
        fragmentLifecycleCallbacks: List<FragmentManager.FragmentLifecycleCallbacks?>?
    )

    /**
     * priority for lifeCycle methods inject
     *
     * @return priority 0 for first
     */
    fun priority(): Int
}