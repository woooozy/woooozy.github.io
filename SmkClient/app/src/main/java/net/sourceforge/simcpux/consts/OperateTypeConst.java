package net.sourceforge.simcpux.consts;

/**
 * *****************************************************************************
 * 作者： woozy
 * 开发日期： 2016/9/30.
 * 模块功能：
 * *****************************************************************************
 */
public class OperateTypeConst {
    /**
     * 表示当前为注册操作
     */
    public static final String TYPE_REGIST = "CP1";
    /**
     * 表示当前为登录操作
     */
    public static final String TYPE_LOGIN = "CP2";

    /**
     * 卡的微信支付调微信的请求
     */
    public static final String TYPE_WEIXIN = "CP41";
    /*
    * 补登充值
    * */
    public static final String TYPE_LIMITED_QUERY = "2601";
    public static final String TYPE_LIMITED = "2602";
    public static final String TYPE_UPDATE_VALIDATE = "2603";
    public static final String TYPE_UPDATE_VALIDATE_CONFIRM = "2604";
    public static final String TYPE_UPDATE_VALIDATE_OTHER = "2605";
    public static final String TYPE_CREATE_BOARDORDER = "2701";
    public static final String TYPE_RECHARGE_LIST = "2702";
    public static final String TYPE_RECHARGE_BOARDORDER = "2703";
    public static final String TYPE_RECHARGE_CONFIRM = "2704";
    public static final String TYPE_RECHARGE_CANCEL = "2705";
    public static final String TYPE_RECHARGE_SELECT = "2706";
    public static final String TYPE_RECHARGE_UPDATE = "2707";
    public static final String TYPE_RECHARGE_LIST_OTHER = "2709";
    /*
    * 联机账户
    * */
    public static final String ONLINEACCOUNT_SELECT = "2312";
    public static final String ONLINEACCOUNT_PAY = "2310";
    public static final String ONLINEACCOUNT_PAY_CANCEL = "2313";
}
