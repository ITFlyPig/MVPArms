package com.wangyuelin.downloader.mvp.Home.v;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wangyuelin.downloader.di.component.DaggerHomeContentComponent;
import com.wangyuelin.downloader.di.module.HomeContentModule;
import com.wangyuelin.downloader.di.module.HomeModule;
import com.wangyuelin.downloader.mvp.Home.p.HomeContentPresenter;
import com.wangyuelin.downloader.mvp.Home.p.HomePresenter;
import com.wangyuelin.downloader.mvp.contract.HomeContentContract;

public class HomeFragment extends BaseFragment<HomeContentPresenter> implements HomeContentContract.View {
    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerHomeContentComponent
                .builder()
                .appComponent(appComponent)
                .homeContentModule(new HomeContentModule(this))
                .build()
                .inject(this);

    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public RxPermissions getRxPermissions() {
        return null;
    }

    @Override
    public void showMessage(@NonNull String message) {

    }
}
