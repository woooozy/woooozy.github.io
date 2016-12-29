package net.sourceforge.simcpux.utils;

/**
 * *****************************************************************************
 * 作者： woozy
 * 开发日期： 2016/10/17.
 * 模块功能：
 * *****************************************************************************
 */
public class MessageEvent  {
    public int handlerType;
    public Object message;
    public MessageEvent(int handlerTyep,Object message){
        this.handlerType=handlerTyep;
        this.message=message;
    }
}
