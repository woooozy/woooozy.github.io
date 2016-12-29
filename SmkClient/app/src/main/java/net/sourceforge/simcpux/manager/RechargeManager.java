package net.sourceforge.simcpux.manager;

import android.content.Context;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import net.sourceforge.simcpux.asyHttp.HttpTools;
import net.sourceforge.simcpux.consts.AppConst;
import net.sourceforge.simcpux.consts.NetWorkConst;
import net.sourceforge.simcpux.consts.OperateTypeConst;
import net.sourceforge.simcpux.linstener.HttpCallBack;

/**
 * *****************************************************************************
 * 作者： woozy
 * 开发日期： 2016/10/26.
 * 模块功能：充值接口管理类
 * *****************************************************************************
 */
public class RechargeManager {
    static HttpTools tools;

    /*
    * 2601 年审补登订单查询
    * */
    public static void limited_Query(String data0005, String data0015, String cardRan,HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", OperateTypeConst.TYPE_LIMITED_QUERY);
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
            jsonObject.put("ats", AppConst.ATS);
            jsonObject.put("data0005", data0005.toUpperCase());
            jsonObject.put("data0015", data0015.toUpperCase());
            jsonObject.put("cardRan", cardRan.toUpperCase());
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.TYPE_LIMITED_QUERY, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }

    /*
    * 2602 年审补登
    * */
    public static void limited(String data001A,String data0005, String data0015,String ats, String cardRan,String cardNo,String validate,HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", OperateTypeConst.TYPE_LIMITED);
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
            jsonObject.put("ats", AppConst.ATS);
            jsonObject.put("data0005", data0005.toUpperCase());
            jsonObject.put("data0015", data0015.toUpperCase());
            jsonObject.put("ats", ats);
            jsonObject.put("cardRan", cardRan.toUpperCase());
            jsonObject.put("cardNo",cardNo);
            jsonObject.put("validate",validate);
            jsonObject.put("data001A",data001A);
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.TYPE_LIMITED, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }

    /*
    * 2603 修改卡片有效期和返回8050圈存准备的指令
    * */
    public static void update_validate(String type, String orderCode, String data0005, String data0015, String cardRan, String cardpan, String cityCode, HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", type);
            jsonObject.put("ats", AppConst.ATS);
            jsonObject.put("cardRan", cardRan.toUpperCase());
            jsonObject.put("cardpan", cardpan);
            jsonObject.put("data0005", data0005.toUpperCase());
            jsonObject.put("data0015", data0015.toUpperCase());
            jsonObject.put("loginname", AppConst.phoneNum);
            jsonObject.put("ordercode", orderCode);
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
//            jsonObject.put("cityCode",cityCode);
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.TYPE_UPDATE_VALIDATE, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }

    /*
    * 2604 年审补登确认
    * */
    public static void limited_confirm(String cardNo,String data001A,String data0005, String data0015, String cardRan, HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", OperateTypeConst.TYPE_UPDATE_VALIDATE_CONFIRM);
            jsonObject.put("ats", AppConst.ATS);
            jsonObject.put("cardRan", cardRan.toUpperCase());
            jsonObject.put("data0005", data0005.toUpperCase());
            jsonObject.put("data0015", data0015.toUpperCase());
            jsonObject.put("data001A", data001A.toUpperCase());
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("cardNo", cardNo);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
//            jsonObject.put("cityCode",cityCode);
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.TYPE_UPDATE_VALIDATE_CONFIRM, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }

    /*
    * 2605 他人补登修改有效期
    * */
    public static void update_other_validate( String orderCode, String cardPan,String data0005, String data0015, String cardRan,HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", OperateTypeConst.TYPE_UPDATE_VALIDATE_OTHER);
            jsonObject.put("ats", AppConst.ATS);
            jsonObject.put("cardRan", cardRan.toUpperCase());
            jsonObject.put("data0005", data0005.toUpperCase());
            jsonObject.put("cardpan", cardPan);
            jsonObject.put("data0015", data0015.toUpperCase());
            jsonObject.put("ordercode", orderCode);
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.TYPE_UPDATE_VALIDATE_OTHER, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }


    /*
    * 2701 创建充值订单
    * */
    public static void create_order(String cardpan, int money, HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", OperateTypeConst.TYPE_CREATE_BOARDORDER);
            jsonObject.put("login_name", AppConst.phoneNum);
            jsonObject.put("money", money);
            jsonObject.put("cardpan", cardpan);
            jsonObject.put("paychanel", "0301");
            jsonObject.put("channel", "APP");
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
            jsonObject.put("paymentSsn", AppConst.paymentSsn);
            jsonObject.put("accountNo", AppConst.accountNo);
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.TYPE_CREATE_BOARDORDER, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }

    /*
    * 2702 订单查询借口
    * */
    public static void recharge_list(String orderCode, String cardpan, int pagesize, int pageno, String channel, HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", OperateTypeConst.TYPE_RECHARGE_LIST);
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
            jsonObject.put("login_name", AppConst.phoneNum);
            jsonObject.put("cardpan", cardpan);
            jsonObject.put("ordercode", orderCode);
            jsonObject.put("pagesize", pagesize);
            jsonObject.put("pageno", pageno);
            jsonObject.put("channel", channel);
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.TYPE_RECHARGE_LIST, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }

    /*
    * 2703 卡片充值接口，返回8052指令进行写卡
    * */
    public static void recharge_order(String orderCode, String data0005, String data0015, String dataResult, HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", OperateTypeConst.TYPE_RECHARGE_BOARDORDER);
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
            jsonObject.put("ordercode", orderCode);
            jsonObject.put("ats", AppConst.ATS);
            jsonObject.put("data0005", data0005.toUpperCase());
            jsonObject.put("data0015", data0015.toUpperCase());
            jsonObject.put("dataResult", dataResult.toUpperCase());
            jsonObject.put("login_name", AppConst.phoneNum);
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.TYPE_RECHARGE_BOARDORDER, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }

    /*
    * 2704 充值确认借口
    * */
    public static void recharge_confirm(String orderCode, String data0005, String data0015, String status, HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", OperateTypeConst.TYPE_RECHARGE_CONFIRM);
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
            jsonObject.put("data0005", data0005.toUpperCase());
            jsonObject.put("data0015", data0015.toUpperCase());
            jsonObject.put("ordercode", orderCode);
            jsonObject.put("status", status);
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.TYPE_RECHARGE_CONFIRM, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }

    /*
    * 2705 订单撤销借口
    * */
    public static void recharge_cancel(String orderCode, HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", OperateTypeConst.TYPE_RECHARGE_CANCEL);
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
            jsonObject.put("ordercode", orderCode);
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.TYPE_RECHARGE_CANCEL, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }

    /*
    * 2706 订单状态查询接口
    * */
    public static void recharge_select_status(String orderCode, HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", OperateTypeConst.TYPE_RECHARGE_SELECT);
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
            jsonObject.put("ordercode", orderCode);
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.TYPE_RECHARGE_SELECT, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }

    /*
    * 2707 修改订单状态接口
    *
    * 订单状态
    *00：创建状态
    *01：支付中
    *02：撤消中
    *03：退款中
    *10：完成
    *99：撤消
    *88：已退款
    * */
    public static void recharge_update(String orderCode, String orderstatus, HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", OperateTypeConst.TYPE_RECHARGE_UPDATE);
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
            jsonObject.put("ordercode", orderCode);
            jsonObject.put("orderstatus", orderstatus);
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.TYPE_RECHARGE_UPDATE, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }

    /*
    * 2709 他人补登充值订单查询借口
    * */
    public static void otherValidateQuery(String data0005, String data0015,String ats,String cardRan, String cardpan, int pagesize, int pageno, HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", OperateTypeConst.TYPE_RECHARGE_LIST_OTHER);
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
            jsonObject.put("login_name", AppConst.phoneNum);
            jsonObject.put("data0005", data0005.toUpperCase());
            jsonObject.put("data0015", data0015.toUpperCase());
            jsonObject.put("ats", ats);
            jsonObject.put("cardRan", cardRan.toUpperCase());
            jsonObject.put("cardpan", cardpan);
            jsonObject.put("pagesize", pagesize);
            jsonObject.put("pageno", pageno);
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.TYPE_RECHARGE_LIST_OTHER, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }

}
