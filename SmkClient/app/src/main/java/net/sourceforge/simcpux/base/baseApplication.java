package net.sourceforge.simcpux.base;

import android.app.Application;


/**
 * *****************************************************************************
 * 作者： woozy
 * 开发日期： 2016/10/19.
 * 模块功能：
 * *****************************************************************************
 */
public class baseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        // 创建默认的ImageLoader配置参数
        //注意：在SDK各功能组件使用之前都需要调用
        //SDKInitializer.initialize(getApplicationContext());，因此我们建议该方法放在Application的初始化方法中
/*        SDKInitializer.initialize(getApplicationContext());*/
    }
}
