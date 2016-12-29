package net.sourceforge.simcpux;

import android.app.Dialog;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import net.sourceforge.simcpux.base.BaseActivity;
import net.sourceforge.simcpux.cardbag.model.budeng.BoardChargeActivity;
import net.sourceforge.simcpux.consts.OperateTypeConst;
import net.sourceforge.simcpux.linstener.HttpCallBack;
import net.sourceforge.simcpux.manager.RechargeManager;
import net.sourceforge.simcpux.utils.DialogUtils;
import net.sourceforge.simcpux.asyHttp.HttpTools;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;

public class RechargeActivity extends BaseActivity {

    @BindView(R.id.tv_card_num)
    public TextView tv_card_num;

    @BindView(R.id.tv_available_time)
    public TextView tv_available_time;

    @BindView(R.id.tv_get_balance)
    public TextView tv_get_balance;
    static HttpTools tools;
    private int amount = 1;
    String cardPan = "";
    private Dialog noticeDialog;

    @OnClick(R.id.bt_confirmBtn)
    public void myOnclick(View view) {
        RechargeManager.create_order(cardPan, amount, httpCallBack, RechargeActivity.this);

    }

    private HttpCallBack httpCallBack = new HttpCallBack() {

        @Override
        public void onSuccess(JSONObject jObject, String operType) {
            switch (operType) {
                case OperateTypeConst.TYPE_CREATE_BOARDORDER:
                    Intent Intent = new Intent(RechargeActivity.this, BoardChargeActivity.class);
                    Intent.putExtra("orderCode",jObject.getString("ordercode"));
                    Intent.putExtra("cardpan", cardPan);
                    Intent.putExtra("other", 1);
                    startActivity(Intent);

                    break;
                case OperateTypeConst.TYPE_RECHARGE_CANCEL:
                    Logger.e("订单撤销返回成功！！");
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onFail(int statusCode, String msg, String operType) {
            noticeDialog = DialogUtils.getNoticeDialog(RechargeActivity.this, msg, "确定", null, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noticeDialog.dismiss();
                }
            }, null);
            noticeDialog.show();
        }
    };

    @Override
    public int setResid() {
        return R.layout.activity_recharge;
    }

    @Override
    public void initLayout() {
        toolbar_title.setText(R.string.title_recharge);
        cardPan=getIntent().getStringExtra("cardpan");
        String Validate = getIntent().getStringExtra("cardValidate");
        StringBuilder StringBuilder = new StringBuilder(Validate);
        if (!Validate.isEmpty()) {
            StringBuilder.insert(4, "/");
            StringBuilder.insert(7, "/");
        }
        tv_card_num.setText("No." + cardPan);
        tv_available_time.setText(StringBuilder);
        tv_get_balance.setText(getIntent().getStringExtra("Balance"));
    }

    @Override
    public void setBack() {
        this.finish();
    }
}
