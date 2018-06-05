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
package com.wangyuelin.downloader.di.module;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.di.scope.FragmentScope;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wangyuelin.downloader.mvp.Home.m.HomeContentModel;
import com.wangyuelin.downloader.mvp.Home.m.HomeModel;
import com.wangyuelin.downloader.mvp.contract.HomeContentContract;
import com.wangyuelin.downloader.mvp.contract.HomeContract;

import dagger.Module;
import dagger.Provides;


@Module
public class HomeContentModule {
    private HomeContentContract.View view;

    /**
     * 构建 UserModule 时,将 View 的实现类传进来,这样就可以提供 View 的实现类给 Presenter
     *
     * @param view
     */
    public HomeContentModule(HomeContentContract.View view) {
        this.view = view;
    }

    @FragmentScope
    @Provides
    HomeContentContract.View provideHomeView() {
        return this.view;
    }

    @FragmentScope
    @Provides
    HomeContentContract.Model provideHomeModel(HomeContentModel model) {
        return model;
    }

    @FragmentScope
    @Provides
    RxPermissions provideRxPermissions() {
        return new RxPermissions(view.getActivity());
    }


}
