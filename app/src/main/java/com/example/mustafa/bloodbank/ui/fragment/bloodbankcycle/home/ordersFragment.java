package com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.adapter.ORDERS_FRAGMENT_ADAPTER;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.model.bloodtypes.Bloodtypes;
import com.example.mustafa.bloodbank.data.model.donation_request_create.DonationRequest;
import com.example.mustafa.bloodbank.data.model.donationrequests.DonationRequests;
import com.example.mustafa.bloodbank.data.model.governorates.Governorates;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;

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
public class ordersFragment extends Fragment {


    @BindView(R.id.Fragment_orders_sp_bloodtype)
    Spinner FragmentOrdersSpBloodtype;
    @BindView(R.id.Fragment_orders_sp_city)
    Spinner FragmentOrdersSpCity;
    @BindView(R.id.Fragment_orders_recyclerview)
    RecyclerView FragmentOrdersRecyclerview;
    Unbinder unbinder;
    @BindView(R.id.Fragment_orders_img_search)
    ImageView FragmentOrdersImgSearch;
    private ORDERS_FRAGMENT_ADAPTER adapter;
    private API ApiServices;
    private String api_token;
    private int blood_id, city_id;
    private int gov_id;
    private List<DonationRequest> data = new ArrayList<>();

    public ordersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        unbinder = ButterKnife.bind(this, view);

        ApiServices = RetrofitClient.getClient().create(API.class);
        api_token = SharedPreferencesManger.LoadData(getActivity(), USER_API_TOKEN);
        SetupRecyclerView();
        getData();
        getBloodtype();
        getCity();
        return view;
    }

    private void getCity() {

        ApiServices.getgovernorates().enqueue(new Callback<Governorates>() {
            @Override
            public void onResponse(Call<Governorates> call, Response<Governorates> response) {

                if (response.body().getStatus() == 1) {

                    try {

                        List<String> gov_names = new ArrayList<>();
                        final List<Integer> gov_ids = new ArrayList<>();

                        gov_names.add("كل المحافظات");
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
            public void onFailure(Call<Governorates> call, Throwable t) {

            }
        });
    }

    private void getBloodtype() {

        ApiServices.getbloodtype().enqueue(new Callback<Bloodtypes>() {
            @Override
            public void onResponse(Call<Bloodtypes> call, Response<Bloodtypes> response) {
                if (response.body().getStatus() == 1) {

                    try {

                        List<String> bloodtype = new ArrayList<>();
                        final List<Integer> bloodids = new ArrayList<>();
                        bloodtype.add(" كل الفصائل");
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
            public void onFailure(Call<Bloodtypes> call, Throwable t) {

            }
        });
    }

    private void getData() {

        ApiServices.getdonationrequest(api_token, 1).enqueue(
                new Callback<DonationRequests>() {
                    @Override
                    public void onResponse(Call<DonationRequests> call, Response<DonationRequests> response) {
                        if (response.body().getStatus() == 1) {

                            try {
                                DonationRequests body = response.body();
                                viewResponse(body);

                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DonationRequests> call, Throwable t) {

                    }
                }
        );
    }

    private void viewResponse(DonationRequests body) {

        List<DonationRequest> data = body.getData().getData();

        adapter.sendDataToAdapter(data);
        adapter.notifyDataSetChanged();

    }

    private void SetupRecyclerView() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        FragmentOrdersRecyclerview.setLayoutManager(manager);
        adapter = new ORDERS_FRAGMENT_ADAPTER(getContext(),getActivity());
        FragmentOrdersRecyclerview.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void getDonationFilters() {

        ApiServices.getorderfilter(api_token, blood_id, city_id, 1).enqueue(new Callback<DonationRequests>() {
            @Override
            public void onResponse(Call<DonationRequests> call, Response<DonationRequests> response) {
                if (response.body().getStatus() == 1) {

                    data.addAll(response.body().getData().getData());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<DonationRequests> call, Throwable t) {

            }
        });


    }

    @OnClick(R.id.Fragment_orders_img_search)
    public void onViewClicked() {

        if(blood_id==0 &&gov_id==0){

            getData();
        }
        else {
            getDonationFilters();
        }
    }
}
