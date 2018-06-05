package com.wangyuelin.downloader.mvp.Home.adapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wangyuelin.downloader.mvp.Home.v.DowloadListFragmentFragment;

import java.util.ArrayList;
import java.util.List;

public class ContentTabFragmentsAdapter extends FragmentPagerAdapter {
    private List<String> names;
    private List<Fragment> mFragments;

    public ContentTabFragmentsAdapter(FragmentManager fm, List<String> names) {
        super(fm);
        this.names = names;
        mFragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        DowloadListFragmentFragment fragment = null;
        if (position > 0 && position < mFragments.size()) {
            fragment = (DowloadListFragmentFragment) mFragments.get(position);
        }
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putString("title", names.get(position));
            fragment = DowloadListFragmentFragment.newInstance(bundle);
            mFragments.add(fragment);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return names == null ? 0 : names.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return names.get(position);
    }
}
