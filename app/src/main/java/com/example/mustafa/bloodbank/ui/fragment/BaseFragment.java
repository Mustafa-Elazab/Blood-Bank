package com.example.mustafa.bloodbank.ui.fragment;


import android.support.v4.app.Fragment;

import com.example.mustafa.bloodbank.ui.activity.BaseActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class BaseFragment extends Fragment {


    public BaseActivity baseActivity;

    public void SetUpAvtivity(){

        baseActivity=(BaseActivity)getActivity();
        baseActivity.baseFragment=this;

    }

    public void onBack(){
        baseActivity.superonBackPressed();
    }

}
