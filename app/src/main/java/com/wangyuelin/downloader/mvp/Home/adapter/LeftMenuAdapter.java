package com.wangyuelin.downloader.mvp.Home.adapter;

import android.util.Log;
import android.view.View;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.base.DefaultAdapter;
import com.wangyuelin.downloader.R;
import com.wangyuelin.downloader.mvp.Home.adapter.holder.ItemMenuHolder;
import com.wangyuelin.downloader.mvp.model.entity.LeftMeunItemBean;

import java.util.List;

public class LeftMenuAdapter extends DefaultAdapter<LeftMeunItemBean> {


    public LeftMenuAdapter(List<LeftMeunItemBean> infos) {
        super(infos);
    }

    @Override
    public BaseHolder<LeftMeunItemBean> getHolder(View v, int viewType) {
        Log.d("ll",  "创建Holder");
        return new ItemMenuHolder(v);
    }

    @Override
    public int getLayoutId(int viewType) {
        return R.layout.item_menu;
    }
}
