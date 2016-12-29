package net.sourceforge.simcpux.asyHttp;

import java.net.URLDecoder;

import com.alibaba.fastjson.JSONObject;

/**
 * 自己定义的结果的处理 天喻信息服务端的 处理方式
 *
 */
public class ResponseDeal implements Response {

	@Override
	public Result deal(JSONObject json) {
		Result result = new Result();
		try {
			String res = json.getString("return_code");
			String description="";
			if (res.equals(RESULT_OK)) {
				description = json.getString("return_msg");
				result.success = true;
			} else if (res.equals(RESULT_FAIL)) {
				description = json.getString("err_code_des");
				result.success = false;
			}
			result.return_msg = URLDecoder.decode(description,"UTF-8");
			json.put("return_msg", result.return_msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	class Result {
		public boolean success;
		public String return_msg;

	}

}
