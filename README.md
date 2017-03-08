# MVPFrames
集成目前主流开源框架，涉及Dagger2、ButterKnife、Retrofit2、Glide、Gson等第三方库，适合Android以MVP为结构的中大型项目。

##Usage
•  配置自定义Application必须继承BaseApplication，BaseApplication完成Http、图片、DB、日志管理等框架的初始化工作  
•  使用Activity、Fragment、ViewHolder、Service、Adapter需继承CommonActivity、CommonFragment、BaseViewHolder、BaseService、BaseAdapter来初始化MVP架构  
•  通过Application获取AppComponent里面的对象可直接使用  
•  简单功能及页面无需引入MVP

##Features
•  网络模块：使用Retrofit2，并通过Dagger2注入实例提供给开发者。Retrofit采用注解方式定义接口方便管理，极大的减少了请求代码和步骤，底层采用Okhttp，尽管Google推荐使用HttpURLConnection，但是这个类相比HttpClient相对较弱，而Android5.0已经弃用HttpClient
•  图片模块：目前图片框架种类较多，都有个自的优缺点，所以这里使用策略者模式，使用者只用实现接口，就可以动态替换图片框架，默认使用Glide  
•  数据库模块：在以前的开发过程中，你一定会接触到SQLite，但是在使用时，需要做许多额外的工作，比如编写各种SQL语句，所以这里选用适用于Android的GreenDAO框架。为什么使用GreenDAO，最重要一点是它性能最大化、内存开销最小化并对Android进行高度优化，性能高于同类的ORMLite  

##Feedback
•  Project  [Submit Bug or Idea](https://github.com/DesignQu/MVPFrames/issues)   

##Thanks
[MVPArms](https://github.com/JessYanCoding/MVPArms)

##License
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
