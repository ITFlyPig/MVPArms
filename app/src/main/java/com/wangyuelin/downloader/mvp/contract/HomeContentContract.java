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
package com.wangyuelin.downloader.mvp.contract;

import android.app.Activity;

import com.jess.arms.mvp.IModel;
import com.jess.arms.mvp.IView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wangyuelin.downloader.mvp.Home.adapter.ContentTabFragmentsAdapter;
import com.wangyuelin.downloader.mvp.Home.adapter.LeftMenuAdapter;
import com.wangyuelin.downloader.mvp.model.entity.LeftMeunItemBean;
import com.xunlei.downloadlib.parameter.XLTaskInfo;

import java.util.List;

/**
 * ================================================
 * 展示 Contract 的用法
 *
 * @see <a href="https://github.com/JessYanCoding/MVPArms/wiki#2.4.1">Contract wiki 官方文档</a>
 * Created by JessYan on 09/04/2016 10:47
 * <a href="mailto:jess.yan.effort@gmail.com">Contact me</a>
 * <a href="https://github.com/JessYanCoding">Follow me</a>
 * ================================================
 */
public interface HomeContentContract {
    interface View extends IView {
        Activity getActivity();
        //申请权限
        RxPermissions getRxPermissions();

        void addTask(XLTaskInfo info, String url);

        void setAdapter(ContentTabFragmentsAdapter adapter);

    }
    interface Model extends IModel{
        //获得菜单数据
        List<String> getTabsName();
    }
}
