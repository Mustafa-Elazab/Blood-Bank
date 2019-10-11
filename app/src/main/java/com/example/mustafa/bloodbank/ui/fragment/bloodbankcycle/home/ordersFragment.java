package com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.adapter.DonateAdapter;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.models.donate.Donate;
import com.example.mustafa.bloodbank.data.models.donate.DonateData;
import com.example.mustafa.bloodbank.data.models.gerneral.GeneralResponse;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.helper.OnEndless;
import com.example.mustafa.bloodbank.ui.fragment.BaseFragment;

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
public class ordersFragment extends BaseFragment {


    @BindView(R.id.Fragment_orders_sp_bloodtype)
    Spinner FragmentOrdersSpBloodtype;
    @BindView(R.id.Fragment_orders_sp_city)
    Spinner FragmentOrdersSpCity;
    @BindView(R.id.Fragment_orders_recyclerview)
    RecyclerView FragmentOrdersRecyclerview;
    @BindView(R.id.Fragment_orders_img_search)
    ImageView FragmentOrdersImgSearch;
    Unbinder unbinder;
    private DonateAdapter adapter;
    private API ApiServices;
    private String api_token="";
    private int blood_id, city_id;
    private int gov_id;
    private List<DonateData> DonateData = new ArrayList<>();
    private Integer max = 0;
    private OnEndless onEndless;
    private int finalCurrent_page = 0;
    private boolean checkFilterPost = true;
    private OnEndless onEndlesslistener;
    private int maxPage;

    public ordersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SetUpAvtivity();
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        unbinder = ButterKnife.bind(this, view);
        ApiServices = RetrofitClient.getClient().create(API.class);
        api_token = SharedPreferencesManger.LoadData(getActivity(), USER_API_TOKEN);
        getData(1);
        getBloodtype();
        getCity();
        SetupRecyclerView();
        return view;
    }

    private void getCity() {

        ApiServices.getgovernorates().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                if (response.body().getStatus() == 1) {
                    try {
                        List<String> gov_names = new ArrayList<>();
                        final List<Integer> gov_ids = new ArrayList<>();
                        gov_names.add(getString(R.string.allGov));
                        gov_ids.add(0);
                        for (int i = 0; i < response.body().getData().size(); i++) {

                            gov_names.add(response.body().getData().get(i).getName());
                            gov_ids.add(response.body().getData().get(i).getId());

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    getActivity(), android.R.layout.simple_spinner_item
                                    , gov_names
                            );

                            FragmentOrdersSpCity.setAdapter(adapter);
                            FragmentOrdersSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    gov_id = gov_ids.get(position);
                                }
                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                    } catch (Exception e) {

                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    private void getBloodtype() {

        ApiServices.getbloodtype().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body().getStatus() == 1) {

                    try {

                        List<String> bloodtype = new ArrayList<>();
                        final List<Integer> bloodids = new ArrayList<>();
                        bloodtype.add(getString(R.string.AllBlood));
                        bloodids.add(0);
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            bloodtype.add(response.body().getData().get(i).getName());
                            bloodids.add(response.body().getData().get(i).getId());
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                    getActivity(), android.R.layout.simple_spinner_item, bloodtype
                            );

                            FragmentOrdersSpBloodtype.setAdapter(adapter);
                            FragmentOrdersSpBloodtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    blood_id = bloodids.get(position);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    private void getData(final int page) {
        ApiServices.getdonationrequest(api_token, page).enqueue(
                new Callback<Donate>() {
                    @Override
                    public void onResponse(Call<Donate> call, Response<Donate> response) {
                        Log.i("onOrderData",response.body().getData().getData().toString());
                        try {
                        if (response.body().getStatus() == 1) {
                            List<DonateData> data = response.body().getData().getData();
                            max = response.body().getData().getLastPage();
                            DonateData.addAll(data);
                            adapter.notifyDataSetChanged();

                        }
                            }catch (Exception e) {
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Donate> call, Throwable t) {

                    }
                }
        );
    }
    private void SetupRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        FragmentOrdersRecyclerview.setLayoutManager(linearLayoutManager);

        onEndless = new OnEndless(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= max || max != 0 || current_page == 1) {
                    if (checkFilterPost) {
                        getData(1);
                    } else {
                        getDonationFilters(1);
                    }
                }
            }
        };

        FragmentOrdersRecyclerview.addOnScrollListener(onEndless);
        adapter = new DonateAdapter(getActivity(),getActivity(),DonateData);
        FragmentOrdersRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void getDonationFilters(int page) {
        ApiServices.getorderfilter(api_token, blood_id, city_id, page).enqueue(new Callback<Donate>() {
            @Override
            public void onResponse(Call<Donate> call, Response<Donate> response) {
                if (response.body().getStatus() == 1) {
                    DonateData.addAll(response.body().getData().getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<Donate> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.Fragment_orders_img_search)
    public void onViewClicked() {
        if(blood_id==0 &&gov_id==0){
            getData(1);
        }
        else {
            getDonationFilters(1);
        }
    }
    @Override
    public void onBack() {
        homeFragment homeFragment = new homeFragment();
        HelperMethods.replace(homeFragment,getActivity().getSupportFragmentManager(), R.id.frame_home_cycle, null, null);
    }
}
