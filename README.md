# Face-beautification
    《数字媒体(2)：多媒体》课程中音频小课堂大作业-人脸美化任务
    
## TODO
    - Android
        - 在Face++ API没有完成请求之前不允许UI操作，或者说显示加载中（或者正在处理图片）
    - 美白 ×
    - 磨皮 完成(?)
        - [图像算法---磨皮算法研究汇总](https://blog.csdn.net/trent1985/article/details/50496969)
        - [opencv 美白磨皮人脸检测](https://blog.csdn.net/zhangqipu000/article/details/53260647)
        - 实现了基于双边滤波的磨皮，但是速度过慢，而且效果没有那么好（100的要15秒）
        - [HighPassSkinSmoothing-Android](https://github.com/msoftware/HighPassSkinSmoothing-Android)
        - 速度很快，基本满足要求
    - 大眼 ×
    - 瘦脸 ×
    - 口红 ×
    
## TIPS
    - 不要局限于opencv的处理方式，可以考虑android的convas，可以直接在bitmap上绘制
    
    
## 难点
    使用了Java的Opencv接口，而网上的教程基本都是Python和C++版的，API的名称和调用方法也不太一样，这方面花费了不少时间
