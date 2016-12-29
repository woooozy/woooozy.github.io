package me.fantouch.libs.scrolladv;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import me.fantouch.libs.scrolladv.ScrollAdv.OnImgClickListener;
import net.tsz.afinal.FinalBitmap;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

class ScrollAdvFinalBitmapAdapter extends PagerAdapter implements LifeCycleInterface {
    private FinalBitmap fb;
    private List<String> imgUrls;
    private OnImgClickListener mOnPagerItemClickListener;

    public ScrollAdvFinalBitmapAdapter(Context context, List<String> imgUrls, OnImgClickListener listener) {
        super();
        this.imgUrls = imgUrls;
        this.mOnPagerItemClickListener = listener;
        initFinalBitmap(context);
    }

    private void initFinalBitmap(Context context) {
        fb = FinalBitmap.create(context);
        fb.configLoadingImage(android.R.drawable.stat_notify_sync);
        fb.configLoadfailImage(android.R.drawable.stat_notify_error);
        fb.configBitmapMaxWidth(context.getResources().getDisplayMetrics().widthPixels);
        fb.configBitmapMaxHeight(context.getResources().getDisplayMetrics().heightPixels);
    }

    public void configLoadingImage(int loadingImgId) {
        fb.configLoadingImage(loadingImgId);
    }

    public void configLoadfailImage(int loadingImgId) {
        fb.configLoadfailImage(loadingImgId);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        ImageView imageView = new ImageView(container.getContext());
        imageView.setScaleType(ScaleType.FIT_XY);
        imageView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mOnPagerItemClickListener != null) {
                    mOnPagerItemClickListener.onImgClick(position);
                }

            }
        });
        fb.display(imageView, imgUrls.get(position));

        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imgUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Nothing
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Nothing
    }

    @Override
    public void onPause() {
        fb.onPause();
    }

    @Override
    public void onResume() {
        fb.onResume();
    }

    @Override
    public void onDestroy() {
        fb.onDestroy();
    }
}
