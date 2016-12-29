package net.sourceforge.simcpux.asyHttp;

import java.net.URLDecoder;

import org.apache.http.Header;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.protocol.HTTP;

import android.content.Context;

import com.alibaba.fastjson.JSONObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import net.sourceforge.simcpux.linstener.HttpCallBack;

import com.orhanobut.logger.Logger;

public class HttpTools {
	private AsyncHttpClient asyncHttpClient;
	private HttpCallBack httpCallback;
	private String operType;
	private Context context;
	public HttpTools(HttpCallBack httpCallback,String operType,Context context) {
		asyncHttpClient = new AsyncHttpClient();
		this.httpCallback = httpCallback;
		this.operType = operType;
		this.context = context;
	}

	public void sendRequest(String url, String data) {
//		RequestParams params = new RequestParams("data", data);
//		httpHead(params, phone, encryptFlag);
		Logger.e("连接的服务端地址--------->"+ url);
		Logger.e("发送的数据--------->"+data);
		asyncHttpClient.setTimeout(30000);// 设置超时时间
		asyncHttpClient.addHeader("Content-Type", "application/json");
		asyncHttpClient.addHeader("charset", HTTP.UTF_8);

		ByteArrayEntity myEntity = new ByteArrayEntity(data.getBytes());
		asyncHttpClient.post(context, url, myEntity, "application/json", new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				Logger.e("post request onStart...");
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				Logger.e("HttpTools--->statusCode:" + statusCode);
				if (statusCode == 200) {
					try {
						String res = URLDecoder.decode(new String(responseBody), "UTF-8");
						JSONObject jObject = JSONObject.parseObject(res);
						Logger.e("返回的数据---------->"+jObject.toString());
						Response response = new ResponseDeal();
						ResponseDeal.Result result = response.deal(jObject);

						if (result.success) {
							httpCallback.onSuccess(jObject,operType);
						} else {
							httpCallback.onFail(Response.ERROR_CODE_RESPONSE,
									result.return_msg,operType);
						}
					} catch (Exception e) {
						e.printStackTrace();
						httpCallback.onFail(
								Response.ERROR_CODE_RESPONSE_EXCEPTION,
								"数据格式错误,请稍后重试~",operType);
					}
				}
			}

			@Override
			public void onFailure(int statusCode, Header[] headers, byte[] bytes, Throwable throwable) {
				Logger.e("HttpTools--->post request onFailure...statusCode"+statusCode);
				try {
					httpCallback.onFail(statusCode, "网络异常，请检查网络..",operType);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
					Logger.e("HttpTools--->post request onFinish...");
			}
		});

	}
//
//	private void httpHead(RequestParams params, String phone,
//			boolean encryptFlag) {
//		params.put("phone", phone);
//		params.put("encrypt", encryptFlag);
//	}

//	private static String exception(Throwable t) throws IOException {
//		if (t == null)
//			return null;
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		try {
//			t.printStackTrace(new PrintStream(baos));
//		} finally {
//			baos.close();
//		}
//		return baos.toString();
//	}
}
