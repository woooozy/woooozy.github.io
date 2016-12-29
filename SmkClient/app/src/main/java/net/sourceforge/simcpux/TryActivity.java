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

import com.alibaba.fastjson.JSONObject;

import net.sourceforge.simcpux.base.BaseActivity;
import net.sourceforge.simcpux.consts.AppConst;
import net.sourceforge.simcpux.consts.OperateTypeConst;
import net.sourceforge.simcpux.linstener.HttpCallBack;
import net.sourceforge.simcpux.linstener.SendService;
import net.sourceforge.simcpux.manager.CardManager;
import net.sourceforge.simcpux.manager.NfcCardOperateManager;
import com.orhanobut.logger.Logger;
import net.sourceforge.simcpux.manager.RechargeManager;
import net.sourceforge.simcpux.utils.DialogUtils;
import net.sourceforge.simcpux.utils.ToastUtil;

import me.fantouch.libs.log.L;

public class TryActivity extends BaseActivity {
    private static final String TAG = "TryActivity";
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private NfcCardOperateManager nfcCardManager;

    private String cardNumber;
    private String transId;
    private String orderCode;
    private String Balance;
    private String file0005;
    private String file0015;
    private Double successMoney;
    String response;
    private Dialog noticeDialog;

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null)
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, CardManager.FILTERS, CardManager.TECHLISTS);
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
            //TODO Auto-generated method
            if(TextUtils.isEmpty(cardNumber) ||
                    TextUtils.isEmpty(transId) ||
                    TextUtils.isEmpty(orderCode) ||
                    TextUtils.isEmpty(""+successMoney) ){
//                ToastUtil.showToast(TryActivity.this,"参数异常");
                Logger.e("参数异常");
                return;
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Logger.e("走防拔的指令了");
                    tryRealInCardFromTranId(transId);
                }
            });
        }

    };

    @Override
    public int setResid() {
        return R.layout.activity_try;
    }

    @Override
    public void initLayout() {
        toolbar_title.setText(R.string.title_query);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        cardNumber =  getIntent().getStringExtra("cardNumber");
        orderCode = getIntent().getStringExtra("orderCode");
        Balance = getIntent().getStringExtra("Balance");
        transId =getIntent().getStringExtra("transId");
        file0005 = getIntent().getStringExtra("file0005");
        file0015 = getIntent().getStringExtra("file0015");
        successMoney =getIntent().getDoubleExtra("successMoney",0);

    }

    private HttpCallBack httpCallBack=new HttpCallBack() {

        @Override
        public void onSuccess(JSONObject jObject, String operType) {
            switch (operType) {
                case  OperateTypeConst.TYPE_RECHARGE_CONFIRM:
                    noticeDialog= DialogUtils.getNoticeDialog(TryActivity.this,response, "确定", null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            noticeDialog.dismiss();
                            finish();
                        }
                    },null);
                    noticeDialog.setCancelable(false);
                    noticeDialog.show();
                    break;
                default:break;
            }
        }

        @Override
        public void onFail(int statusCode, String msg, String operType) {
            noticeDialog = DialogUtils.getNoticeDialog(TryActivity.this, msg, "确定", null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noticeDialog.dismiss();
                }
            }, null);
            noticeDialog.show();
        }
    };
    /**
     * 通过序号检测是否真的成功
     */
    private void tryRealInCardFromTranId(String transId) {
        // 做防拔指令
        String jylx = "02";
        if (TextUtils.isEmpty(transId)) {
            ToastUtil.showToast(TryActivity.this, "充值流水序号异常，异常操作");
            return;
        }else {
            try {
                if (cardNumber.length()==19){
                    response= nfcCardManager.fangBa(AppConst.APP_MF_JTB,jylx,transId,successMoney+"",Balance);
                }else {
                    response=  nfcCardManager.fangBa(AppConst.APP_ADF1,jylx,transId,successMoney+"",Balance);
                }
                ToastUtil.showToast(TryActivity.this, response);
                L.sendLogFiles(TryActivity.this, SendService.class,AppConst.phoneNum);
                String status="";
                if (TextUtils.equals(response,"补登充值成功")){
                    status="1";
                }else {
                    status="0";
                }
                RechargeManager.recharge_confirm(orderCode,file0005,file0015,status,httpCallBack,TryActivity.this);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setBack() {
        this.finish();
    }


}
