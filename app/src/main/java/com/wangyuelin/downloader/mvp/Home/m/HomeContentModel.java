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
package com.wangyuelin.downloader.mvp.Home.m;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.OnLifecycleEvent;

import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.integration.IRepositoryManager;
import com.jess.arms.mvp.BaseModel;
import com.wangyuelin.downloader.app.utils.Menus;
import com.wangyuelin.downloader.mvp.contract.HomeContentContract;
import com.wangyuelin.downloader.mvp.contract.HomeContract;
import com.wangyuelin.downloader.mvp.model.entity.LeftMeunItemBean;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;


@ActivityScope
public class HomeContentModel extends BaseModel implements HomeContentContract.Model {


    @Inject
    public HomeContentModel(IRepositoryManager repositoryManager) {
        super(repositoryManager);
    }



    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    void onPause() {
        Timber.d("Release Resource");
    }



}
