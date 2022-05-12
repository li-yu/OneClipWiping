# 《一剪没》OneClipWiping

![https://github.com/li-yu/OneClipWiping/blob/main/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png](https://github.com/li-yu/OneClipWiping/blob/main/app/src/main/res/mipmap-xxxhdpi/ic_launcher.png)

 一个可能让剪贴板只被使用一次的 Android App。

#### 初衷

谷歌从 Android 10 开始禁止**后台应用**读写剪贴板，这一定程度上改善了剪贴板的安全性，但是我们仍能看到，在打开某些应用时剪贴板被疯狂读写，此时如果剪贴板里存在一些手机号、身份证号以及详细地址等个人隐私信息的话，无疑是会被其读取的，至于它们拿来干嘛只有鬼知道。

#### 目标

- 粘贴动作完成后立即清空剪贴板。

- 复制动作后，如果 15 秒内没有进行任何粘贴动作，同样执行清空操作。

#### 原理

- 借助无障碍服务 AccessibilityService 监听全局复制、剪切和粘贴事件。

- 无障碍服务 AccessibilityService 下不需要申请悬浮窗权限。

- 透明悬浮窗获取到焦点后可以进行剪贴板操作，这对于用户是无感知的。

- 另一种监听复制事件的方法：
  
  ```kotlin
    // 和以前一样，App 同样需要实现以下方法监听剪贴板的变动事件
    clipboardManager.addPrimaryClipChangedListener {
      // 当 App 在后台时，Android 10 以后虽然无法正常回调此方法，
      // 但是系统会产生报错日志，我们捕获这个报错日志即可
      // 注意：需要赋予 android.permission.READ_LOGS 权限才能抓到日志（部分手机无效）
  }
  ```

#### 注意事项

- 允许拥有通知权限，开启前台服务提高存活几率。

- 允许自启动，每家手机厂商设置方式不一样，但大同小异。

- 多任务列表窗口加锁也是提升存活率的好办法。

- vivo 系手机一定务必开启：**设置**->**电池**->**后台耗电管理**->**允许后台高耗电**，否则服务无法正常运行。

- 执行 `adb -d shell pm grant cn.liyuyu.oneclipwiping android.permission.READ_LOGS`，部分手机即使赋权后，还是不能捕获到 `logcat`，比如 vivo 系。

- 当发现无障碍服务已经开启，但还是无法正常工作时，请尝试重启手机，屡试不爽。

#### 已知问题

微信聊天界面的输入框以及聊天列表均重新实现了 ContextMenu，无障碍服务拿不到其复制和粘贴的点击事件，目前只能依赖于复制后系统的 Toast 提示的 “已复制” 或者借助 `READ_LOGS`，然后等待指定时间后清空剪贴板。

#### TODO

- 加强应用的保活策略

- 实现各项参数可配置

- 白名单机制

- 智能忽略机制

- 正则表达式匹配清除

- 不妥协的兜底清除机制

#### License

[Apache License Version 2.0](https://github.com/li-yu/OneClipWiping/blob/main/LICENSE)
