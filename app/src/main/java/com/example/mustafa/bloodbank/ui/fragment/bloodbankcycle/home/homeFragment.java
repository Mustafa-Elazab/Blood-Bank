package com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.home;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.adapter.VIEW_PAGER_ADAPTER;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.activity.HomeActivity;
import com.example.mustafa.bloodbank.ui.activity.UserCycleActivity;
import com.example.mustafa.bloodbank.ui.fragment.BaseFragment;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.DonateRequsetFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class homeFragment extends BaseFragment {


    public FrameLayout ActivityHomeFrame;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    Unbinder unbinder;
    @BindView(R.id.Fragment_Home)
    RelativeLayout FragmentHome;
    private Activity activity;
    private HomeActivity homeActivity;
    private Context Context;

    public homeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SetUpAvtivity();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        homeActivity = (HomeActivity) getActivity();
        viewpager.setAdapter(new VIEW_PAGER_ADAPTER(getChildFragmentManager(), getActivity()));
        tabLayout.setupWithViewPager(viewpager);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.Fragment_post_fabbtn)
    public void onViewClicked() {

        DonateRequsetFragment donateRequsetFragment = new DonateRequsetFragment();
        homeActivity.getVisibility(View.VISIBLE);
        HelperMethods.replace(donateRequsetFragment, getActivity().getSupportFragmentManager(), R.id.Activity_home_Frame, null, null);

    }

    @Override
    public void onBack() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("هل تريد الخروج ؟")
                .setCancelable(false)
                .setPositiveButton("نعم ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(getActivity(), UserCycleActivity.class);
                        SharedPreferencesManger.clean(getActivity());
                        getActivity().startActivity(intent);

                    }
                })
                .setNegativeButton("لا", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
