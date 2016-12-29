package net.sourceforge.simcpux;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.Parcelable;
import android.text.TextUtils;

import net.sourceforge.simcpux.base.BaseActivity;
import net.sourceforge.simcpux.cardbag.model.budeng.ValidateActivity;
import net.sourceforge.simcpux.consts.AppConst;
import net.sourceforge.simcpux.entity.QCHistory;
import net.sourceforge.simcpux.manager.CardManager;
import net.sourceforge.simcpux.manager.NfcCardOperateManager;
import net.sourceforge.simcpux.utils.MessageEvent;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QueryActivity extends BaseActivity {
    private static final String TAG = "QueryActivity";
    private static final int MSG_GET_CARDINFO_SUCCESS = 0x01;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private NfcCardOperateManager nfcCardManager;
    String[] resp8050, Res_0005, Res_0015, NoAndVali = null;

    private Map<String, Object> cardList = new HashMap<String, Object>();
    String money="";
    private ArrayList<QCHistory> card_History;

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
            //TODO Auto-generated method
            try {
//                Res_0005 = nfcCardManager.getCardType(AppConst.APP_MF);

                // 卡号
                Res_0015 = nfcCardManager.getCardNumberAndValidate(AppConst.APP_MF_JTB);
//                Res_0015 = nfcCardManager.getCardNumberAndValidate(AppConst.APP_ADF1);
                // 余额
                money = nfcCardManager.getCardBalance(AppConst.APP_MF_JTB);

                // 交易记录
                card_History = nfcCardManager.getQCHistory(AppConst.APP_MF_JTB);

                Logger.e("卡号------------>" + Res_0015[1]+"有效期------------>" + Res_0015[0]);
//                if (Res_0005 == null) {
//                    return;
//                }
                cardList.put("money", money);
                cardList.put("cardValidate", Res_0015[0]);
                cardList.put("cardNo", Res_0015[1]);
                EventBus.getDefault().post(new MessageEvent(MSG_GET_CARDINFO_SUCCESS, cardList));
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }

    };

    @Override
    public int setResid() {
        return R.layout.activity_query;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        Intent intent = null;
        switch (event.handlerType) {
            case MSG_GET_CARDINFO_SUCCESS:
                Map<String, Object> result = (HashMap<String, Object>) event.message;
                if (TextUtils.equals(getIntent().getStringExtra("type"), "recharge")) {
                    intent = new Intent(QueryActivity.this, RechargeActivity.class);
                } else if (TextUtils.equals(getIntent().getStringExtra("type"), "query")) {
                    intent = new Intent(QueryActivity.this, QueryResultActivity.class);
                } else if (TextUtils.equals(getIntent().getStringExtra("type"), "limited")) {
                    intent = new Intent(QueryActivity.this, ValidateActivity.class);
                } else if (TextUtils.equals(getIntent().getStringExtra("type"), "otherlimited")) {
                    intent = new Intent(QueryActivity.this, OtherValidateActivity.class);
                }
                intent.putExtra("Balance", result.get("money").toString());
                intent.putExtra("cardValidate", result.get("cardValidate").toString());
                intent.putExtra("cardpan", result.get("cardNo").toString());
                intent.putExtra("history", card_History);// 计次卡读不到交易记录
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void initLayout() {
        toolbar_title.setText(R.string.title_query);
        EventBus.getDefault().register(this);
        card_History = new ArrayList<QCHistory>();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    public void setBack() {
        this.finish();
    }
}
