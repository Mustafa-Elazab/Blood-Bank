package com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.adapter.NotificationsAdapter;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.models.notifications.DataNotify;
import com.example.mustafa.bloodbank.data.models.notifications.Notifications;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.fragment.BaseFragment;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.home.homeFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.USER_API_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends BaseFragment {
    @BindView(R.id.Fragment_Notifications_rv)
    RecyclerView FragmentNotificationsRv;
    private API ApiServices;
    private NotificationsAdapter adapter;
    Unbinder unbinder;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SetUpAvtivity();
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        unbinder = ButterKnife.bind(this, view);
        ApiServices = RetrofitClient.getClient().create(API.class);
        SetupRecycler();
        getNotificationsData();
        return view;
    }


    private void SetupRecycler() {

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        FragmentNotificationsRv.setLayoutManager(manager);
        adapter = new NotificationsAdapter(getContext(), getActivity());
        FragmentNotificationsRv.setAdapter(adapter);


    }

    private void getNotificationsData() {
        String api_token = SharedPreferencesManger.LoadData(getActivity(),USER_API_TOKEN);
        ApiServices.getnotifications(api_token).enqueue(new Callback<Notifications>() {
            @Override
            public void onResponse(Call<Notifications> call, Response<Notifications> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        Notifications body = response.body();
                        List<DataNotify> data = body.getDataNotifyPage().getData();
                        adapter.getData(data);
                        adapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Notifications> call, Throwable t) {

            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    @Override
    public void onBack() {
        homeFragment homeFragment = new homeFragment();
        HelperMethods.replace(homeFragment,getActivity().getSupportFragmentManager(), R.id.frame_home_cycle, null, null);
    }

}
