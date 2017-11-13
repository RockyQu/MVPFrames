# MVPFrames
这是一个Android MVP开发框架

## Usage
* 配置自定义Application必须继承BaseApplication，BaseApplication完成Http、图片、DB、日志管理等框架的初始化工作  
* 使用Activity、Fragment、ViewHolder、Service、Adapter需继承CommonActivity、CommonFragment、BaseViewHolder、BaseService、BaseAdapter来初始化MVP架构  
* 通过Application获取AppComponent里面的对象可直接使用  
* 简单功能及页面无需引入MVP

## Download
Gradle:
```
compile 'android.frame:MVPFrames:1.4.2'
```

## History
[UpdateLog](https://github.com/DesignQu/Logg/releases)   

## Libraries
* [Dagger2](https://google.github.io/dagger)  
* [ButterKnife](http://jakewharton.github.io/butterknife)  
* [Retrofit2](https://github.com/square/retrofit)  
* [Okhttp](https://github.com/square/okhttp)  
* [Glide](https://github.com/bumptech/glide)  
* [Gson](https://github.com/google/gson)  
* [GreenDAO](https://github.com/greenrobot/greenDAO)  
* [EasyPermissions](https://github.com/googlesamples/easypermissions)  
* [Logg](https://github.com/DesignQu/Logg)  

## Feedback
* Project  [Submit Bug or Idea](https://github.com/DesignQu/MVPFrames/issues)   

## Thanks
[MVPArms](https://github.com/JessYanCoding/MVPArms)  

## About Me
* Email [china.rocky.coder@gmail.com](china.rocky.coder@gmail.com)  
* Home [https://designqu.github.io](https://designqu.github.io)  
* GitHub [https://github.com/DesignQu](https://github.com/DesignQu)  

## License
```
Copyright 2016-2018 DesignQu

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
