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

import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.TryActivity;
import net.sourceforge.simcpux.base.BaseActivity;
import net.sourceforge.simcpux.consts.AppConst;
import net.sourceforge.simcpux.consts.OperateTypeConst;
import net.sourceforge.simcpux.linstener.HttpCallBack;
import net.sourceforge.simcpux.linstener.SendService;
import net.sourceforge.simcpux.manager.CardManager;
import net.sourceforge.simcpux.manager.NfcCardOperateManager;
import net.sourceforge.simcpux.manager.RechargeManager;
import net.sourceforge.simcpux.utils.CommonUtils;
import net.sourceforge.simcpux.utils.DialogUtils;
import net.sourceforge.simcpux.utils.MessageEvent;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import java.io.IOException;

import me.fantouch.libs.log.L;


public class BoardChargeActivity extends BaseActivity {
    private static final String TAG = "BoardChargeActivity";
    private final static int UPDATEVALID = 0x00;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private NfcCardOperateManager nfcCardManager;

    String[] resp8050, cardType, NoAndVali = null;
    String file0005 = "";
    String file0015 = "";
    String file8050 = "";
    String orderCode = "";
    String loginname = "";
    String cardPan = "";
    String random = "";
    String Balance = "";//
    String cityCode = "";
    String type = "";
    int other;
    private Double successMoney;
    private String amount ="1";

    String[] res0017 = null;
    private Dialog noticeDialog, fangbaDialog;

    String transIndex = "";

    @Override
    public int setResid() {
        return R.layout.activity_pay_finish;
    }

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
                    Balance = nfcCardManager.getCardBalance(AppConst.APP_MF_JTB);

                    // 卡号
                    res0017 = nfcCardManager.MuitiTradeRecord(AppConst.APP_MF_JTB);

                    file0005 = nfcCardManager.getOOO5File(AppConst.APP_MF_JTB);
                    file0015 = nfcCardManager.getOO15File(AppConst.APP_MF_JTB);
                } else {////////////CPU卡/////////////////////
                    file0005 = nfcCardManager.getOOO5File(AppConst.APP_MF);
                    file0015 = nfcCardManager.getOO15File(AppConst.APP_ADF1);

                    Balance = nfcCardManager.getCardBalance(AppConst.APP_ADF1);
                }

                //============================充值之前的金额上限的判断===================================================
                Double availMoney = AppConst.MAXQCMONEY - Double.parseDouble(Balance);
                String availMoneyDouble = CommonUtils.formatDouble(availMoney);
                availMoney = Double.parseDouble(availMoneyDouble);
                Double amountMoney = Double.parseDouble(amount);
                successMoney = Double.parseDouble(Balance) +amountMoney;
                if(amountMoney.compareTo(availMoney)>0){
                    noticeDialog = DialogUtils.getNoticeDialog(BoardChargeActivity.this,
                            "补登充值失败" + "输入的金额不能超过可充值金额！", "确定", null, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            }, null);
                    noticeDialog.setCancelable(false);
                    noticeDialog.show();
                    return;
                }
                random = nfcCardManager.RamdomNum();
                    EventBus.getDefault().post(new MessageEvent(UPDATEVALID, null));
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
                case OperateTypeConst.TYPE_RECHARGE_BOARDORDER:
                    try {
                        String Tac = nfcCardManager.boardResult8052(jObject.getString("apdu"));
                        String status;
                        if (Tac.endsWith("9000")) {
                            status = "1";
                            RechargeManager.recharge_confirm(orderCode, file0005, file0015, status, httpCallBack, BoardChargeActivity.this);
                        } else {
                            status = "0";
                            ShowDialogToFangba(transIndex);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case OperateTypeConst.TYPE_UPDATE_VALIDATE:
                    Logger.e(jObject.toJSONString());
                    writeCard(jObject);
                    break;
                case OperateTypeConst.TYPE_UPDATE_VALIDATE_OTHER:
                    Logger.e(jObject.toJSONString());
                    writeCard(jObject);
                    break;
                case OperateTypeConst.TYPE_RECHARGE_CONFIRM:
                    Logger.e("充值成功后的金额 "+successMoney);
                    Logger.e("2704返回值-------->" + jObject.toString());
                    noticeDialog = DialogUtils.getNoticeDialog(BoardChargeActivity.this, "充值成功！", "确定", null, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            L.sendLogFiles(BoardChargeActivity.this, SendService.class,AppConst.phoneNum);
                            noticeDialog.dismiss();
                            finish();
                        }
                    }, null);
                    noticeDialog.show();
                    break;
                case OperateTypeConst.TYPE_RECHARGE_LIST_OTHER:
                    orderCode=jObject.getJSONArray("list").getJSONObject(0).getString("ordercode");
                    RechargeManager.update_validate(type, orderCode, file0005, file0015, random, cardPan, cityCode, httpCallBack, BoardChargeActivity.this);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onFail(int statusCode, String msg, String operType) {
            noticeDialog = DialogUtils.getNoticeDialog(BoardChargeActivity.this, msg, "确定", null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    L.sendLogFiles(BoardChargeActivity.this, SendService.class,AppConst.phoneNum);
                    noticeDialog.dismiss();
                }
            }, null);
            noticeDialog.show();
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEvent(MessageEvent event) {
        switch (event.handlerType) {
            case UPDATEVALID:

                if (cardPan.length() == 19) {
                    cityCode = res0017[0];
                } else {
                    cityCode = file0005.substring(4, 8);
                }
                RechargeManager.update_validate(type, orderCode, file0005, file0015, random, cardPan, cityCode, httpCallBack, BoardChargeActivity.this);
                break;

            default:
                break;
        }
    }
    @Override
    public void initLayout() {
        EventBus.getDefault().register(this);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        cardPan = getIntent().getStringExtra("cardpan");
        orderCode = getIntent().getStringExtra("orderCode");
        loginname = getIntent().getStringExtra("loginName");
/*        cardPan = "3105060801000000028";
        orderCode = "20161226163336000000000000015634";
        loginname = "18600000000";*/
        AppConst.phoneNum=loginname;

        other = getIntent().getIntExtra("other",0);
        if (other == 0) {
            type = OperateTypeConst.TYPE_UPDATE_VALIDATE;
            toolbar_title.setText("贴卡卟噔");
        } else if (other == 1) {
            type = OperateTypeConst.TYPE_UPDATE_VALIDATE_OTHER;
            toolbar_title.setText("为他人卟噔");
        }

    }

    @Override
    public void setBack() {
        this.finish();
    }

    private void writeCard(JSONObject jObject){
        try {
            if (!TextUtils.equals(jObject.getString("apdu"), "9000")) {
                String tac = nfcCardManager.recharge_yxrq(jObject.getString("apdu").toUpperCase());
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
            RechargeManager.recharge_order(orderCode, file0005, file0015, file8050, httpCallBack, BoardChargeActivity.this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 防拔对话框提示
     */
    private void ShowDialogToFangba(final String transindex) {
        fangbaDialog = DialogUtils.getNoticeDialog(BoardChargeActivity.this,
                "充值可能成功，为了避免您的损失，请进入充值检测流程，否则可能造成麻烦?", "确定", "取消",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(BoardChargeActivity.this,
                                TryActivity.class);
                        intent.putExtra("cardNumber", cardPan);
                        intent.putExtra("transId", transindex);
                        intent.putExtra("orderCode", orderCode);
                        intent.putExtra("successMoney", successMoney);
                        intent.putExtra("Balance", Balance);
                        intent.putExtra("file0005", file0005);
                        intent.putExtra("file0015", file0015);
                        startActivity(intent);
                        BoardChargeActivity.this.finish();
                    }
                }, new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        fangbaDialog.dismiss();
                        finish();
                    }

                });
        fangbaDialog.setCancelable(false);
        fangbaDialog.show();
    }
}
