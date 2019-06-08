# Anki 涂鸦制卡

<img src='etc/icons/ic_launcher.png' width='100px' height='100px'>

## 简介

“Anki 涂鸦制卡”([下载地址](https://www.coolapk.com/apk/com.mmjang.ankillusion))可快速地在图片上进行知识点遮挡并制作记忆卡片。

图片遮挡流程：

![](etc/pic/ankidoodle.gif)

卡片复习：

![](etc/pic/ankidroid.gif)

“Anki 涂鸦制卡”需要配合记忆卡片软件 Ankidroid（[下载地址](https://www.coolapk.com/apk/com.ichi2.anki
)）使用。使用前需**确保**安装了 Ankidroid。

## 特性

图片可来自图库、相机、应用间分享；

可对图片进行翻转和裁剪这两种预处理流程；

遮挡矩形支持缩放、移动、旋转和删除操作；

支持3种制卡模式：

1. **全遮全显** 制作一张卡片，正面全部遮挡，反面全部显示；

![](etc/pic/mode_1.png)

2. **单遮单显** 根据遮挡矩形区域数量制作多张卡片，每张卡片依次遮挡各个矩形；

![](etc/pic/mode_2.png)

3. **全遮单显** 根据遮挡矩形区域数量制作多张卡片，每张卡片正面遮挡所有区域并依次高亮单个区域，背面仅显示高亮区域。

![](etc/pic/mode_3.png)

## 常见问题

### 为啥卡片中的图片无法同步到 PC 端?

由于 Ankidroid 的某些实现细节，它并不会检查通过接口添加的卡片里的媒体链接，这会导致图片无法进入媒体数据库
并且无法被同步。

为了解决这个问题, 在同步之前先点击 Ankidroid 右上角菜单的“检查媒体”选项。这会让通过涂鸦添加的图片可
同步到其他客户端。