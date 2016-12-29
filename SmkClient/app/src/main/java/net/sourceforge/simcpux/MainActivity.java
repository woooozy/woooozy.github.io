package net.sourceforge.simcpux;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.balysv.materialmenu.MaterialMenuDrawable;

import net.sourceforge.simcpux.adapter.HomeAdapter;
import net.sourceforge.simcpux.base.BaseActivity;
import net.sourceforge.simcpux.consts.OperateTypeConst;
import net.sourceforge.simcpux.linstener.HttpCallBack;
import net.sourceforge.simcpux.manager.OnlineAccManager;
import net.sourceforge.simcpux.widget.DividerGridItemDecoration;
import com.orhanobut.logger.Logger;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    @BindView(R.id.id_nv_menu)
    NavigationView navigationView;

    @BindView(R.id.id_layout_drawer)
    DrawerLayout drawerLayout;

    @BindView(R.id.rl_main)
    RecyclerView rl_main;

    private boolean isDrawerOpened;
    private HomeAdapter HomeAdapter;
    String[] mainTitle={"余额查询","卡片充值","订单查询","联机账户","年审补登","他人年审补登","预留位置","预留位置","预留位置"};
    @Override
    public int setResid() {
        return R.layout.activity_main;
    }

    @Override
    public void initLayout() {
        setSupportActionBar(toolbar);
        materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
//        toolbar.setNavigationIcon(materialMenu);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialMenu.animateIconState(MaterialMenuDrawable.IconState.ARROW);
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        drawerLayout.setDrawerListener(SimpleDrawerListener);
        navigationView.setNavigationItemSelectedListener(onItemSelectListener);

        HomeAdapter = new HomeAdapter(this, mainTitle);
        rl_main.setLayoutManager(new GridLayoutManager(this,3));
        rl_main.setAdapter(HomeAdapter);
        rl_main.addItemDecoration(new DividerGridItemDecoration(this));
        HomeAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case 0:
                        Intent intent=new Intent(MainActivity.this,QueryActivity.class);
                        intent.putExtra("type", "query");
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent1=new Intent(MainActivity.this,QueryActivity.class);
                        intent1.putExtra("type", "recharge");
                        startActivity(intent1);
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this,RechargeOrderActivity.class));
                        break;
                    case 3:
                        OnlineAccManager.OnlineSelect("1999013002000001746","383935",httpCallBack,MainActivity.this);
                        break;
                    case 4:
                        Intent intent2=new Intent(MainActivity.this,QueryActivity.class);
                        intent2.putExtra("type", "limited");
                        startActivity(intent2);
                        break;
                    case 5:
                        Intent intent5=new Intent(MainActivity.this,QueryActivity.class);
                        intent5.putExtra("type", "otherlimited");
                        startActivity(intent5);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private HttpCallBack httpCallBack=new HttpCallBack() {

        @Override
        public void onSuccess(JSONObject jObject, String operType) {
            switch (operType) {
                case  OperateTypeConst.ONLINEACCOUNT_SELECT:
                    Logger.e("联机账户查询成功！！");
                    OnlineAccManager.Online_Pay("1999013002000001746","383935",100,httpCallBack,MainActivity.this);
                    break;
                case OperateTypeConst.ONLINEACCOUNT_PAY:
                    Logger.e("联机账户消费成功！！");
                    String merchid = jObject.getString("merchid");
                    String termno = jObject.getString("termno");
                    String flowNo = jObject.getString("flowno");
                    String batchno = jObject.getString("batchno");
                    OnlineAccManager.Online_Pay_Cancel("1999013002000001746","383935",100,merchid,termno,flowNo,batchno,httpCallBack,MainActivity.this);
                    break;
                case  OperateTypeConst.ONLINEACCOUNT_PAY_CANCEL:
                    Logger.e("联机账户消费撤销成功！！");
                    break;
                default:break;
            }
        }

        @Override
        public void onFail(int statusCode, String msg, String operType) {
            Logger.e(msg);
        }
    };


    public DrawerLayout.SimpleDrawerListener SimpleDrawerListener = new DrawerLayout.SimpleDrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);
            materialMenu.setTransformationOffset(MaterialMenuDrawable.AnimationState.BURGER_ARROW,
                    isDrawerOpened ? 2 - slideOffset : slideOffset);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            isDrawerOpened = true;
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            isDrawerOpened = false;
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (newState == DrawerLayout.STATE_IDLE) {
                if (isDrawerOpened) {
                    materialMenu.animateIconState(MaterialMenuDrawable.IconState.ARROW);
                } else {
                    materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
                }
            }
        }
    };

    public NavigationView.OnNavigationItemSelectedListener onItemSelectListener = new NavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            Logger.e( item.getTitle() + "");
            item.setChecked(true);
            drawerLayout.closeDrawers();
            if (TextUtils.equals(item.getTitle(), "登录")) {
            }
            return true;
        }
    };

    @Override
    public void setBack() {
        this.finish();
    }
}
