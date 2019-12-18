# Face-beautification
   《数字媒体(2)：多媒体》课程中音频小课堂大作业-人脸美化任务
    
## TODO
   - Android 部分完成
       - 在Face++ API没有完成请求之前不允许UI操作，或者说显示加载中（或者正在处理图片）
       - 增加自选图片功能
       - 增加保存图片到本地功能
   - 美白 ×
   - 磨皮 完成
       - [图像算法---磨皮算法研究汇总](https://blog.csdn.net/trent1985/article/details/50496969)
       - [opencv 美白磨皮人脸检测](https://blog.csdn.net/zhangqipu000/article/details/53260647)
       - 实现了基于双边滤波的磨皮，但是速度过慢，而且效果没有那么好（100的要15秒）
       - [HighPassSkinSmoothing-Android](https://github.com/msoftware/HighPassSkinSmoothing-Android)
       - 速度很快，基本满足要求
   - 大眼 完成
       - [图像处理算法之瘦脸及放大眼睛](https://blog.csdn.net/grafx/article/details/70232797?locationNum=11&fps=1)
       - [Android：代码撸彩妆 2（大眼，瘦脸，大长腿)](https://juejin.im/post/5d5ff49bf265da03d42fae9c)
       - 尝试双轴非等比例缩放，失败，因为眼珠子不圆，改为缩小大眼上线，可以满足效果
   - 瘦脸 完成
       - [Android：修图技术之瘦脸效果](https://mp.weixin.qq.com/s?__biz=MzIwMzYwMTk1NA%3D%3D&mid=2247489371&idx=1&sn=0174082ea4a53de46f7880add8141aed)
       - 直接用了bitmap进行操作，没有用drawBitmapMesh，因为速度已经足够快
       - 选择鼻子中间作为中心，level来控制r\_max，并且level根据脸尺寸进行自适应
   - 口红 完成
        - [Android Canvas之Path操作](https://www.jianshu.com/p/9ad3aaae0c63)
        - 用canvas填充face++拿到的口红边界，然后做个模糊和混合
    
## TIPS
   - 不要局限于opencv的处理方式，可以考虑android的canvas，可以直接在bitmap上绘制
    
    
## 难点
   使用了Java的Opencv接口，而网上的教程基本都是Python和C++版的，API的名称和调用方法也不太一样，这方面花费了不少时间
