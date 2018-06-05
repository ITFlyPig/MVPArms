/*
 * Copyright 2017 JessYan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wangyuelin.downloader.mvp.Home.p;

import android.app.Application;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.SupportActivity;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.AppManager;
import com.jess.arms.mvp.BasePresenter;
import com.jess.arms.utils.PermissionUtil;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wangyuelin.downloader.app.utils.Constant;
import com.wangyuelin.downloader.app.utils.FileUtil;
import com.wangyuelin.downloader.mvp.Home.adapter.ContentTabFragmentsAdapter;
import com.wangyuelin.downloader.mvp.Home.adapter.LeftMenuAdapter;
import com.wangyuelin.downloader.mvp.contract.HomeContentContract;
import com.wangyuelin.downloader.mvp.contract.HomeContract;
import com.wangyuelin.downloader.mvp.model.entity.LeftMeunItemBean;
import com.xunlei.downloadlib.XLTaskHelper;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

import java.util.List;

import javax.inject.Inject;

import me.jessyan.rxerrorhandler.core.RxErrorHandler;


@FragmentScope
public class HomeContentPresenter extends BasePresenter<HomeContentContract.Model, HomeContentContract.View> {
    @Inject
    RxErrorHandler mErrorHandler;
    @Inject
    AppManager mAppManager;
    @Inject
    Application mApplication;


    private ContentTabFragmentsAdapter mContentAdapter;



    @Inject
    public HomeContentPresenter(HomeContentContract.Model model, HomeContentContract.View rootView) {
        super(model, rootView);
    }

    /**
     * 使用 2017 Google IO 发布的 Architecture Components 中的 Lifecycles 的新特性 (此特性已被加入 Support library)
     * 使 {@code Presenter} 可以与 {@link SupportActivity} 和 {@link Fragment} 的部分生命周期绑定
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    void onCreate() {

    }


    /**
     * 初始化
     */
    public void initPagerAdapter(FragmentManager fm) {
        List<String> titles = mModel.getTabsName();
        mContentAdapter = new ContentTabFragmentsAdapter(fm, titles);
        mRootView.setAdapter(mContentAdapter);

    }


    public void addDownloadTask(String url) {
        //请求外部存储权限用于适配android6.0的权限管理机制
        PermissionUtil.externalStorage(new PermissionUtil.RequestPermission() {
            @Override
            public void onRequestPermissionSuccess() {
                //request permission success, do something.
                //开始下载
                long taskId = 0;
                try {
                    FileUtil.checkDir(Constant.SAVE_PATH);
                    String fileName = XLTaskHelper.instance(mApplication).getFileName(url);
                    taskId = XLTaskHelper.instance(mApplication).addThunderTask(url,Constant.SAVE_PATH, fileName);
                    XLTaskInfo taskInfo = XLTaskHelper.instance(mApplication).getTaskInfo(taskId);
                    mRootView.addTask(taskInfo, url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRequestPermissionFailure(List<String> permissions) {
                mRootView.showMessage("Request permissions failure");
            }

            @Override
            public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {
                mRootView.showMessage("Need to go to the settings");
            }
        }, mRootView.getRxPermissions(), mErrorHandler);

    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mErrorHandler = null;
        this.mAppManager = null;
        this.mApplication = null;
    }


}
