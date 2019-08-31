package com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.adapter.FAVOURITE_FRAGMENT_ADAPTER;
import com.example.mustafa.bloodbank.adapter.ORDERS_FRAGMENT_ADAPTER;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.model.donationrequests.DonationRequests;
import com.example.mustafa.bloodbank.data.model.myfavourites.Datum;
import com.example.mustafa.bloodbank.data.model.myfavourites.MyFavourites;
import com.example.mustafa.bloodbank.data.model.posttogglefavourite.Data;
import com.example.mustafa.bloodbank.data.model.posttogglefavourite.PostToggleFavourite;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;

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
public class FavouriteFragment extends Fragment {



    @BindView(R.id.Fragment_favourite_recyclerView)
    RecyclerView FragmentFavouriteRecyclerView;
    Unbinder unbinder;
    private FAVOURITE_FRAGMENT_ADAPTER adapter;
    private API ApiServices;
    private String api_token;

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        unbinder = ButterKnife.bind(this, view);
        ApiServices = RetrofitClient.getClient().create(API.class);
        api_token = SharedPreferencesManger.LoadData(getActivity(), USER_API_TOKEN);
        SetupRecyclerView();
        getData();
        return view;
    }

    private void getData() {
        ApiServices.getallfavourite(api_token).enqueue(new Callback<MyFavourites>() {
            @Override
            public void onResponse(Call<MyFavourites> call, Response<MyFavourites> response) {

                if (response.body().getStatus() == 1) {

                    try {
                        MyFavourites body = response.body();
                        viewresponse(body);
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MyFavourites> call, Throwable t) {

            }
        });

    }

    private void viewresponse(MyFavourites body) {

        List<Datum> data = body.getData().getData();
        adapter.sendDataToAdapter(data);
        adapter.notifyDataSetChanged();
    }


    private void SetupRecyclerView() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        FragmentFavouriteRecyclerView.setLayoutManager(manager);
        adapter = new FAVOURITE_FRAGMENT_ADAPTER(getContext(),getActivity());
        FragmentFavouriteRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

