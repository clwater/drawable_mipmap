---
title: Android中drawable和mipmap到底有什么区别
date: 2022-12-20 15:41:44
tags: ["android" , "设计"]
categories : "MpAndroidChart"
cover : https://source.unsplash.com/1600x900/?nature/20221116095921
top_img : https://image-random-clwater.vercel.app/api/random
---


> 老项目代码中发现有的图片放到了`drawable`中, 有的图片放到了`mipmap`中, 开发时秉承哪个目录下文件多放哪里的原则, 偶尔有疑惑搜一搜文章, 看到了结论也就这么使用了, 不过今日有时间, 依次检验了一下文章中的内容, 发现和实际的表现出入甚远. 



# 常见的几种结论
### Case 1  drawable会剔除其它密度, mipmap会保留全部(实际上最终的结论和这个有关联)
> 当xhdpi密度的手机在加载apk的时候Google是有一个优化的，是会剔除drawable其他密度的文件，只保留一个基本的drawable和drawable-xhdpi的文件，而mipmap是会全部保留的。

<!-- more -->

检测方法也比较简单,  在`drawable`和`mipmap`不同密度的问价夹下分别放入同一类图片(图片标文字用于检查), 分别打包并检查其大小

#### Case1.1 安装包与应用大小
|  | 安装包大小 | 应用大小 |
| --- | --- | --- |
| drawable | 13.3 MB (14,016,841 字节) | 14.04MB |
| mipmap | 13.3 MB (14,017,191 字节) | 14.04MB |
###### 结论1.1
由此可见, 虽然两个安装包大小略有差异, 考虑到图片本身的大小(每张图片都在1Mb作用), 可以认为放入`drawable`和`mipmap`文件夹中的图片在安装包和应用安装后没有差异

#### Case1.2 应用内表现

排除安装包的情况, 我们看一下在应用内的表现情况(通过adb shell wm density保证只修改手机的dpi信息)
|  | 100 | 420 | 800 |
| --- | --- | --- | --- |
| drawable | <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212192034141.png"/> | <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212192034231.png"/> | <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212192034624.png"/> |
| mipmap | <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212192032115.png"/> | <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212192031816.png"/> | <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212192033995.png"/> |
###### 结论1.2
由此可见, 文件不论放在哪个目录下, 在手机中都会正确的显示为其匹配的图片资源


### Case 1.3 应用内缩放
>如果一个 imageView 有缩放动画，使用 drawable 下的图片，会一直使用一张来缩放图片实现 imageView 缩放动画。
>如果使用 mipmap 下的图片，会根据缩放程度自动选择比当前分辨率大而又最接近当前分辨率的图片来做缩放处理。

这个可能大家见得不是很多,  不过既然有这种说法, 那就来测试一下

###### drawable 
| 小缩放比例 | 大缩放比例 |
| --- | --- |
| <img width="200" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212192118872.png"/> | <img width="200" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212192118141.png"/> |


##### mipmap 

| 小缩放比例 | 大缩放比例 |
| --- | --- |
| <img width="200" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212192119332.png"/> | <img width="200" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212192119909.png"/> |
###### 结论1.3
可以看到在缩放动画的过程中, 一直显示的都是同一个动画


### Case 2 应用内性能
> Google对mipmap的图片进行了性能优化, 使其可以表现的更好

###### drawable 
| 性能检查一览 | MEMORY | 10次图片加载平均时间 |
| --- | --- | --- |
|  <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212201029065.png"/> |  <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212201030206.png"/> | 146 |

##### mipmap 
| 性能检查一览 | MEMORY | 10次图片加载平均时间 |
| --- | --- | --- |
| <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212201030747.png"/> | <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212201030493.png"/> | 151 |
###### 结论2 
可以看到, 加载单张图片的情况下其性能基本一致,不排除图片太小/太少性能优化不明显的情况, 不过尝试单证图片重复加载的情况下依旧表现为性能相近的情况, 或许时只针对特殊类型有优化? 如各位知道的更详细, 欢迎和我进行交流.

### Case3 启动图标
> 在查阅资料的时候, 发现多次提及minmap应用只放入应用的启动图标, 使其可以得到优化.

|  | 100dpi | 420dpi | 800dpi |
| --- | --- | --- | --- |
|  | <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212201828162.png"/> | <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212201829421.png"/> | <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212201829421.png"/> |

###### 结论3
可以看到, 不同dpi的情况下应用图标的显示情况都是一致的. 其应用图标切换的边界值也是一致的.
关于420dpi和800dpi显示效果一样的情况, 因为种种原因, 应用图片在选择图片资源的时候, 需要将密度扩大25%左右[^1].

**看到这里大家应该和我有着一样的疑惑, 既然drawable和mipmap下图片的表现不论是安装包还是应用内, 甚至连官方文档都这么说了, 为什么各种测试结果下来, 两者的表现基本的一致呢?**

# 罪魁祸首 Bundle(.aab)
提到Bundle(.aab)国内的开发者可能都比较陌生, 甚至不少之前做过Google Play上架应用的都不是很熟悉. 这个其实在我们每次手动打包的时候都会出现.
![](https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212201633188.png)
简单来说.aab包一般用于Google Play商店使用, 在你从Google Play商店下载应用时, 它会根据你手机的实际使用情况来下载不同drawable中的资源. 以期望达到减少安装包大小的目的. (一般情况下手机dpi不会改变, 其它密度下的资源文件直到应用卸载时都不会被使用).

下面的测试使用到的工具为bundletool[^2], 简单来说, 就是模拟从Google Play下载应用和安装应用的过程.

## 安装包比较
|  | 安装包(apks)大小 | 应用大小 |
| --- | --- | --- |
| drawable | 5.91 MB (6,201,543 字节) | 6.22MB |
| mipmap | 12.6 MB (13,230,670 字节) | 13.26MB |

## 应用内表现
|  | 100 | 420 |
| --- | --- | --- |
| drawable | <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212201832564.png"/> | <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212201831242.jpg"/> |
| mipmap | <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212201832837.jpg"/> | <img width="200px" src="https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212201832319.jpg"/> |
可以看到, 当图片放到`drawable`相关文件夹下的时候, 通过.aab包安装的应用会比放到`minmap`的下的应用小许多, 并且应用内更改dpi的时候页可以看到其不再能自动根据当前dpi选择对应的图片了.

# 结论
那么通过以上的测试, 我们可以得到以下结论了
**以下结论均不涉及mipmap的性能优化相关(主要是暂未能设计好一个比较明确的测试对比)**
**以下测试机型为pixel 7, 测试Android版本为13**
1. 当应用构建为.apk的情况下, `drawable`和`mipmap`文件夹下的资源表现无差异, 不论是应用内表现还是在启动器(应用图标)中表现.
2. 当应用构建为.aab的情况下, `drawable`文件夹下的资源会寻找匹配的设备密度保留, 不匹配的资源会被删除已保证apk的大小.而`mipmap`文件夹下的资源文件会全部被保留.

# 那么我们应用内使用的图片就可以放到任意的目录下么?
如果你的应用是通过.apk分发安装的, 原则上是没有区别的.  但是Google对相关的目录也有推荐说明:![](https://clwater-obsidian.oss-cn-beijing.aliyuncs.com/img/202212201846770.png)

可以看到, `mipmap`目录下原则上只能保存应用图标. 同样, 其[官方项目](https://github.com/orgs/android/repositories?type=all)及[单密度资源项目](https://github.com/android/architecture-samples/tree/main/app/src/main/res)也都是这样使用设计这两个文件夹的.

# .aab包内mipmap保留机制是否是只适用于应用图标
测试后可以发现, mipmap的保留机制适用于mipmap下所有的图片资源, 不论是否为应用图标

相关代码可以访问[我的GitHub](https://github.com/clwater/drawable_mipmap)

[^1]: https://developer.android.com/training/multiscreen/screendensities#mipmap
[^2]:https://github.com/google/bundletool/
