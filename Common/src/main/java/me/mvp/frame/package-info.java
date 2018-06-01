/*
 * Copyright 2018 RockyQu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 更新日志
 * <p>
 * 1、修改部分核心包名及类名称
 * 2、数据库引入 2017 Google IO 大会 Architecture Components 架构组件 Room
 * 3、完善部分注释
 * 4、框架增加日志模块，提供扩展开关接口
 * 5、增加全局错误回调接口 AppModule.GlobalErrorHandler，可以通过 AppComponent 获取，此接口不限于只处理 HTTP 异常，其他模块也可以回调此接口来处理统一异常管理
 * 6、完善 Demo
 */
package me.mvp.frame;