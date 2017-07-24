# MVPFrames
这是一个适用于大中小全能型AndroidMVP开发框架，主要集成目前主流开源框架，涉及Dagger2、ButterKnife、Retrofit2、Glide、Gson等第三方库

## Usage
* 配置自定义Application必须继承BaseApplication，BaseApplication完成Http、图片、DB、日志管理等框架的初始化工作  
* 使用Activity、Fragment、ViewHolder、Service、Adapter需继承CommonActivity、CommonFragment、BaseViewHolder、BaseService、BaseAdapter来初始化MVP架构  
* 通过Application获取AppComponent里面的对象可直接使用  
* 简单功能及页面无需引入MVP

## Features
* **网络模块** 使用Retrofit2，并通过Dagger2注入实例提供给开发者。Retrofit采用注解方式定义接口方便管理，极大的减少了请求代码和步骤，底层采用Okhttp  
* **图片模块** 使用策略者模式，使用者只需替换三个类，就可以更换图片框架，默认使用Glide，通过AppComponent获取ImageLoader实例  
* **数据库模块** 选择适用于Android的GreenDAO框架，通过AppComponent获取DaoSession实例

## Libraries
* [Dagger2](https://google.github.io/dagger)  
* [ButterKnife](http://jakewharton.github.io/butterknife)  
* [Retrofit2](https://github.com/square/retrofit)  
* [Okhttp](https://github.com/square/okhttp)  
* [Glide](https://github.com/bumptech/glide)  
* [Gson](https://github.com/google/gson)  
* [GreenDAO](https://github.com/greenrobot/greenDAO)  
* [EasyPermissions](https://github.com/googlesamples/easypermissions)  
* [Q-Log](https://github.com/googlesamples/easypermissions)  

## Feedback
* Project  [Submit Bug or Idea](https://github.com/DesignQu/MVPFrames/issues)   

## Thanks
[MVPArms](https://github.com/JessYanCoding/MVPArms)  
[TODO-MVP](https://github.com/googlesamples/android-architecture/tree/todo-mvp)  
[Nucleus](https://github.com/konmik/nucleus)  

## License
```
Copyright 2016-2017 DesignQu

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
