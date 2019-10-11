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
import com.example.mustafa.bloodbank.adapter.AdapterGridView;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.models.gerneral.GeneralResponse;
import com.example.mustafa.bloodbank.data.models.gerneral.GeneralResponseData;
import com.example.mustafa.bloodbank.data.models.notificationssettings.NotificationsSettings;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.fragment.BaseFragment;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.home.homeFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.USER_API_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsettingsFragment extends BaseFragment {
    @BindView(R.id.settingNotificationFragmentBloodTypeGrid)
    GridView settingNotificationFragmentBloodTypeGrid;
    @BindView(R.id.settingNotificationFragmentGovernortesGrid)
    GridView settingNotificationFragmentGovernortesGrid;
    @BindView(R.id.settingNotificationFragmentSaveBtn)
    Button settingNotificationFragmentSaveBtn;
    private AdapterGridView adapterGovernortGridView;
    private AdapterGridView adapterBloodTypeGridView;
    private List<GeneralResponseData> governortareGeneratedModelArrayList = new ArrayList<>();
    // var adapter grid view  blood Type
    private List<GeneralResponseData> bloodTypeGeneratedModelArrayList = new ArrayList<>();
    private List<Integer> idBloodType = new ArrayList<>();
    private List<Integer> idGovernorates = new ArrayList<>();
    private API ApiServices;
    Unbinder unbinder;

    public NotificationsettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SetUpAvtivity();
        View view = inflater.inflate(R.layout.fragment_notificationssettings, container, false);
        unbinder = ButterKnife.bind(this, view);
        ApiServices = RetrofitClient.getClient().create(API.class);
        getDataBloodTypeAndGovernorates();
        return view;
    }

    public void getDataBloodTypeAndGovernorates() {
        // get  PaginationData governorates
        ApiServices.getnotifications_settings(SharedPreferencesManger.LoadData(getActivity(),USER_API_TOKEN)).enqueue(new Callback<NotificationsSettings>() {
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
                    e.getMessage();
                }
            }

            @Override
            public void onFailure(Call<NotificationsSettings> call, Throwable t) {

            }
        });

    }

    // get data Blood_types
    private void BloodTypes() {
        // get data Blood_types
        ApiServices.getbloodtype().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                Log.d("response BloodTypes ", response.body().getMsg());
                try {
                    if (response.body().getStatus() == 1) {
                        bloodTypeGeneratedModelArrayList.addAll(response.body().getData());
                        adapterBloodTypeGridView = new AdapterGridView(getActivity(), bloodTypeGeneratedModelArrayList, idBloodType);
                        settingNotificationFragmentBloodTypeGrid.setAdapter(adapterBloodTypeGridView);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });

    }

    // get data governorates
    private void Governorate() {

        // get data governorates
        ApiServices.getgovernorates().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        governortareGeneratedModelArrayList.addAll(response.body().getData());
                        adapterGovernortGridView = new AdapterGridView(getActivity(), governortareGeneratedModelArrayList, idGovernorates);
                        settingNotificationFragmentGovernortesGrid.setAdapter(adapterGovernortGridView);
                    }

                } catch (Exception e) {
                    e.getMessage();
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.settingNotificationFragmentSaveBtn)
    public void onViewClicked() {

        // get data governorates
        ApiServices.ChangeNotificationsSettings(SharedPreferencesManger.LoadData(getActivity(),USER_API_TOKEN)
                , adapterGovernortGridView.numCheck, adapterBloodTypeGridView.numCheck)
                .enqueue(new Callback<NotificationsSettings>() {
                    @Override
                    public void onResponse(Call<NotificationsSettings> call, Response<NotificationsSettings> response) {
                        Log.d("responses", SharedPreferencesManger.LoadData(getActivity(),USER_API_TOKEN));

                        try {
                            if (response.body().getStatus() == 1) {
                                Toast.makeText(getActivity(), "Msg" + response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                homeFragment homeFragment=new homeFragment();
                                HelperMethods.replace(homeFragment,getActivity().getSupportFragmentManager(),
                                        R.id.Activity_home_Frame,null,null);
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
                    }});
    }

    @Override
    public void onBack() {
        homeFragment homeFragment = new homeFragment();
        HelperMethods.replace(homeFragment,getActivity().getSupportFragmentManager(), R.id.frame_home_cycle, null, null);
    }


}


