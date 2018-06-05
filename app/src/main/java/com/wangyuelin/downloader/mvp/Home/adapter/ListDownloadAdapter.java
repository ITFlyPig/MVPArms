package com.wangyuelin.downloader.mvp.Home.adapter;

import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.wangyuelin.downloader.R;
import com.wangyuelin.downloader.mvp.Home.adapter.holder.ItemListDownloadHolder;
import com.wangyuelin.downloader.mvp.Home.bean.DownloadTaskBean;

import java.util.List;

public class ListDownloadAdapter extends DefaultAdapter<DownloadTaskBean> {


    public ListDownloadAdapter(List<DownloadTaskBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<DownloadTaskBean> getHolder(View v, int viewType) {
        return new ItemListDownloadHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_download_list;
    }
}
