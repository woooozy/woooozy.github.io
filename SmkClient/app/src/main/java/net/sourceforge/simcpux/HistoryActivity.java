package net.sourceforge.simcpux;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import net.sourceforge.simcpux.adapter.HistoryAdapter;
import net.sourceforge.simcpux.base.BaseActivity;
import net.sourceforge.simcpux.entity.QCHistory;
import net.sourceforge.simcpux.widget.DividerItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;

public class HistoryActivity extends BaseActivity {

    @BindView(R.id.rl_history)
    RecyclerView rl_history;

    HistoryAdapter HistoryAdapter;
    ArrayList<QCHistory> hisDataList;
    @Override
    public int setResid() {
        return R.layout.activity_history;
    }

    @Override
    public void initLayout() {
        toolbar_title.setText(R.string.title_history);
        hisDataList=new ArrayList<>();
        ArrayList<QCHistory> arr=(ArrayList<QCHistory>)getIntent().getSerializableExtra("history");
        for (int i = 0; i < arr.size(); i++) {
            if (!"".equals(arr.get(i).getBalance())) {
                hisDataList.add(arr.get(i));
            }
        }
        HistoryAdapter=new HistoryAdapter(this,R.layout.item_history_layout,hisDataList);
        if (hisDataList.isEmpty()){
            HistoryAdapter.setEmptyView(LayoutInflater.from(this).inflate(R.layout.activity_history,null,false));
        }
        rl_history.setLayoutManager(new LinearLayoutManager(this ));
        rl_history.setAdapter(HistoryAdapter);
        rl_history.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
    }

    @Override
    public void setBack() {
        this.finish();
    }
}
