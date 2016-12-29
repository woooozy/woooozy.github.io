package net.sourceforge.simcpux.linstener;

import com.alibaba.fastjson.JSONObject;


public interface HttpCallBack {
	public void onSuccess(JSONObject jObject, String operType);

	public void onFail(int statusCode, String msg, String operType);
}
