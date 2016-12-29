package net.sourceforge.simcpux;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sourceforge.simcpux.adapter.OrderListAdapter;
import net.sourceforge.simcpux.base.BaseActivity;
import net.sourceforge.simcpux.consts.OperateTypeConst;
import net.sourceforge.simcpux.entity.OrderEntity;
import net.sourceforge.simcpux.linstener.HttpCallBack;
import net.sourceforge.simcpux.manager.RechargeManager;
import net.sourceforge.simcpux.widget.DividerItemDecoration;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import butterknife.BindView;

public class RechargeOrderActivity extends BaseActivity {
    net.sourceforge.simcpux.adapter.OrderListAdapter OrderListAdapter;
    ArrayList<OrderEntity> listDataList;

    @BindView(R.id.rl_orderList)
    RecyclerView rl_orderList;
    HttpCallBack HttpCallBack=new HttpCallBack() {
        @Override
        public void onSuccess(JSONObject jObject, String operType) {
            switch (operType){
                case OperateTypeConst.TYPE_RECHARGE_LIST:
                    JSONArray JSONArray = jObject.getJSONArray("list");
                    Gson Gson=new Gson();
                    listDataList=Gson.fromJson(JSONArray.toString(),new TypeToken<ArrayList<OrderEntity>>(){}.getType());
                    OrderListAdapter=new OrderListAdapter(RechargeOrderActivity.this,R.layout.item_orderlist_layout,listDataList);
                    rl_orderList.setAdapter(OrderListAdapter);
                    rl_orderList.addItemDecoration(new DividerItemDecoration(RechargeOrderActivity.this,DividerItemDecoration.VERTICAL_LIST));
                    OrderListAdapter.setOnRecyclerViewItemClickListener(new BaseQuickAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, int i) {
                        }
                    });
                    OrderListAdapter.openLoadAnimation();
                    break;
                default:break;
            }
        }

        @Override
        public void onFail(int statusCode, String msg, String operType) {
            Logger.e(msg);
        }
    };
    @Override
    public int setResid() {
        return R.layout.activity_recharge_order;
    }

    @Override
    public void initLayout() {
        toolbar_title.setText(R.string.title_recharge_list);
        listDataList=new ArrayList<>();
//        OrderListAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.activity_splash,null,false));
        rl_orderList.setLayoutManager(new LinearLayoutManager(this ));
        RechargeManager.recharge_list("","3105060801000000028",10,1,"APP",HttpCallBack,RechargeOrderActivity.this);
    }

    @Override
    public void setBack() {
        this.finish();
    }
}
