package net.sourceforge.simcpux.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.sourceforge.simcpux.R;

/**
 * *****************************************************************************
 * 作者： woozy
 * 开发日期： 2016/10/8.
 * 模块功能：
 * *****************************************************************************
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeHolder> {

    private Context Context;
    private String[] mDatas;

    public interface OnItemClickListener
    {
        void onItemClick(View view, int position);
    }
    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener)
    {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public HomeAdapter(Context Context, String[] mDatas){
        this.Context=Context;
        this.mDatas=mDatas;
    }
    @Override
    public HomeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HomeHolder HomeHolder = new HomeHolder(LayoutInflater.from(Context).inflate(R.layout.item_main_layout, parent, false));
        return HomeHolder;
    }

    @Override
    public void onBindViewHolder(final HomeHolder holder, final int position) {
        holder.tx.setText(mDatas[position]);
        if (mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(holder.itemView,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDatas.length;
    }

    class HomeHolder extends RecyclerView.ViewHolder {

        TextView tx;
        public HomeHolder(View itemView) {
            super(itemView);
            tx=(TextView)itemView.findViewById(R.id.item_tx);
        }
    }
}
