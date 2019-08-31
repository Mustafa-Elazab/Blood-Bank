package com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.model.notificationssettings.NotificationsSettings;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.LoadData;
import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.USER_API_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsettingsFragment extends Fragment {


    @BindView(R.id.settingNotificationFragmentBloodTypeGrid)
    GridView settingNotificationFragmentBloodTypeGrid;
    @BindView(R.id.settingNotificationFragmentGovernortesGrid)
    GridView settingNotificationFragmentGovernortesGrid;
    @BindView(R.id.settingNotificationFragmentSaveBtn)
    Button settingNotificationFragmentSaveBtn;
    Unbinder unbinder;
    private API ApiServices;
    private List<Integer> idBloodType;
    private List<Integer> idGovernorates;

    public NotificationsettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notificationssettings, container, false);

        ApiServices = RetrofitClient.getClient().create(API.class);

        getUserNotificationData();
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    private void getUserNotificationData() {


        String api_token = LoadData(getActivity(), USER_API_TOKEN);
        idBloodType = new ArrayList<>();
        idGovernorates = new ArrayList<>();
        ApiServices.getnotifications_settings(api_token).enqueue(new Callback<NotificationsSettings>() {
            @Override
            public void onResponse(Call<NotificationsSettings> call, Response<NotificationsSettings> response) {

                try {

                    if (response.body().getStatus() == 1) {

                        for (int i = 0; i < response.body().getData().getBloodTypes().size(); i++) {
                            idBloodType.add(Integer.valueOf(response.body().getData().getBloodTypes().get(i)));
                        }
                        for (int i = 0; i < response.body().getData().getGovernorates().size(); i++) {
                            idGovernorates.add(Integer.valueOf(response.body().getData().getGovernorates().get(i)));
                        }
                        // get data Blood Types
                        BloodTypes();
                        // get data Governorate
                        Governorate();
                    }

                } catch (Exception e) {

                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NotificationsSettings> call, Throwable t) {

            }
        });
    }

    private void Governorate() {


    }

    private void BloodTypes() {


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.settingNotificationFragmentSaveBtn)
    public void onViewClicked() {

// get data governorates
        ApiServices.ChangeNotificationsSettings(LoadData(getActivity(), USER_API_TOKEN)
                , adapterGovernortGridView.numCheck, adapterBloodTypeGridView.numCheck)
                .enqueue(new Callback<NotificationsSettings>() {
                    @Override
                    public void onResponse(Call<NotificationsSettings> call, Response<NotificationsSettings> response) {
                        Log.d("responses", LoadData(getActivity(), USER_API_TOKEN));

                        try {
                            if (response.body().getStatus() == 1) {
                                Toast.makeText(getActivity(), "Msg" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), "Status" + response.body().getStatus(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.getMessage();
                        }

                    }

                    @Override
                    public void onFailure(Call<NotificationsSettings> call, Throwable t) {
                        Log.d("responsess", t.getMessage());
                    }
                });
    }
    }
}
