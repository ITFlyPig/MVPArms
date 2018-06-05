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
package com.wangyuelin.downloader.di.component;

import com.jess.arms.di.component.AppComponent;
import com.jess.arms.di.scope.ActivityScope;
import com.jess.arms.di.scope.FragmentScope;
import com.wangyuelin.downloader.di.module.HomeContentModule;
import com.wangyuelin.downloader.di.module.HomeModule;
import com.wangyuelin.downloader.mvp.Home.v.HomeActivity;
import com.wangyuelin.downloader.mvp.Home.v.HomeFragment;

import dagger.Component;

@FragmentScope
@Component(modules = HomeContentModule.class, dependencies = AppComponent.class)
public interface HomeContentComponent {
    void inject(HomeFragment fragment);
}
