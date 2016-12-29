package net.sourceforge.simcpux.cardbag.model.budeng;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.orhanobut.logger.Logger;

import net.sourceforge.simcpux.R;
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

/*
* 年审不登页面
* */
public class ValidateActivity extends BaseActivity {
    private static final String TAG = "ValidateActivity";
    private final static int LIMITED = 0x00;
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
                    random = nfcCardManager.RamdomNum2();
                } else {////////////CPU卡/////////////////////
                    Logger.e("读取易通卡CPU卡文件！！！！！！！！！！！！！！！");
                    file0005 = nfcCardManager.getOOO5File(AppConst.APP_MF);
                    file0015 = nfcCardManager.getOO15File(AppConst.APP_ADF1);
                    random = nfcCardManager.RamdomNum();
                }
                EventBus.getDefault().post(new MessageEvent(LIMITED, null));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    };

    private HttpCallBack httpCallBack = new HttpCallBack() {

        @Override
        public void onSuccess(JSONObject jObject, String operType) {
            switch (operType) {
                case OperateTypeConst.TYPE_LIMITED:
                    try {
                        String returnMsg = nfcCardManager.limited_yxrq(jObject.getString("apdu").toUpperCase());
                        if (TextUtils.equals(returnMsg, "9000")) {
                            RechargeManager.limited_confirm(cardPan, file001A,file0005, file0015, random, httpCallBack, ValidateActivity.this);
                        } else {
                            noticeDialog = DialogUtils.getNoticeDialog(ValidateActivity.this, "年审补登失败！", "确定", null, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    L.sendLogFiles(ValidateActivity.this, SendService.class, AppConst.phoneNum);
                                    noticeDialog.dismiss();
                                }
                            }, null);
                            noticeDialog.show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case OperateTypeConst.TYPE_UPDATE_VALIDATE_CONFIRM:
                    noticeDialog = DialogUtils.getNoticeDialog(ValidateActivity.this, "年审补登成功", "确定", null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            L.sendLogFiles(ValidateActivity.this, SendService.class, AppConst.phoneNum);
                            noticeDialog.dismiss();
                        }
                    }, null);
                    noticeDialog.show();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onFail(int statusCode, String msg, String operType) {
            noticeDialog = DialogUtils.getNoticeDialog(ValidateActivity.this, msg, "确定", null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    L.sendLogFiles(ValidateActivity.this, SendService.class, AppConst.phoneNum);
                    noticeDialog.dismiss();
                }
            }, null);
            noticeDialog.show();
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEvent(MessageEvent event) {
        switch (event.handlerType) {
            case LIMITED:
                if (cardPan.length() == 19) {////////////交通部卡/////////////////////
                    RechargeManager.limited(file001A, file0005, file0015, AppConst.ATS, random, cardPan, file001A.substring(22, 30), httpCallBack, ValidateActivity.this);
                } else {////////////CPU卡/////////////////////
                    RechargeManager.limited(file001A, file0005, file0015, AppConst.ATS, random, cardPan, "", httpCallBack, ValidateActivity.this);
                }
                break;
            default:
                break;
        }

    }

    @Override
    public int setResid() {
        return R.layout.activity_limited;
    }

    @Override
    public void initLayout() {
        EventBus.getDefault().register(this);
        toolbar_title.setText(R.string.title_recharge);

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
