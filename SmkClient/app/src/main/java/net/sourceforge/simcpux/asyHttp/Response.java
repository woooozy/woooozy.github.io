package net.sourceforge.simcpux.asyHttp;

import com.alibaba.fastjson.JSONObject;

public interface Response {
	public static final String RESULT_OK = "SUCCESS";
	public static final String RESULT_FAIL = "FAIL";
	public static final int ERROR_CODE_RESPONSE = 9000;
	public static final int ERROR_CODE_RESPONSE_EMPTY = 9001;
	public static final int ERROR_CODE_RESPONSE_EXCEPTION = 9002;

	ResponseDeal.Result deal(JSONObject json);

}
