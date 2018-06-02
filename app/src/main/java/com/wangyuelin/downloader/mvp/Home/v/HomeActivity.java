package com.wangyuelin.downloader.mvp.Home.v;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wangyuelin.downloader.di.component.DaggerHomeComponent;
import com.wangyuelin.downloader.di.module.HomeModule;
import com.wangyuelin.downloader.mvp.Home.p.HomePresenter;
import com.wangyuelin.downloader.mvp.contract.HomeContract;

/**
 * 首页
 */
public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHomeComponent
                .builder()
                .appComponent(appComponent)
                .homeModule(new HomeModule(this))
                .build()
                .inject(this);

    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return 0;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void startLoadMore() {

    }

    @Override
    public void endLoadMore() {

    }

    @Override
    public Activity getActivity() {
        return null;
    }

    @Override
    public RxPermissions getRxPermissions() {
        return null;
    }

    @Override
    public void showMessage(@NonNull String message) {

    }
}
