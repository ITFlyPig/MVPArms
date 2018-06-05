package com.wangyuelin.downloader.di.component;

import dagger.Component;

import com.jess.arms.di.component.AppComponent;

import com.wangyuelin.downloader.di.module.DowloadListFragmentModule;

import com.jess.arms.di.scope.FragmentScope;
import com.wangyuelin.downloader.mvp.Home.v.DowloadListFragmentFragment;

@FragmentScope
@Component(modules = DowloadListFragmentModule.class, dependencies = AppComponent.class)
public interface DowloadListFragmentComponent {
    void inject(DowloadListFragmentFragment fragment);
}