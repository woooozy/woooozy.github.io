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
 * 开发日期： 2016/11/11.
 * 模块功能：
 * *****************************************************************************
 */
public class OnlineAccManager {
    static HttpTools tools;
    /*
    * 2312 	联机账户余额查询接口
    * */
    public static void OnlineSelect(String account, String password,HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", OperateTypeConst.ONLINEACCOUNT_SELECT);
            jsonObject.put("account", account);
            jsonObject.put("password",password);
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.ONLINEACCOUNT_SELECT, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }
    /*
    * 2310 	联机账户支付接口
    * */
    public static void Online_Pay(String account, String password,int money,HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", OperateTypeConst.ONLINEACCOUNT_PAY);
            jsonObject.put("account", account);
            jsonObject.put("password",password);
            jsonObject.put("money",money);
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.ONLINEACCOUNT_PAY, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }
    /*
    * 2313 	联机账户支付撤销接口
    * */
    public static void Online_Pay_Cancel(String account, String password,int money,String merchid,String termno,String flowNo,String batchno,HttpCallBack httpCallBack, Context Context) {
        JSONObject jsonObject = new JSONObject();
        String params = "";
        try {
            jsonObject.put("type", OperateTypeConst.ONLINEACCOUNT_PAY_CANCEL);
            jsonObject.put("account", account);
            jsonObject.put("password",password);
            jsonObject.put("money",money);
            jsonObject.put("merchid",merchid);
            jsonObject.put("termno",termno);
            jsonObject.put("flowNo",flowNo);
            jsonObject.put("batchno",batchno);
            jsonObject.put("unit_code", AppConst.unit_code);
            jsonObject.put("imei", AppConst.imei);
            jsonObject.put("msg_id", AppConst.msg_id);
            params = jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        tools = new HttpTools(httpCallBack, OperateTypeConst.ONLINEACCOUNT_PAY_CANCEL, Context);
        tools.sendRequest(NetWorkConst.URL, params);
    }
}
