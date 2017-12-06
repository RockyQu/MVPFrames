/*
 * Copyright 2016-2018 DesignQu
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
 * 1、增加 Snacker 效果类似 Snackbar 只是显示在窗口上面，从上往下推出
 * 2、增加 Toaster 对 Toast 二次封闭，更合理的链式 API 调用方式
 * 3、移除 ToastBar
 * 4、增加 BaseSimpleDialogFragment 基于 DialogFragment 可以轻松实现各种 Dialog 效果
 * 5、修改 文件下载模块，基于 FileDownloader 基本封装，后面会继续完善
 * 6、增加 ImageLoader 配置参数 ScaleType 用法和  ImageView.ScaleType 属性相同，默认不设置任何ScaleType
 * 7、修改 PermissionUtils 应用权限管理 替换为 RxPermissions
 */
package com.tool.common;