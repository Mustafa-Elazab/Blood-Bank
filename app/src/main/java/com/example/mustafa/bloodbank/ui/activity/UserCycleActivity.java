package com.example.mustafa.bloodbank.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.fragment.usercycle.LoginFragment;
import com.example.mustafa.bloodbank.ui.fragment.usercycle.RegisterFragment;

public class UserCycleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_cycle);

        LoginFragment loginFragment=new LoginFragment();
        HelperMethods.replace(loginFragment,getSupportFragmentManager(),R.id.frame_user_cycle,null,null);

    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
