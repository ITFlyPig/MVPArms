package com.wangyuelin.downloader.widget;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.wangyuelin.downloader.R;
import com.wangyuelin.downloader.mvp.Home.bean.DownloadTaskType;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InputDialog extends Dialog {

    private OnConfirmListener mOnConfirmListener;

    @BindView(R.id.et_input)
    TextInputEditText etInput;
    @BindView(R.id.btn_ok)
    Button btnOk;

    public InputDialog(Context context) {
        super(context);

        View v = LayoutInflater.from(context).inflate(R.layout.view_input_url_popupwindow, null);
        setContentView(v);
        ButterKnife.bind(this, v);

        Window dialogWindow = getWindow();

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialogWindow.setGravity(Gravity.CENTER);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = etInput.getText().toString();
                if (checkUrl(url)) {
                    dismiss();
                    if (mOnConfirmListener != null) {
                        mOnConfirmListener.onConfir(url);
                    }
                } else {
                    etInput.setError("输入的链接不支持");
                }

            }
        });

//        setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = ((Activity)context).getWindow().getAttributes();
//                lp.alpha = 1f;
//                ((Activity)context).getWindow().setAttributes(lp);
//            }
//        });

    }

    public interface OnConfirmListener{
        void onConfir(String url);
    }


    /**
     * 检查是否是支持的连接
     * @param url
     * @return
     */
    private boolean checkUrl(String url){
        if (TextUtils.isEmpty(url)) {
            return false;
        }
        DownloadTaskType[] types = DownloadTaskType.values();
        for (DownloadTaskType type : types) {
            if (url.startsWith(type.getProtocol())) {
                return true;
            }
        }
        return false;
    }


    public void setOnConfirmListener(OnConfirmListener mOnConfirmListener) {
        this.mOnConfirmListener = mOnConfirmListener;
    }
}
