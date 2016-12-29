package net.sourceforge.simcpux.linstener;

import java.io.File;
import java.io.FileNotFoundException;

import android.util.Log;

import me.fantouch.libs.reporter.AbsSendFileService;
import me.fantouch.libs.reporter.NotificationHelper;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class SendService extends AbsSendFileService {

    public static String URL = "http://110.80.22.108:18080/xmic-web/aapp/logname";// 测试3G

    //public static String URL = "http://172.16.35.232:8080/xmic-web/aapp/logname";// 测试wifi
    //public static String URL = "http://218.5.80.23:18080/xmic-web/aapp/logname";// 厦门生产环境
    @Override
    public void sendToServer(File reportsZip, NotificationHelper notification) {
        AjaxParams params = new AjaxParams();
        try {

            params.put("telephone", telephone);
            params.put("reportsZip", reportsZip);
            params.put("logname", telephone);
            //手机号码
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        FinalHttp fh = new FinalHttp();
        fh.post(URL, params, new AjaxCallBack<String>() {

            @Override
            public boolean isProgress() {
                // TODO Auto-generated method stub
                return super.isProgress();
            }

            @Override
            public AjaxCallBack<String> progress(boolean progress, int rate) {
                // TODO Auto-generated method stub
                return super.progress(progress, rate);
            }

            @Override
            public void onLoading(long count, long current) {
                // TODO Auto-generated method stub
                super.onLoading(count, current);
            }

            @Override
            public void onSuccess(String t) {
                // TODO Auto-generated method stub
                super.onSuccess(t);
                stopSelf();// 无论成功还是失败,都应该停止服务
                Log.e("SendService", "t = " + t);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                // TODO Auto-generated method stub
                super.onFailure(t, errorNo, strMsg);
                stopSelf();// 无论成功还是失败,都应该停止服务
            }

        });
    }

}

