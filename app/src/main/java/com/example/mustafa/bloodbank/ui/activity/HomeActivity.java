package com.example.mustafa.bloodbank.ui.activity;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustafa.bloodbank.data.model.notifications.DataNotify;
import com.example.mustafa.bloodbank.data.model.notifications.Notifications;
import com.example.mustafa.bloodbank.data.model.notificationscount.NotificationsCount;
import com.example.mustafa.bloodbank.data.model.registertoken.RegisterToken;
import com.example.mustafa.bloodbank.data.model.removetoken.RemoveToken;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.AboutAppFragment;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.ContactUsFragment;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.EditProfileFragment;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.FavouriteFragment;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.NotificationsFragment;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.NotificationsettingsFragment;
import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;

import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.home.homeFragment;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.USER_API_TOKEN;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.Activity_home_img_menu)
    ImageView ActivityHomeImgMenu;
    @BindView(R.id.Activity_home_img_notifications)
    ImageView ActivityHomeImgNotifications;
    @BindView(R.id.Tool_bar_title)
    TextView ToolBarTitle;
    @BindView(R.id.Activity_home_img_back)
    ImageView ActivityHomeImgBack;
    @BindView(R.id.Activity_home_relative)
    RelativeLayout ActivityHomeRelative;
    @BindView(R.id.Activity_home_Frame)
    FrameLayout ActivityHomeFrame;
    public DrawerLayout drawer;
    @BindView(R.id.notificationCount)
    TextView notificationCount;
    private API ApiServices;
    private String Apitokens;
    private String bodyMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank);
        ButterKnife.bind(this);


        ApiServices = RetrofitClient.getClient().create(API.class);

        getApiNotifications();
        getFireBaseNotifications();
        removeFireBaseNotifications();
        final String api_token=SharedPreferencesManger.LoadData(HomeActivity.this,USER_API_TOKEN);
        Apitokens="W4mx3VMIWetLcvEcyF554CfxjZHwdtQldbdlCl2XAaBTDIpNjKO1f7CHuwKl";
        ApiServices.getnotificationscount(Apitokens).enqueue(new Callback<NotificationsCount>() {
            @Override
            public void onResponse(Call<NotificationsCount> call, Response<NotificationsCount> response) {

                try{

                    if (response.body().getStatus()==1) {



                            String getnotificationcount = String.valueOf(response.body().getData().getNotificationsCount());
                            Toast.makeText(HomeActivity.this, api_token, Toast.LENGTH_SHORT).show();
                            Toast.makeText(HomeActivity.this, getnotificationcount, Toast.LENGTH_SHORT).show();
                            notificationCount.setText(getnotificationcount);


                    }

                }catch (Exception e)
                {
                    Toast.makeText(HomeActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationsCount> call, Throwable t) {

            }
        });
        if (savedInstanceState == null) {
            homeFragment homeFragment = new homeFragment();
            HelperMethods.replace(homeFragment, getSupportFragmentManager(), R.id.frame_home_cycle, null, null);

        }

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void getApiNotifications() {

        String api_token = "W4mx3VMIWetLcvEcyF554CfxjZHwdtQldbdlCl2XAaBTDIpNjKO1f7CHuwKl";
        ApiServices.getnotifications(api_token).enqueue(new Callback<Notifications>() {
            @Override
            public void onResponse(Call<Notifications> call, Response<Notifications> response) {

                try {

                    if (response.body().getStatus() == 1) {


                        Notifications body = response.body();

                        bodyMsg = body.getMsg();


                    }
                } catch (Exception e) {
                    Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();                }
            }

            @Override
            public void onFailure(Call<Notifications> call, Throwable t) {

            }
        });

    }



    private void removeFireBaseNotifications() {

        String token = FirebaseInstanceId.getInstance().getToken();

        ApiServices.RemoveToken(token,Apitokens).enqueue(
                new Callback<RemoveToken>() {
                    @Override
                    public void onResponse(Call<RemoveToken> call, Response<RemoveToken> response) {
                        try{



                        }catch (Exception e){
                            Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<RemoveToken> call, Throwable t) {

                    }
                }
        );
    }

    private void getFireBaseNotifications() {

        final String token = FirebaseInstanceId.getInstance().getToken();
        ApiServices.getRegisterToken(token,Apitokens,"android")
        .enqueue(new Callback<RegisterToken>() {
            @Override
            public void onResponse(Call<RegisterToken> call, Response<RegisterToken> response) {
                try{


                }catch (Exception e){

                    Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RegisterToken> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        homeFragment homeFragment = new homeFragment();
        HelperMethods.replace(homeFragment, getSupportFragmentManager(), R.id.frame_home_cycle, null, null);
        ActivityHomeImgMenu.setVisibility(View.VISIBLE);
        ActivityHomeImgNotifications.setVisibility(View.VISIBLE);
        ActivityHomeImgBack.setVisibility(View.INVISIBLE);
        ToolBarTitle.setText("");
        ActivityHomeFrame.setVisibility(View.INVISIBLE);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {

            ActivityHomeImgMenu.setVisibility(View.INVISIBLE);
            ActivityHomeImgNotifications.setVisibility(View.INVISIBLE);
            ToolBarTitle.setText("تعديل البيانات");
            ActivityHomeFrame.setVisibility(View.INVISIBLE);
            notificationCount.setVisibility(View.INVISIBLE);
            EditProfileFragment editProfileFragment = new EditProfileFragment();
            HelperMethods.replace(editProfileFragment, getSupportFragmentManager(), R.id.frame_home_cycle, null, null);


        }

        if (id == R.id.nav_notificat) {

            ActivityHomeImgMenu.setVisibility(View.INVISIBLE);
            ActivityHomeImgNotifications.setVisibility(View.INVISIBLE);
            ActivityHomeImgBack.setVisibility(View.VISIBLE);
            ToolBarTitle.setText("اعدادات الاشعارات");
            ActivityHomeFrame.setVisibility(View.INVISIBLE);
            NotificationsettingsFragment notificationsettingsFragment = new NotificationsettingsFragment();
            HelperMethods.replace(notificationsettingsFragment, getSupportFragmentManager(), R.id.frame_home_cycle, null, null);


        }
        if (id == R.id.nav_favourite) {

            ActivityHomeImgMenu.setVisibility(View.INVISIBLE);
            ActivityHomeImgNotifications.setVisibility(View.INVISIBLE);
            ActivityHomeImgBack.setVisibility(View.VISIBLE);
            ToolBarTitle.setText("المفضلة");
            notificationCount.setVisibility(View.INVISIBLE);
            ActivityHomeFrame.setVisibility(View.INVISIBLE);
            FavouriteFragment favouriteFragment = new FavouriteFragment();
            HelperMethods.replace(favouriteFragment, getSupportFragmentManager(), R.id.frame_home_cycle, null, null);


        }


        if (id == R.id.nav_home) {

            ActivityHomeImgMenu.setVisibility(View.VISIBLE);
            ActivityHomeImgNotifications.setVisibility(View.VISIBLE);
            ActivityHomeImgBack.setVisibility(View.INVISIBLE);
            ToolBarTitle.setText("");
            ActivityHomeFrame.setVisibility(View.INVISIBLE);
            homeFragment homeFragment = new homeFragment();
            HelperMethods.replace(homeFragment, getSupportFragmentManager(), R.id.frame_home_cycle, null, null);

        }


        if (id == R.id.nav_contact) {

            ActivityHomeImgMenu.setVisibility(View.INVISIBLE);
            ActivityHomeImgNotifications.setVisibility(View.INVISIBLE);
            ActivityHomeImgBack.setVisibility(View.VISIBLE);
            ToolBarTitle.setText("اتصل بنا");
            notificationCount.setVisibility(View.INVISIBLE);
            ActivityHomeFrame.setVisibility(View.INVISIBLE);
            ContactUsFragment contactUsFragment = new ContactUsFragment();
            HelperMethods.replace(contactUsFragment, getSupportFragmentManager(), R.id.frame_home_cycle, null, null);


        }


        if (id == R.id.nav_aboutapp) {

            ActivityHomeImgMenu.setVisibility(View.INVISIBLE);
            ActivityHomeImgNotifications.setVisibility(View.INVISIBLE);
            ActivityHomeImgBack.setVisibility(View.VISIBLE);
            ToolBarTitle.setText("عن التطبيق");
            notificationCount.setVisibility(View.INVISIBLE);
            ActivityHomeFrame.setVisibility(View.INVISIBLE);
            AboutAppFragment aboutAppFragment = new AboutAppFragment();
            HelperMethods.replace(aboutAppFragment, getSupportFragmentManager(), R.id.frame_home_cycle, null, null);


        }


        if (id == R.id.nav_store) {


        }


        if (id == R.id.nav_signout) {
            getUserExit();
            removeFireBaseNotifications();
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getUserExit() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("هل تريد الخروج ؟")
                .setCancelable(false)
                .setPositiveButton("نعم ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(HomeActivity.this, UserCycleActivity.class);
                        SharedPreferencesManger.clean(HomeActivity.this);
                        startActivity(intent);

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


    @OnClick({R.id.Activity_home_img_menu, R.id.Activity_home_img_notifications})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Activity_home_img_menu:
                drawer.openDrawer(Gravity.START);
                break;
            case R.id.Activity_home_img_notifications:

                NotificationsFragment notificationsFragment=new NotificationsFragment();
                ActivityHomeImgMenu.setVisibility(View.INVISIBLE);
                ActivityHomeImgNotifications.setVisibility(View.INVISIBLE);
                ActivityHomeImgBack.setVisibility(View.VISIBLE);
                ToolBarTitle.setText("التنبيهات");
                notificationCount.setVisibility(View.INVISIBLE);
                ActivityHomeFrame.setVisibility(View.INVISIBLE);
                HelperMethods.replace(notificationsFragment,getSupportFragmentManager(),R.id.frame_home_cycle,null,null);
                break;
            case R.id.Activity_home_img_back:

                onBackPressed();
                break;
        }
    }

    public void getVisibility(int visibility) {
        ActivityHomeFrame.setVisibility(visibility);
    }
}
