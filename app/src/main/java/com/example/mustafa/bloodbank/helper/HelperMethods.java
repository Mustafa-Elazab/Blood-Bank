package com.example.mustafa.bloodbank.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;


public class HelperMethods {
    private static ProgressDialog checkDialog;

    public static void replace(Fragment fragment, FragmentManager supportFragmentManager, int id, TextView tool_bar_title,
                               String title) {


        FragmentTransaction beginTransaction = supportFragmentManager.beginTransaction();
        beginTransaction.replace(id, fragment);
        beginTransaction.addToBackStack(null);

        if (tool_bar_title != null) {
            tool_bar_title.setText(title);
        }
        beginTransaction.commit();
    }

    public static void onLoadImageFromUrl(ImageView imageView, String URl, Context context) {

        Glide.with(context)

                .load(URl)

                .into(imageView);

    }

    public static void setInitRecyclerViewAsLinearLayoutManager(Context context, RecyclerView recyclerView, LinearLayoutManager linearLayoutManager) {

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(linearLayoutManager);

    }

    public static void setInitRecyclerViewAsGridLayoutManager(Activity activity, RecyclerView recyclerView, GridLayoutManager gridLayoutManager, int numberOfColumns) {

        gridLayoutManager = new GridLayoutManager(activity, numberOfColumns);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(gridLayoutManager);

    }

    public static void disappearKeypad(Activity activity, View v) {

        try {

            if (v != null) {

                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

            }

        } catch (Exception e) {
        }

    }

    public static void showProgressDialog(Activity activity, String title) {

        try {

            if (checkDialog == null) {

                checkDialog = new ProgressDialog(activity);

                checkDialog.setMessage(title);

                checkDialog.setIndeterminate(false);

                checkDialog.setCancelable(false);
            }

            checkDialog.show();
        } catch (Exception e) {
        }

    }

    public static void customToast(Activity activity,String title){

        try{

            Toast.makeText(activity, title, Toast.LENGTH_SHORT).show();

        }catch (Exception e){

        }

    }
    public static void dismissProgressDialog() {

        try {

            if (checkDialog != null && checkDialog.isShowing()) {

                checkDialog.dismiss();

            }

        } catch (Exception e) {
        }

    }

}
