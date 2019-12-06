# xbrick-base
安卓组件化化开发基础库


### 注意事项
为了实现解耦的目的，组件之间不应该存在之间依赖，即每个组件可单独编译成独立的apk。在 [ArmsComponent](https://github.com/JessYanCoding/ArmsComponent)项目中，主工程对组件的依赖使用 compileOnly project 的方式避免编码过程中误引入代码依赖，导致无法解耦造成的编译错误。但是在此项目中由于使用了databinding，如果使用 compileOnly project 会导致运行错误，因为databinding编译过程中会通过apt/kapt生成代码，使用了compileOnly project 后生成的代码中会有部分缺失导致不可预期的错误，参见 [stackoverflow](https://stackoverflow.com/questions/58788726/question-databinding-not-working-with-runtimeonly-gradle-dependncies)，[简书](https://www.jianshu.com/p/ec09dc60061e)。为了解决此问题，可在编码时使用 compileOnly project 或不添加依赖，在构建主工程时再改为implementation project 的依赖方式。

### 鸣谢
此项目在搭建过程中参考了一些优秀的开源项目，表示感谢。

多模块的application生命周期处理参考：https://github.com/PandaQAQ/PandaMvp
gradle脚本编写参考：https://github.com/JessYanCoding/ArmsComponent
