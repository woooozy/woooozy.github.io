package net.sourceforge.simcpux.utils;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import net.sourceforge.simcpux.R;

/**
 * *****************************************************************************
 * 作者： woozy
 * 开发日期： 2016/11/16.
 * 模块功能：
 * *****************************************************************************
 */
public class DialogUtils {
    private static Dialog loadingDialog;

    private DialogUtils() {

    }

    /**
     *
     * ********************************************************************<br>
     * 方法功能：获取自定义的加载的Dialog<br>
     * 参数说明：<br>
     * 作 者：杨明<br>
     * 开发日期：2013-7-4 上午9:28:53<br>
     * 修改日期：<br>
     * 修改人：<br>
     * 修改说明：<br>
     * ********************************************************************<br>
     */
    public static Dialog showLoadingDialog(final Context context,
                                           final int resId) {
        loadingDialog = new Dialog(context, R.style.loadingDialog);
        loadingDialog.setContentView(R.layout.progress_dialog_layout);
        final ImageView loadingImage = (ImageView) loadingDialog
                .findViewById(R.id.loadingImg);
        loadingImage.setBackgroundResource(R.drawable.loading_bg);
        final TextView msgTV = (TextView) loadingDialog
                .findViewById(R.id.msgTV);
        msgTV.setText(resId);
        final Animation animation = AnimationUtils.loadAnimation(context,
                R.anim.loading_rotate);
        // 使用ImageView显示动画
        loadingImage.startAnimation(animation);

        loadingDialog.setCancelable(false);
        return loadingDialog;
    }

    /**
     *
     * ********************************************************************<br>
     * 方法功能：获取自定义的Dialog<br>
     * 参数说明：message：表示Dialog的消息，confirmBtnTxt：确认按钮的字符串，cancelBtnTxt：取消按钮的字符串 <br>
     * confirmListener：表示确认按钮的监听， cancelListener：表示取消按钮的监听<br>
     * 如果只有一个按钮，则可以将另一个监听传null<br>
     * 作 者：杨明<br>
     * 开发日期：2013-7-4 上午9:56:13<br>
     * 修改日期：<br>
     * 修改人：<br>
     * 修改说明：<br>
     * ********************************************************************<br>
     */
    public static Dialog getNoticeDialog(final Context context,
                                         final String message, final String confirmBtnTxt,
                                         final String cancelBtnTxt, final OnClickListener confirmListener,
                                         final OnClickListener cancelListener) {
        final Dialog dialog = new Dialog(context, R.style.noticeDialog);
        dialog.setContentView(R.layout.notice_dialog_layout);
        dialog.setCanceledOnTouchOutside(false);
        final TextView msgTxt = (TextView) dialog.findViewById(R.id.msgText);
        msgTxt.setText(message);
        final Button confirmBtn = (Button) dialog.findViewById(R.id.confirmBtn);
        final Button cancelBtn = (Button) dialog.findViewById(R.id.cancelBtn);
        if (confirmListener != null) {
            confirmBtn.setText(confirmBtnTxt);
            confirmBtn.setOnClickListener(confirmListener);
        } else {
            confirmBtn.setVisibility(View.GONE);
        }
        if (cancelListener != null) {
            cancelBtn.setText(cancelBtnTxt);
            cancelBtn.setOnClickListener(cancelListener);
        } else {
            cancelBtn.setVisibility(View.GONE);
        }
        return dialog;
    }

    public static Dialog getSuccessDialog(final Context context,
                                          final String message, final String confirmBtnTxt,
                                          final String cancelBtnTxt, final OnClickListener confirmListener) {
        final Dialog dialog = new Dialog(context, R.style.noticeDialog);
        dialog.setContentView(R.layout.success_dialog);
        final TextView msgTxt = (TextView) dialog.findViewById(R.id.msgText);
        msgTxt.setText(message);
        final Button confirmBtn = (Button) dialog.findViewById(R.id.confirmBtn);
        if (confirmListener != null) {
            confirmBtn.setText(confirmBtnTxt);
            confirmBtn.setOnClickListener(confirmListener);
        } else {
            confirmBtn.setVisibility(View.GONE);
        }
        return dialog;
    }
}
