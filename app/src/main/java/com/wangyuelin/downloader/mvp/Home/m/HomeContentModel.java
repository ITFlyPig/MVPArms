package com.wangyuelin.downloader.mvp.Home.m;

import android.app.Application;

import com.google.gson.Gson;
import com.jess.arms.di.scope.FragmentScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.wangyuelin.downloader.mvp.contract.DowloadListFragmentContract;
import com.wangyuelin.downloader.mvp.contract.HomeContentContract;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


@FragmentScope
public class HomeContentModel extends BaseModel implements HomeContentContract.Model {
    @Inject
    Gson mGson;
    @Inject
    Application mApplication;

    private List<String> mTabsName;

    @Inject
    public HomeContentModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mGson = null;
        this.mApplication = null;
    }

    @Override
    public List<String> getTabsName() {
        if (mTabsName == null) {
            mTabsName = new ArrayList<>();
            mTabsName.add("正在下载");
            mTabsName.add("已下载");
        }

        return mTabsName;
    }
}