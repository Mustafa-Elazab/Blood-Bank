package com.example.mustafa.bloodbank.ui.activity.splashcycle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.adapter.IMAGE_VIEW_PAGER;
import com.example.mustafa.bloodbank.ui.activity.UserCycleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SliderActivity extends AppCompatActivity {

    @BindView(R.id.Slider_viewpager)
    ViewPager SliderViewpager;
    @BindView(R.id.Slider_btn_skip)
    Button SliderBtnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slider);
        ButterKnife.bind(this);

        IMAGE_VIEW_PAGER image_view_pager=new IMAGE_VIEW_PAGER(this);

        SliderViewpager.setAdapter(image_view_pager);

    }

    @OnClick(R.id.Slider_btn_skip)
    public void onViewClicked() {

        Intent intent=new Intent(SliderActivity.this, UserCycleActivity.class);
        startActivity(intent);
        finish();
    }
}
