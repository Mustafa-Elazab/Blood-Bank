package com.example.mustafa.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.ui.activity.HomeActivity;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.home.ordersFragment;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.home.postsFragment;

import java.util.ArrayList;
import java.util.List;

public class VIEW_PAGER_ADAPTER extends FragmentPagerAdapter {

    private Context activity;

    Fragment fragment[] = {new postsFragment(), new ordersFragment()};

    List<String> title = new ArrayList<>();

    public VIEW_PAGER_ADAPTER(FragmentManager fm, Context activity) {
        super(fm);
        this.activity = activity;
        title.add(activity.getString(R.string.articals));
        title.add(activity.getString(R.string.donation_request));
    }

    @Override
    public Fragment getItem(int i) {
        return fragment[i];
    }

    @Override
    public int getCount() {
        return fragment.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return title.get(position);
    }
}