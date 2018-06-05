package com.wangyuelin.downloader.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.wangyuelin.downloader.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChooseDialog extends Dialog implements View.OnClickListener {


    @BindView(R.id.tv_download)
    TextView tvDownload;
    @BindView(R.id.tv_play)
    TextView tvPlay;
    @BindView(R.id.tv_cancle)
    TextView tvCancle;

    private OnChooseListener mOnChooseListener;

    public ChooseDialog(Context context) {
        super(context, R.style.ChooseDialog);
        View v = LayoutInflater.from(context).inflate(R.layout.view_download_popupwindow, null);
        setContentView(v);
        ButterKnife.bind(this, v);
        tvDownload.setOnClickListener(this);
        tvCancle.setOnClickListener(this);
        tvPlay.setOnClickListener(this);
        Window dialogWindow = getWindow();

        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialogWindow.getDecorView().setPadding(0, 0, 0, 0);
        dialogWindow.setGravity(Gravity.BOTTOM|Gravity.CENTER);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_download:
                if (mOnChooseListener != null) {
                    mOnChooseListener.onDownoad();
                }
                dismiss();
                break;
            case R.id.tv_play:
                if (mOnChooseListener != null) {
                    mOnChooseListener.onPlay();
                }
                dismiss();
                break;
            case R.id.tv_cancle:
                if (mOnChooseListener != null) {
                    mOnChooseListener.onCancle();
                }
                dismiss();
                break;
        }

    }


    public interface OnChooseListener {
        void onPlay();

        void onDownoad();

        void onCancle();

    }

    public void setOnChooseListener(OnChooseListener mOnChooseListener) {
        this.mOnChooseListener = mOnChooseListener;
    }
}
