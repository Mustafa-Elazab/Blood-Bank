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
import com.example.mustafa.bloodbank.adapter.FavouriteAdapter;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.models.artical.Artical;
import com.example.mustafa.bloodbank.data.models.artical.ArticalData;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.fragment.BaseFragment;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.home.homeFragment;

import java.util.ArrayList;
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
public class FavouriteFragment extends BaseFragment {
    @BindView(R.id.Fragment_favourite_recyclerView)
    RecyclerView FragmentFavouriteRecyclerView;
    Unbinder unbinder;
    private FavouriteAdapter adapter;
    private API ApiServices;
    private String api_token="";
    private List<ArticalData> FavouriteArtical=new ArrayList<>();

    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SetUpAvtivity();
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        unbinder = ButterKnife.bind(this, view);
        ApiServices = RetrofitClient.getClient().create(API.class);
        api_token = SharedPreferencesManger.LoadData(getActivity(), USER_API_TOKEN);
        getData();
        SetupRecyclerView();
        return view;
    }

    private void getData() {
        ApiServices.getallfavourite(api_token).enqueue(new Callback<Artical>() {
            @Override
            public void onResponse(Call<Artical> call, Response<Artical> response) {
                try {
                if (response.body().getStatus() == 1) {

                    List<ArticalData> data = response.body().getData().getData();
                    FavouriteArtical.addAll(data);
                    adapter.notifyDataSetChanged();
                }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Artical> call, Throwable t) {

            }
        });

    }

    private void SetupRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        FragmentFavouriteRecyclerView.setLayoutManager(manager);
        adapter = new FavouriteAdapter(getActivity(),getActivity(),FavouriteArtical);
        FragmentFavouriteRecyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

