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
import com.example.mustafa.bloodbank.adapter.NOTIFICATIONS_ADAPTER;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.model.notifications.DataNotify;
import com.example.mustafa.bloodbank.data.model.notifications.Notifications;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;

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
public class NotificationsFragment extends Fragment {


    @BindView(R.id.Fragment_Notifications_rv)
    RecyclerView FragmentNotificationsRv;

    private API ApiServices;
    private NOTIFICATIONS_ADAPTER adapter;

    Unbinder unbinder;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        adapter = new NOTIFICATIONS_ADAPTER(getContext(), getActivity());
        FragmentNotificationsRv.setAdapter(adapter);


    }

    private void getNotificationsData() {

        String api_token = "W4mx3VMIWetLcvEcyF554CfxjZHwdtQldbdlCl2XAaBTDIpNjKO1f7CHuwKl";
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


}
