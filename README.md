# 《一剪没》OneClipWiping

 一个可能让剪贴板只被使用一次的 Android App。

#### 初衷

其实谷歌从 Android 10 开始就禁止后台应用读写剪贴板了，这一定程度上改善了剪贴板的安全性，但是我们仍可看到，在打开某些应用时剪贴板被疯狂读取，此时如果剪贴板里存在一些手机号、身份证号以及详细地址等个人隐私信息的话，无疑是会被其读取的，至于它们拿来干嘛只有鬼知道。

#### 初步实现

借助无障碍服务 AccessibilityService 监听全局复制和粘贴事件，初步实现两项机制：

- 粘贴动作完成后立即清空剪贴板。

- 复制动作后，如果 10 秒内没有进行粘贴动作，也会清空剪贴板。

#### 注意事项

- 需要允许拥有通知权限，用于开启前台服务提高存活几率。

- 需要允许自启动，每家手机厂商设置不一样，大同小异。

- 多任务列表窗口加锁是提升存活率的好办法。

- vivo 系手机一定务必开启：**设置**->**电池**->**后台耗电管理**->**允许后台高耗电**，否则服务无法正常允许。

- 目前还处于前期开发阶段，Bug 在所难免，当发现无障碍服务明明已经打开，但是还是无法工作时，请尝试重启手机。

#### 已知问题

微信聊天界面的输入框以及聊天列表重写了 ContextMenu，无障碍服务拿不到复制和粘贴的点击事件，目前只能依赖于复制后系统的 Toast 提示 “已复制”，然后等待 10 秒后清空剪贴板，这一块有待深入研究。

#### TODO

- 加强应用的保活策略

- 实现各项参数可配置

- 白名单机制

- 智能忽略机制

- 正则表达式匹配清除

- 不妥协的兜底清除机制

#### License

[Apache License Version 2.0](https://github.com/li-yu/OneClipWiping/blob/main/LICENSE)
