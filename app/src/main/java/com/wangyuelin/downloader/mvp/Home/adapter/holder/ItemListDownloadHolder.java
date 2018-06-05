package com.wangyuelin.downloader.mvp.Home.adapter.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.wangyuelin.downloader.R;
import com.wangyuelin.downloader.app.utils.Constant;
import com.wangyuelin.downloader.mvp.Home.bean.DownloadTaskBean;

import butterknife.BindView;

public class ItemListDownloadHolder extends BaseHolder<DownloadTaskBean> {

    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_szie)
    TextView tvSzie;
    @BindView(R.id.tv_download_status)
    TextView tvDownloadStatus;
    @BindView(R.id.btn_play)
    Button btnPlay;

    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架


    public ItemListDownloadHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到 Context 的地方,拿到 AppComponent,从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(DownloadTaskBean data, int position) {
        tvName.setText(data.getName());
        tvSzie.setText(data.getTotalSize());
        String statusStr = "";
        switch (data.getStatus()) {
            case Constant.TaskStatus.DOWNLOADING:
                statusStr = "正在下载" + data.getSpeed();
                break;
            case Constant.TaskStatus.DONE:
                statusStr = "下载完成";
                break;
            case Constant.TaskStatus.FAIL:
                statusStr = "下载失败";
                break;
        }

        tvDownloadStatus.setText(statusStr);
        if (TextUtils.isEmpty(data.getPlayUrl())) {
            btnPlay.setVisibility(View.GONE);
        } else {
            btnPlay.setVisibility(View.VISIBLE);
            btnPlay.setTag(data.getPlayUrl());
        }

    }
}
