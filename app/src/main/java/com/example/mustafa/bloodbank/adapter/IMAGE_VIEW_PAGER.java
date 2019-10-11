package com.example.mustafa.bloodbank.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.mustafa.bloodbank.R;

public class IMAGE_VIEW_PAGER extends PagerAdapter {

    private Context mContext;
    private int[] images=new int[]{R.drawable.slider2,R.drawable.slider};

    public IMAGE_VIEW_PAGER(Context context){
        mContext=context;
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        ImageView imageView=new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(images[position]);
        container.addView(imageView,0);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((ImageView)object);
    }
}
