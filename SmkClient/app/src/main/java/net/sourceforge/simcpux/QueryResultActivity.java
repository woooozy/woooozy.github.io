package net.sourceforge.simcpux;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import net.sourceforge.simcpux.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class QueryResultActivity extends BaseActivity {
    @BindView(R.id.tx_query_balance)
    public TextView tx_query_balance;
    @BindView(R.id.tx_query_validate)
    public TextView tx_query_validate;
    @BindView(R.id.tx_query_number)
    public TextView tx_query_number;
    @Override
    public int setResid() {
        return R.layout.activity_query_result;
    }

    @OnClick(R.id.btn_history)
    public void myOnClick(View View){
        Intent intent = getIntent();
        Intent intent2 = new Intent(QueryResultActivity.this,
                HistoryActivity.class);
        intent2.putExtra("history", intent.getSerializableExtra("history"));
        startActivity(intent2);
    }
    @Override
    public void initLayout() {
        toolbar_title.setText(R.string.title_result);
        tx_query_balance.setText("余额:"+getIntent().getStringExtra("Balance"));
        tx_query_validate.setText("有效期:"+getIntent().getStringExtra("cardValidate"));
        tx_query_number.setText("No."+getIntent().getStringExtra("cardNo"));
    }

    @Override
    public void setBack() {
        this.finish();
    }
}
