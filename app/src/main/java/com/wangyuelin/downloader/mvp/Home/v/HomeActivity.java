package com.wangyuelin.downloader.mvp.Home.v;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.FrameLayout;

import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wangyuelin.downloader.R;
import com.wangyuelin.downloader.di.component.DaggerHomeComponent;
import com.wangyuelin.downloader.di.module.HomeModule;
import com.wangyuelin.downloader.mvp.Home.adapter.LeftMenuAdapter;
import com.wangyuelin.downloader.mvp.Home.p.HomePresenter;
import com.wangyuelin.downloader.mvp.contract.HomeContract;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页
 */
public class HomeActivity extends BaseActivity<HomePresenter> implements HomeContract.View {
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.left_menu)
    RecyclerView leftMenu;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

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
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        HomeFragment homeFragment =  HomeFragment.newInstance(null);
        getSupportFragmentManager().beginTransaction().add(R.id.fl_content, homeFragment).commit();
        initRecyclerView();
        mPresenter.initMenuAdapter();

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
    public void setMenuAdapter(LeftMenuAdapter menuAdapter) {
        if (menuAdapter != null) {
            Log.d("ll",  "为左边的菜单设置adapter 数据：" + menuAdapter.getInfos().size() );
            leftMenu.setAdapter(menuAdapter);
            menuAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        ArmsUtils.configRecyclerView(leftMenu, linearLayoutManager);
    }

}
