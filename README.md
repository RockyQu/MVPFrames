# MVPFrames
整合大量主流开源项目并且可高度配置化的 Android MVP 快速集成框架

## Download
Gradle:
```
api 'me.frame:MVPFrames:3.0.0'
```

## Libraries
* [Dagger2](https://google.github.io/dagger)  
* [RxJava2](https://github.com/ReactiveX/RxJava)  
* [RxLifecycle](https://github.com/trello/RxLifecycle)  
* [Butterknife](https://github.com/JakeWharton/butterknife)  
* [Retrofit2](https://github.com/square/retrofit)  
* [Okhttp](https://github.com/square/okhttp)  
* [Glide](https://github.com/bumptech/glide)  
* [Gson](https://github.com/google/gson)  
* [RxPermissions](https://github.com/tbruyelle/RxPermissions)  
* [LeakCanary](https://github.com/square/leakcanary)  
* [Room](https://developer.android.com/topic/libraries/architecture/room.html)  2017 Google IO 大会 Architecture Components 架构组件
* [Logg](https://github.com/RockyQu/Logg)  

## History
[UpdateLog](https://github.com/RockyQu/MVPFrames/releases)

## Proguard
```
-keep public class * implements me.mvp.frame.integration.ConfigModule
-keep class * implements me.mvp.frame.frame.IModel {
    <methods>;
    <fields>;
}
```
       
## Feedback
* Project  [Submit Bug or Idea](https://github.com/RockyQu/MVPFrames/issues)

## Thanks
* [MVPArms](https://github.com/JessYanCoding/MVPArms)  
* [Architecture Components](https://github.com/googlesamples/android-architecture-components)

## About Me
* Email [250533855@qq.com](250533855@qq.com)  
* Home [http://rockycoder.cn](http://rockycoder.cn)  
* GitHub [https://github.com/RockyQu](https://github.com/RockyQu)  

## License
```
Copyright 2018 RockyQu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
