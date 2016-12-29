package net.sourceforge.simcpux.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.entity.OrderEntity;
import net.sourceforge.simcpux.utils.CommonUtils;

import java.util.List;

/**
 * *****************************************************************************
 * 作者： woozy
 * 开发日期： 2016/11/8.
 * 模块功能：
 * *****************************************************************************
 */
public class OrderListAdapter extends BaseQuickAdapter<OrderEntity>{
    Context Context;
    public OrderListAdapter(Context context, int layoutResId, List<OrderEntity> data) {
        super(context, layoutResId, data);
        this.Context=context;
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, OrderEntity orderEntity) {
        StringBuilder sb = new StringBuilder(orderEntity.getOrdertime().substring(0,orderEntity.getOrdertime().length()-6));
        baseViewHolder.setText(R.id.trade_time,sb.toString());
        sb.insert(4, "-");
        sb.insert(7, "-");
        baseViewHolder.setText(R.id.trade_week,"周"+ CommonUtils.getWeek(sb.toString()));
        baseViewHolder.setText(R.id.order_cardpan,orderEntity.getCardpan());
        String Status="";
        switch (orderEntity.getOrderstatus()){
            case "00":
                Status="创建状态";
                break;
            case "01":
                Status="支付中";
                break;
            case "02":
                Status="撤消中";
                break;
            case "03":
                Status="退款中";
                break;
            case "10":
                Status="完成";
                break;
            case "99":
                Status="撤消";
                break;
            case "88":
                Status="已退款";
                break;
            default:
                Status="未知状态";
                break;
        }
        baseViewHolder.setText(R.id.trade_type_txt,Status);

    }
}
