package net.sourceforge.simcpux.consts;

/**
 * *****************************************************************************
 * 作者： woozy
 * 开发日期： 2016/9/30.
 * 模块功能：
 * *****************************************************************************
 */
public class NetWorkConst {
    //	public static final String IP_PORT = "http://219.159.71.141:8081/citypay_om";//正式环境
    public static final String IP_PORT = "http://110.80.22.108:18080";//测试环境
//    public static final String IP_PORT = "http://10.8.3.87:8080";//测试环境
    //	public static final String IP_PORT = "http://10.8.3.137:8081/citypaay_om";//昌日服务端地址
    public static final String URL = IP_PORT+ "/xmic-web/interfaces/";

    /**
     * result："01"表示失败，result:"00"表示成功
     */
    public static final String RESULT_SUCCESS = "00";

    /**
     * result："01"表示失败，result:"00"表示成功
     */
    public static final String RESULE_FAIL = "01";
    /**
     * result："01"表示失败，result:"00"表示成功 ,result:"02"表示未知
     */

    // --------------------响应标识代码-----------------------
    /** 网络数据获取成功 */
    public static final int RESPONSE_SUCCESS = 200;
}
