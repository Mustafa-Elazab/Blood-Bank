package com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.fragment.BaseFragment;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.home.homeFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutAppFragment extends BaseFragment {


    public AboutAppFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aboutapp, container, false);
    }
    @Override
    public void onBack() {
        homeFragment homeFragment = new homeFragment();
        HelperMethods.replace(homeFragment,getActivity().getSupportFragmentManager(), R.id.frame_home_cycle, null, null);
    }

}
