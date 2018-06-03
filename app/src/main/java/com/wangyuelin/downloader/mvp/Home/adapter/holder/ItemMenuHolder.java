package com.wangyuelin.downloader.mvp.Home.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseHolder;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.http.imageloader.ImageLoader;
import com.jess.arms.http.imageloader.glide.ImageConfigImpl;
import com.jess.arms.utils.ArmsUtils;
import com.wangyuelin.downloader.R;
import com.wangyuelin.downloader.mvp.model.entity.LeftMeunItemBean;

import butterknife.BindView;

public class ItemMenuHolder extends BaseHolder<LeftMeunItemBean> {
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;

    private AppComponent mAppComponent;
    private ImageLoader mImageLoader;//用于加载图片的管理类,默认使用 Glide,使用策略模式,可替换框架

    public ItemMenuHolder(View itemView) {
        super(itemView);
        //可以在任何可以拿到 Context 的地方,拿到 AppComponent,从而得到用 Dagger 管理的单例对象
        mAppComponent = ArmsUtils.obtainAppComponentFromContext(itemView.getContext());
        mImageLoader = mAppComponent.imageLoader();
    }

    @Override
    public void setData(LeftMeunItemBean data, int position) {
        if (data == null) {
            return;
        }
        tvName.setText(data.getName());
        if (data.getIcon() > 0) {
            ivIcon.setImageResource(data.getIcon());
        }

    }
}
