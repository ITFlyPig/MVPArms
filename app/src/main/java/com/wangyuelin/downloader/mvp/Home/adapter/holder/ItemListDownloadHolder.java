package com.wangyuelin.downloader.mvp.Home.adapter.holder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.utils.ArmsUtils;
import com.wangyuelin.downloader.R;
import com.wangyuelin.downloader.app.event.PauseEvent;
import com.wangyuelin.downloader.app.event.StartDownloadEvent;
import com.wangyuelin.downloader.app.utils.Constant;
import com.wangyuelin.downloader.app.utils.IconUtil;
import com.wangyuelin.downloader.mvp.Home.bean.DownloadTaskBean;
import com.wangyuelin.downloader.widget.ProgressLineView;

import org.simple.eventbus.EventBus;

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
    @BindView(R.id.tv_pause_download)
    TextView tvPauseDownload;
    @BindView(R.id.ll_content)
    LinearLayout llContent;
    @BindView(R.id.v_split_line)
    ProgressLineView progressLineView;

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
        if (TextUtils.isEmpty(data.getName())) {
            tvName.setText("名称为空");
        } else {
            tvName.setText(data.getName());
        }
        //下载的进度
        progressLineView.setProgress(data.getProgress());
        Log.d("dd", "下载进度：" + data.getProgress());

        tvSzie.setText(data.getDownLoadSize() + "/" + data.getTotalSize());
        Log.d("st", "下载的状态：" + data.getStatus());
        String statusStr = "";
        switch (data.getStatus()) {
            case Constant.TaskStatus.DOWNLOADING:
                statusStr = data.getSpeed();
                break;
            case Constant.TaskStatus.STOP:
                statusStr = "停止下载";
                break;
            case Constant.TaskStatus.DONE:
                statusStr = "下载完成";
                break;
            case Constant.TaskStatus.FAIL:
                statusStr = "下载失败";
                break;
        }
//
        tvDownloadStatus.setText(statusStr);
//        if (TextUtils.isEmpty(data.getPlayUrl())) {
//            btnPlay.setVisibility(View.GONE);
//        } else {
//            btnPlay.setVisibility(View.VISIBLE);
//            btnPlay.setTag(data.getPlayUrl());
//        }
        String type = IconUtil.getTypeByName(data.getName());
        if (!TextUtils.isEmpty(type)) {
            int icon = IconUtil.getIconByType(type);
            ivCover.setImageDrawable(ivStatus.getContext().getResources().getDrawable(icon));
        }

        ivCover.setTag(data);
        ivCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTaskBean downloadInfo = (DownloadTaskBean) v.getTag();
                if (downloadInfo.getStatus() == Constant.TaskStatus.DOWNLOADING) {
                    String playUrl = downloadInfo.getPlayUrl();
                    if (!TextUtils.isEmpty(playUrl)) {
                        Log.d("pl", "边下边播：" + playUrl);
                        playUrl(playUrl, v.getContext());
                    } else {
                        Snackbar.make(llContent, "抱歉，此视频不支持播放", Snackbar.LENGTH_SHORT).show();
                    }
                } else if (downloadInfo.getStatus() == Constant.TaskStatus.DONE) {
                    String path = downloadInfo.getSavePath() + downloadInfo.getName();
                    Log.d("pl", "播放本地的：" + path);
                    if (!TextUtils.isEmpty(path)) {
                        playUrl(path, v.getContext());
                    }

                }


            }
        });

        llContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadTaskBean data = (DownloadTaskBean) v.getTag();
                if (data == null) {
                    return;
                }
                EventBus.getDefault().post(new StartDownloadEvent(data.getTaskId(), data.getDownloadUrl()));//在下载就暂停，暂停就开始下载
            }
        });
        llContent.setTag(data);

//        if (data.getStatus() == Constant.TaskStatus.STOP) {
//            tvPauseDownload.setText("继续下载");
//        } else if (data.getStatus() == Constant.TaskStatus.DOWNLOADING) {
//            tvPauseDownload.setText("暂停下载");
//        }

    }


//    @Override
//    public void onClick(View view) {
//        super.onClick(view);
//        switch (view.getId()) {
//            case R.id.tv_pause_download://暂停或者开始
//                DownloadTaskBean data = (DownloadTaskBean) view.getTag();
//                if (data == null) {
//                    return;
//                }
//                if (data.getStatus() == Constant.TaskStatus.DOWNLOADING) {//暂停下载
//                    EventBus.getDefault().post(new PauseEvent(data.getTaskId()));
//                } else {//开始下载
//                    EventBus.getDefault().post(new StartDownloadEvent(data.getTaskId(), data.getDownloadUrl()));
//                }
//
//
//                break;
//        }
//    }

    /**
     * 播放视频
     *
     * @param url
     * @param context
     */
    private void playUrl(String url, Context context) {
        Intent mediaIntent = new Intent(Intent.ACTION_VIEW);
        mediaIntent.setDataAndType(Uri.parse(url), "video/*");
        context.startActivity(mediaIntent);
    }
}
