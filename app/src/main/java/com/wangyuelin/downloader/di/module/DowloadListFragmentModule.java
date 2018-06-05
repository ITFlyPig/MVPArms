package com.wangyuelin.downloader.di.module;

import com.jess.arms.di.scope.FragmentScope;

import dagger.Module;
import dagger.Provides;

import com.wangyuelin.downloader.mvp.contract.DowloadListFragmentContract;
import com.wangyuelin.downloader.mvp.Home.m.DowloadListFragmentModel;


@Module
public class DowloadListFragmentModule {
    private DowloadListFragmentContract.View view;

    /**
     * 构建DowloadListFragmentModule时,将View的实现类传进来,这样就可以提供View的实现类给presenter
     *
     * @param view
     */
    public DowloadListFragmentModule(DowloadListFragmentContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    DowloadListFragmentContract.View provideDowloadListFragmentView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    DowloadListFragmentContract.Model provideDowloadListFragmentModel(DowloadListFragmentModel model) {
        return model;
    }
}