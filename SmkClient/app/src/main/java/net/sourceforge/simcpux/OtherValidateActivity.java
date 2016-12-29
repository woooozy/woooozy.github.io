package net.sourceforge.simcpux;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;

import net.sourceforge.simcpux.base.BaseActivity;
import net.sourceforge.simcpux.consts.AppConst;
import net.sourceforge.simcpux.consts.OperateTypeConst;
import net.sourceforge.simcpux.linstener.HttpCallBack;
import net.sourceforge.simcpux.linstener.SendService;
import net.sourceforge.simcpux.manager.CardManager;
import net.sourceforge.simcpux.manager.NfcCardOperateManager;
import net.sourceforge.simcpux.manager.RechargeManager;
import net.sourceforge.simcpux.utils.DialogUtils;
import net.sourceforge.simcpux.utils.MessageEvent;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import me.fantouch.libs.log.L;

public class OtherValidateActivity extends BaseActivity {
    private static final String TAG = "OtherValidateActivity";
    private final static int OTHERLIMITED = 0x00;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private NfcCardOperateManager nfcCardManager;
    String[] resp8050, cardType, NoAndVali = null;
    String file0005 = "";
    String file0015 = "";
    String file001A = "";
    String file8050 = "";
    String orderCode = "";
    String cardPan = "";
    String random = "";
    private Dialog noticeDialog;
    String transIndex = "";
    @Override
    public int setResid() {
        return R.layout.activity_other_validate;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, CardManager.FILTERS, CardManager.TECHLISTS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        final Parcelable p = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Logger.e("p:" + p);
        Tag tag = (Tag) p;
        IsoDep isodep = IsoDep.get(tag);
        nfcCardManager = new NfcCardOperateManager(isodep);
        new Thread(Runnable).start();
    }

    Runnable Runnable = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                if (cardPan.length() == 19) {////////////交通部卡/////////////////////
                    // 余额
                    Logger.e("读取交通部卡的文件！！！！！！！！！！！！！！！！！！！！！");
                    file0005 = nfcCardManager.getOOO5File(AppConst.APP_MF_JTB);
                    file0015 = nfcCardManager.getOO15File(AppConst.APP_MF_JTB);
                    file001A = nfcCardManager.getOO1AFile(AppConst.APP_MF_JTB);
                } else {////////////CPU卡/////////////////////
                    Logger.e("读取易通卡CPU卡文件！！！！！！！！！！！！！！！");
                    file0005 = nfcCardManager.getOOO5File(AppConst.APP_MF);
                    file0015 = nfcCardManager.getOO15File(AppConst.APP_ADF1);
                }
                random = nfcCardManager.RamdomNum();
                EventBus.getDefault().post(new MessageEvent(OTHERLIMITED, null));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEvent(MessageEvent event) {
        switch (event.handlerType) {
            case OTHERLIMITED:
                    RechargeManager.otherValidateQuery(file0005,file0015,AppConst.ATS,random,cardPan,10,1,httpCallBack,OtherValidateActivity.this);

                break;
            default:
                break;
        }

    }

    private HttpCallBack httpCallBack = new HttpCallBack() {

        @Override
        public void onSuccess(JSONObject jObject, String operType) {
            switch (operType) {
                case OperateTypeConst.TYPE_RECHARGE_LIST_OTHER:
                    JSONArray data=jObject.getJSONArray("list");
                    orderCode=data.getJSONObject(0).getString("ordercode");
                    RechargeManager.update_other_validate(orderCode,cardPan,file0005,file0015,random,httpCallBack,OtherValidateActivity.this);
                    break;
                case  OperateTypeConst.TYPE_UPDATE_VALIDATE_OTHER:
                    String apdu=jObject.getString("apdu");
                    try {
                        if (!TextUtils.equals(apdu,"9000")){
                           String tac= nfcCardManager.limited_yxrq(apdu);
                            if (tac.endsWith("9000")) {
                            } else {
                                Logger.e("有效期修改失败！！！！！！！！！！" + tac);
                            }
                        }
                        file8050 = nfcCardManager.get8050File(jObject.getString("prepare_apdu"));
                        transIndex = file8050.substring(8, 12);
                        int a = Integer.valueOf(transIndex, 16);
                        a += 1;
                        transIndex = Integer.toHexString(a);
                        while (transIndex.length() < 4) {
                            transIndex = "0" + transIndex;
                        }
                        RechargeManager.recharge_order(orderCode, file0005, file0015, file8050, httpCallBack, OtherValidateActivity.this);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onFail(int statusCode, String msg, String operType) {
            noticeDialog = DialogUtils.getNoticeDialog(OtherValidateActivity.this, msg, "确定", null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    L.sendLogFiles(OtherValidateActivity.this, SendService.class, AppConst.phoneNum);
                    noticeDialog.dismiss();
                }
            }, null);
            noticeDialog.show();
        }
    };
    @Override
    public void initLayout() {
        EventBus.getDefault().register(this);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        cardPan = getIntent().getStringExtra("cardpan");
    }

    @Override
    public void setBack() {
        this.finish();
    }
}
