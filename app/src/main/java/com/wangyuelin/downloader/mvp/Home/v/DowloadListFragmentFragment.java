package com.wangyuelin.downloader.mvp.Home.v;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.wangyuelin.downloader.R;
import com.wangyuelin.downloader.app.event.PauseEvent;
import com.wangyuelin.downloader.app.event.StartDownloadEvent;
import com.wangyuelin.downloader.app.event.TaskEvent;
import com.wangyuelin.downloader.di.component.DaggerDowloadListFragmentComponent;
import com.wangyuelin.downloader.di.module.DowloadListFragmentModule;
import com.wangyuelin.downloader.mvp.Home.adapter.ListDownloadAdapter;
import com.wangyuelin.downloader.mvp.Home.p.DowloadListFragmentPresenter;
import com.wangyuelin.downloader.mvp.contract.DowloadListFragmentContract;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class DowloadListFragmentFragment extends BaseFragment<DowloadListFragmentPresenter> implements DowloadListFragmentContract.View {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    Unbinder unbinder;

    public static DowloadListFragmentFragment newInstance(Bundle args) {
        DowloadListFragmentFragment fragment = new DowloadListFragmentFragment();
        if (args != null) {
            fragment.setArguments(args);//这个是关键
        }
        return fragment;

    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerDowloadListFragmentComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .dowloadListFragmentModule(new DowloadListFragmentModule(this))
                .build()
                .inject(this);

        Log.d("yy", "presenter是否为空：" + (mPresenter == null));
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dowload_list, container, false);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initRecyclerView();
        mPresenter.initAdapter();

    }

    /**
     * 通过此方法可以使 Fragment 能够与外界做一些交互和通信, 比如说外部的 Activity 想让自己持有的某个 Fragment 对象执行一些方法,
     * 建议在有多个需要与外界交互的方法时, 统一传 {@link Message}, 通过 what 字段来区分不同的方法, 在 {@link #setData(Object)}
     * 方法中就可以 {@code switch} 做不同的操作, 这样就可以用统一的入口方法做多个不同的操作, 可以起到分发的作用
     * <p>
     * 调用此方法时请注意调用时 Fragment 的生命周期, 如果调用 {@link #setData(Object)} 方法时 {@link Fragment#onCreate(Bundle)} 还没执行
     * 但在 {@link #setData(Object)} 里却调用了 Presenter 的方法, 是会报空的, 因为 Dagger 注入是在 {@link Fragment#onCreate(Bundle)} 方法中执行的
     * 然后才创建的 Presenter, 如果要做一些初始化操作,可以不必让外部调用 {@link #setData(Object)}, 在 {@link #initData(Bundle)} 中初始化就可以了
     * <p>
     * Example usage:
     * <pre>
     * public void setData(@Nullable Object data) {
     *     if (data != null && data instanceof Message) {
     *         switch (((Message) data).what) {
     *             case 0:
     *                 loadData(((Message) data).arg1);
     *                 break;
     *             case 1:
     *                 refreshUI();
     *                 break;
     *             default:
     *                 //do something
     *                 break;
     *         }
     *     }
     * }
     *
     * // call setData(Object):
     * Message data = new Message();
     * data.what = 0;
     * data.arg1 = 1;
     * fragment.setData(data);
     * </pre>
     *
     * @param data 当不需要参数时 {@code data} 可以为 {@code null}
     */
    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {

    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        ArmsUtils.configRecyclerView(mRecyclerView, linearLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.drawable_dvider));
        mRecyclerView.addItemDecoration(divider);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null)
            unbinder.unbind();
    }

    @Override
    public void setAdapter(ListDownloadAdapter adapter) {
        mRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Log.d("xk", "list size：" + adapter.getInfos().size());
    }

    @Subscriber
    public void addTask(TaskEvent taskEvent) {
        mPresenter.addTask(taskEvent.getTaskInfo(), taskEvent.getUrl());
    }

    @Subscriber
    public void pauseDownload(PauseEvent pauseEvent) {
        mPresenter.pause(pauseEvent.getTaskId());

    }

    @Subscriber
    public void startDownload(StartDownloadEvent startDownloadEvent) {
        mPresenter.startDownload(startDownloadEvent.getTaskId(), startDownloadEvent.getUrl());
    }


}
