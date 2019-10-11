package com.example.mustafa.bloodbank.ui.activity;

import android.support.v7.app.AppCompatActivity;

import com.example.mustafa.bloodbank.ui.fragment.BaseFragment;

public class BaseActivity extends AppCompatActivity {


    public BaseFragment baseFragment;


    public void superonBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        baseFragment.onBack();
    }
}
