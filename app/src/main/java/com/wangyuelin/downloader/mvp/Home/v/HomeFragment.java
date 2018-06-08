package com.wangyuelin.downloader.mvp.Home.v;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wangyuelin.downloader.R;
import com.wangyuelin.downloader.app.event.TaskEvent;
import com.wangyuelin.downloader.app.utils.ChoosePopUtil;
import com.wangyuelin.downloader.app.utils.InputPopUtil;
import com.wangyuelin.downloader.di.component.DaggerHomeContentComponent;
import com.wangyuelin.downloader.di.module.HomeContentModule;
import com.wangyuelin.downloader.mvp.Home.adapter.ContentTabFragmentsAdapter;
import com.wangyuelin.downloader.mvp.Home.p.HomeContentPresenter;
import com.wangyuelin.downloader.mvp.contract.HomeContentContract;
import com.wangyuelin.downloader.widget.ChooseDialog;
import com.wangyuelin.downloader.widget.InputDialog;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

import org.simple.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment<HomeContentPresenter> implements HomeContentContract.View, View.OnClickListener, InputDialog.OnConfirmListener {
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    Unbinder unbinder;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_add_download)
    ImageView ivAddDownload;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    Unbinder unbinder1;
    private ChooseDialog mChooseDialog;
    private InputDialog mInputDialog;

    @Inject
    RxPermissions mRxPermissions;

    /**
     * 使用静态工厂的原因：
     * Fragment在重新创建的时候只会调用无参的构造方法，并且如果之前通过fragment.setArguments(bundle)这种方式设置过参数的话，
     * Fragment重建时会得到这些参数
     *
     * @return
     */
    public static HomeFragment newInstance(Bundle args) {
        HomeFragment fragment = new HomeFragment();
        if (args != null) {
            fragment.setArguments(args);//这个是关键
        }
        return fragment;

    }

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
        return inflater.inflate(R.layout.fragment_home_content, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        ivAddDownload.setOnClickListener(this);
        initRecyclerView();
        initToolBar();
        initTablayout();
        mPresenter.initPagerAdapter(getChildFragmentManager());


    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public RxPermissions getRxPermissions() {
        return mRxPermissions;
    }

    @Override
    public void addTask(XLTaskInfo info, String url) {
        EventBus.getDefault().post(new TaskEvent(info, url));
    }

    @Override
    public void setAdapter(ContentTabFragmentsAdapter adapter) {
        viewpager.setAdapter(adapter);
    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        ArmsUtils.configRecyclerView(mRecyclerView, linearLayoutManager);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    private void initToolBar() {
        DrawerArrowDrawable drawerArrowDrawable = new DrawerArrowDrawable(getContext());
        drawerArrowDrawable.setColor(getResources().getColor(R.color.white));
        toolbar.setNavigationIcon(drawerArrowDrawable);
    }

    private void initTablayout() {
        tablayout.addTab(tablayout.newTab().setText("正在下载"));
        tablayout.addTab(tablayout.newTab().setText("已下载"));
        tablayout.setupWithViewPager(viewpager);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add_download:
                if (mChooseDialog == null || !mChooseDialog.isShowing()) {
                    mChooseDialog = ChoosePopUtil.showChoosePop(getActivity(), new ChooseDialog.OnChooseListener() {
                        @Override
                        public void onPlay() {
                            if (mInputDialog != null && mInputDialog.isShowing()) {
                                return;
                            }
                            InputPopUtil.showChoosePop(getActivity(), HomeFragment.this);


                        }

                        @Override
                        public void onDownoad() {
                            if (mInputDialog != null && mInputDialog.isShowing()) {
                                return;
                            }
                            InputPopUtil.showChoosePop(getActivity(), HomeFragment.this);

                        }

                        @Override
                        public void onCancle() {

                        }
                    });
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void onConfir(String url) {
        //添加下载的任务
        mPresenter.addDownloadTask(url);

    }
}
