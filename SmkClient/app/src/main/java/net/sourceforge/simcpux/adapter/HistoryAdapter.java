package net.sourceforge.simcpux.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.entity.QCHistory;
import net.sourceforge.simcpux.utils.CommonUtils;

import java.util.List;

/**
 * *****************************************************************************
 * 作者： woozy
 * 开发日期： 2016/10/14.
 * 模块功能：
 * *****************************************************************************
 */
public class HistoryAdapter extends BaseQuickAdapter<QCHistory>{
    Context Context;
    public HistoryAdapter(Context context, int layoutResId, List<QCHistory> data) {
        super(context, layoutResId, data);
        this.Context=context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder,QCHistory qcHistory) {
        int tradeTypeInt = Integer.parseInt(qcHistory.getTradeType());
        baseViewHolder.setText(R.id.trade_money_txt,qcHistory.getBalance());
        String type="";
        String status="";
        int color=0;
        if(tradeTypeInt == 2){
            type="充值";
            status="+";
            color=R.color.colorPrimary;
        }else if (tradeTypeInt >= 3) {
            type="消费";
            status="-";
            color=R.color.colorRed;
        }
        baseViewHolder.setText(R.id.trade_type_txt,type);
        baseViewHolder.setText(R.id.trade_money_txt,status+qcHistory.getBalance() + "元");
        baseViewHolder.setTextColor(R.id.trade_money_txt,color);
        StringBuilder sb = new StringBuilder(qcHistory.getTradeDate());
        sb.insert(4, "-");
        sb.insert(7, "-");
        baseViewHolder.setText(R.id.trade_time,sb.toString());
        baseViewHolder.setText(R.id.trade_week,"周"+ CommonUtils.getWeek(sb.toString()));
    }
}
