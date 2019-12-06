# xbrick-base

### 基础介绍
安卓组件化化开发基础库

![宿主首页](https://raw.githubusercontent.com/zz-xlab/xbrick-app/master/pictures/app.png)



项目使用纯kotlin+mvvm+databinding开发

一个主工程(参考xbrick-app)+多个业务组件(参考xbrick-user)+一个基础服务库(参考xbrick-base)，可打包成一个最终的应用包。
每个组件单独使用一个git repo。最终在主项目中使用git submodule对业务组件代码进行管理

![用户组件登录页](https://github.com/zz-xlab/xbrick-app/blob/master/pictures/login.png?raw=true)

基于以上步骤可快速复用多个业务模块组合成各有差异，业务模块复用的应用。中正移动端团队已在实际项目中成功运用，此方式比使用一份代码多个app分支的方式更易维护。

组件之间通信使用ARouter+Eventbus相结合的方式，实现解耦。



![项目结构](https://raw.githubusercontent.com/zz-xlab/xbrick-app/master/pictures/arch.jpg)


基础服务模块自带实验室功能，帮助开发过程中快速调试。
实验室功能清单：
1.新增删除接口服务器地址
2.动态切换接口服务器地址
3.查看设备推送唯一ID(待实现)
4.扫描二维码、条形码
5.打开H5页面
6.分享测试(待实现)
7.通知测试(待实现)
8.应用内跳转测试(待实现)
9.模块信息列表(待新增模块构建hash值)
![实验室](https://github.com/zz-xlab/xbrick-app/blob/master/pictures/xlab.png?raw=true)



### 注意事项

1. 为了实现解耦的目的，组件之间不应该存在之间依赖，即每个组件可单独编译成独立的apk。在 [ArmsComponent](https://github.com/JessYanCoding/ArmsComponent)项目中，主工程对组件的依赖使用 compileOnly project 的方式避免编码过程中误引入代码依赖，导致无法解耦造成的编译错误。但是在此项目中由于使用了databinding，如果使用 compileOnly project 会导致运行错误，因为databinding编译过程中会通过apt/kapt生成代码，使用了compileOnly project 后生成的代码中会有部分缺失导致不可预期的错误，参见 [stackoverflow](https://stackoverflow.com/questions/58788726/question-databinding-not-working-with-runtimeonly-gradle-dependncies)，[简书](https://www.jianshu.com/p/ec09dc60061e)。为了解决此问题，可在编码时使用 compileOnly project 或不添加依赖，在构建主工程时再改为implementation project 的依赖方式。

2. 基础服务组件中使用BaseApp模块继承了Application，在主工程的manifest文件中需指定app为BaseApp，业务组件或主工程中需要使用到Application及其生命周期的地方通过生命周期的委托方式实现。参照xbrick-app组件中的LifeCycleInjector类实现，并将其配置到manifest文件中。

3. 为了在实验室中展示当前app内的组件信息，需在各组件自身的build.gradle文件中增加模块信息配置及在Manifest文件中指定buildConfig类名称，如下示例：

````
组件的build.gradle配置组件信息
buildTypes {
        release {
            buildConfigField "String", "MODULE_NAME", '"用户信息"'
        }
        debug {
            buildConfigField "String", "MODULE_NAME", '"用户信息"'
        }
    }
    
组件的AndroidManifest.xml配置模块信息
<meta-data
            android:name="cn.zzstc.lzm.BuildConfig"
            android:value="@string/module_info" />
````

### 计划添加


### 鸣谢
此项目在搭建过程中参考了一些优秀的开源项目，表示感谢。

多模块的application生命周期处理参考：https://github.com/PandaQAQ/PandaMvp
gradle脚本编写参考：https://github.com/JessYanCoding/ArmsComponent

此外本项目中还用到的第三方库：Arouter、EventBus、bga-qrcode-zbar、QMUI、logger、autosize等表示感谢

###其它
欢迎star及pr