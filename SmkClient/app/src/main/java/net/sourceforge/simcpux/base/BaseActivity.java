package net.sourceforge.simcpux.base;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.balysv.materialmenu.MaterialMenuDrawable;
import net.sourceforge.simcpux.R;
import net.sourceforge.simcpux.widget.StatusBarCompat;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * *****************************************************************************
 * 作者： woozy
 * 开发日期： 2016/9/9.
 * 模块功能：
 * *****************************************************************************
 */
public abstract class BaseActivity extends AppCompatActivity{
    public MaterialMenuDrawable materialMenu;
    @BindView(R.id.activity_base_toolbar)
    public Toolbar toolbar;

    @BindView(R.id.toolbar_title)
    public TextView toolbar_title;

    public abstract int setResid();

    public abstract void initLayout();

    public abstract void setBack();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setResid());
        ButterKnife.bind(this);
        StatusBarCompat.compat(this);


        materialMenu = new MaterialMenuDrawable(this, Color.WHITE, MaterialMenuDrawable.Stroke.THIN);
        materialMenu.animateIconState(MaterialMenuDrawable.IconState.ARROW);
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(materialMenu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialMenu.animateIconState(MaterialMenuDrawable.IconState.BURGER);
                setBack();
            }
        });

        initLayout();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode()==KeyEvent.KEYCODE_BACK&&event.getAction()==KeyEvent.ACTION_UP){
            setBack();
            return true;
        }else {
            return super.dispatchKeyEvent(event);
        }
    }
}
