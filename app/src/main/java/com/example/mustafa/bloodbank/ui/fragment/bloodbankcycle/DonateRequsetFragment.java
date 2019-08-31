package com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.model.bloodtypes.Bloodtypes;
import com.example.mustafa.bloodbank.data.model.cities.Cities;
import com.example.mustafa.bloodbank.data.model.donation_request_create.DonationRequestCreate;
import com.example.mustafa.bloodbank.data.model.governorates.Governorates;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.activity.MapsActivity;
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

import static android.support.constraint.Constraints.TAG;
import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.LATITUDE;
import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.LONGITUDE;
import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.USER_API_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class DonateRequsetFragment extends Fragment {


    @BindView(R.id.Fragment_donate_request_ed_name)
    TextInputLayout FragmentDonateRequestEdName;
    @BindView(R.id.Fragment_donate_request_ed_age)
    TextInputLayout FragmentDonateRequestEdAge;
    @BindView(R.id.Fragment_donate_request_ed_hospital)
    TextInputLayout FragmentDonateRequestEdHospital;
    @BindView(R.id.Fragment_donate_request_ed_hospital_address)
    TextInputLayout FragmentDonateRequestEdHospitalAddress;
    @BindView(R.id.Fragment_donate_request_img_location)
    ImageView FragmentDonateRequestImgLocation;
    @BindView(R.id.Fragment_donate_request_sp_gov)
    Spinner FragmentDonateRequestSpGov;
    @BindView(R.id.Fragment_donate_request_sp_city)
    Spinner FragmentDonateRequestSpCity;
    @BindView(R.id.Fragment_donate_request_ed_phone)
    TextInputLayout FragmentDonateRequestEdPhone;
    @BindView(R.id.Fragment_donate_request_ed_note)
    TextInputLayout FragmentDonateRequestEdNote;
    @BindView(R.id.Fragment_donate_request_btn_send)
    Button FragmentDonateRequestBtnSend;
    String patient_name, patient_age, patient_bloodtype, patient_phone, hospital_name, hospital_address, notes, bugs_number;
    API ApiServices;
    @BindView(R.id.Fragment_donate_request_sp_bloodtype)
    Spinner FragmentDonateRequestSpBloodtype;
    @BindView(R.id.Fragment_donate_request_ed_bloodbugs)
    TextInputLayout FragmentDonateRequestEdBloodbugs;
    @BindView(R.id.Fragment_donate_request_sp_rlt_bloodtype)
    RelativeLayout FragmentDonateRequestSpRltBloodtype;
    @BindView(R.id.Fragment_donate_request_sp_relative_gov)
    RelativeLayout FragmentDonateRequestSpRelativeGov;
    @BindView(R.id.Fragment_donate_request_sp_relative_city)
    RelativeLayout FragmentDonateRequestSpRelativeCity;
    @BindView(R.id.Fragment_donate_request_img_menu)
    ImageView FragmentDonateRequestImgMenu;
    @BindView(R.id.Fragment_donate_request_img_notifications)
    ImageView FragmentDonateRequestImgNotifications;
    @BindView(R.id.Fragment_donate_request_Tool_bar_title)
    TextView FragmentDonateRequestToolBarTitle;
    @BindView(R.id.Fragment_donate_request_img_back)
    ImageView FragmentDonateRequestImgBack;
    @BindView(R.id.relative_write)
    RelativeLayout relativeWrite;
    private List<Integer> city_ids;
    private List<Integer> blood_ids;
    private Integer city_id, blood_id;
    private Activity activity;
    private String latitude, longitude;

    Unbinder unbinder;

    public DonateRequsetFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_donate_request, container, false);
        unbinder = ButterKnife.bind(this, view);


        FragmentDonateRequestImgMenu.setVisibility(View.INVISIBLE);
        FragmentDonateRequestImgNotifications.setVisibility(View.VISIBLE);
        FragmentDonateRequestImgBack.setVisibility(View.VISIBLE);
        FragmentDonateRequestToolBarTitle.setText("طلب التبرع ");


        latitude = SharedPreferencesManger.LoadData(getActivity(), LATITUDE);
        longitude = SharedPreferencesManger.LoadData(getActivity(), LONGITUDE);

        if (latitude == null && longitude == null) {

        } else {
            Toast.makeText(getActivity(), latitude + longitude, Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getActivity(), latitude + longitude, Toast.LENGTH_SHORT).show();
        ApiServices = RetrofitClient.getClient().create(API.class);
        getbloodtype();
        getGovermantes();


        return view;
    }

    private void CheckVariable() {


        patient_name = FragmentDonateRequestEdName.getEditText().getText().toString();
        patient_age = FragmentDonateRequestEdAge.getEditText().getText().toString();
        patient_phone = FragmentDonateRequestEdPhone.getEditText().getText().toString();
        hospital_name = FragmentDonateRequestEdHospital.getEditText().getText().toString();
        bugs_number = FragmentDonateRequestEdBloodbugs.getEditText().getText().toString();
        hospital_address = FragmentDonateRequestEdHospitalAddress.getEditText().getText().toString();
        notes = FragmentDonateRequestEdNote.getEditText().getText().toString();


        if (TextUtils.isEmpty(patient_name)) {
            Toast.makeText(getActivity(), "Name is Requird", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(patient_age)) {
            Toast.makeText(getActivity(), "Age is Requird", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(patient_phone)) {
            Toast.makeText(getActivity(), "Phone is Requird", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(hospital_name)) {
            Toast.makeText(getActivity(), "Hospital Name is Requird", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(bugs_number)) {
            Toast.makeText(getActivity(), "BugsNumber is Requird", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(notes)) {
            Toast.makeText(getActivity(), "Notes is Requird", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(hospital_address)) {
            Toast.makeText(getActivity(), "Hospital Address is Requird", Toast.LENGTH_SHORT).show();
        } else {

            getDonationSend();
        }


    }

    private void getDonationSend() {

        city_id = city_ids.get(FragmentDonateRequestSpCity.getSelectedItemPosition());
        if (city_id == 0) {

            return;
        }

        blood_id = blood_ids.get(FragmentDonateRequestSpBloodtype.getSelectedItemPosition());
        if (blood_id == 0) {

            return;
        }
        String api_token = SharedPreferencesManger.LoadData(getActivity(), USER_API_TOKEN);
        ApiServices.adddonaterequestcreate(api_token, patient_name, patient_age, blood_id, Integer.valueOf(bugs_number),
                hospital_name, hospital_address, city_id, (patient_phone), notes, Double.valueOf(latitude), Double.valueOf(longitude)
        ).enqueue(new Callback<DonationRequestCreate>() {
            @Override
            public void onResponse(Call<DonationRequestCreate> call, Response<DonationRequestCreate> response) {

                try {
                    Log.i(TAG, "onResponse: ");
                    if (response.body().getStatus() == 1) {

                        Toast.makeText(getActivity(), "Requset Done Upload .", Toast.LENGTH_SHORT).show();


                        FragmentDonateRequestEdName.getEditText().setText("");
                        FragmentDonateRequestEdAge.getEditText().setText("");
                        FragmentDonateRequestEdHospital.getEditText().setText("");
                        FragmentDonateRequestEdHospitalAddress.getEditText().setText("");
                        FragmentDonateRequestEdBloodbugs.getEditText().setText("");
                        FragmentDonateRequestEdNote.getEditText().setText("");
                        FragmentDonateRequestEdPhone.getEditText().setText("");
                        FragmentDonateRequestSpBloodtype.setSelection(0);
                        FragmentDonateRequestSpGov.setSelection(0);
                        FragmentDonateRequestSpCity.setSelection(0);

                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DonationRequestCreate> call, Throwable t) {

                Log.i(TAG, "onResponse: ");

            }
        });

    }


    private void getbloodtype() {

        ApiServices.getbloodtype().enqueue(new Callback<Bloodtypes>() {
            @Override
            public void onResponse(Call<Bloodtypes> call, Response<Bloodtypes> response) {

                if (response.body().getStatus() == 1) {

                    try {

                        List<String> blood_type = new ArrayList<>();
                        blood_ids = new ArrayList<>();
                        blood_type.add(getResources().getString(R.string.bloodtype));
                        blood_ids.add(0);

                        for (int i = 0; i < response.body().getData().size(); i++) {

                            blood_type.add(response.body().getData().get(i).getName());
                            blood_ids.add(response.body().getData().get(i).getId());


                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item
                                , blood_type);

                        FragmentDonateRequestSpBloodtype.setAdapter(adapter);

                        blood_id = blood_ids.get(FragmentDonateRequestSpBloodtype.getSelectedItemPosition());


                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Bloodtypes> call, Throwable t) {

            }
        });
    }

    private void getGovermantes() {

        ApiServices.getgovernorates().enqueue(new Callback<Governorates>() {
            @Override
            public void onResponse(Call<Governorates> call, Response<Governorates> response) {
                try {
                    if (response.body().getStatus() == 1) {

                        List<String> names = new ArrayList<>();
                        final List<Integer> ids = new ArrayList<>();

                        names.add(getResources().getString(R.string.goverminate));
                        ids.add(0);

                        for (int i = 0; i < response.body().getData().size(); i++) {

                            names.add(response.body().getData().get(i).getName());
                            ids.add(response.body().getData().get(i).getId());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_item, names);

                        FragmentDonateRequestSpGov.setAdapter(adapter);

                        FragmentDonateRequestSpGov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position == 0) {

                                } else {
                                    getCities(ids.get(position));
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }

                } catch (Exception e) {

                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Governorates> call, Throwable t) {

            }
        });
    }

    private void getCities(Integer id) {
        ApiServices.getCity(id).enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
                try {
                    if (response.body().getStatus() == 1) {

                        List<String> names = new ArrayList<>();
                        city_ids = new ArrayList<>();

                        names.add(getResources().getString(R.string.city));
                        city_ids.add(0);

                        for (int i = 0; i < response.body().getData().size(); i++) {

                            names.add(response.body().getData().get(i).getName());
                            city_ids.add(response.body().getData().get(i).getId());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_item, names);

                        FragmentDonateRequestSpCity.setAdapter(adapter);

                        city_id = city_ids.get(FragmentDonateRequestSpCity.getSelectedItemPosition());


                    }

                } catch (Exception e) {

                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Cities> call, Throwable t) {

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Fragment_donate_request_img_location, R.id.Fragment_donate_request_btn_send, R.id.Fragment_donate_request_img_notifications, R.id.Fragment_donate_request_img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {


            case R.id.Fragment_donate_request_img_location:

                Intent intent = new Intent(getActivity(), MapsActivity.class);
                getActivity().startActivity(intent);

                break;

            case R.id.Fragment_donate_request_btn_send:

                CheckVariable();


                break;

            case R.id.Fragment_donate_request_img_notifications:
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                HelperMethods.replace(notificationsFragment, getActivity().getSupportFragmentManager(), R.id.frame_home_cycle, null, null);
                break;
            case R.id.Fragment_donate_request_img_back:

                homeFragment homeFragment = new homeFragment();
                HelperMethods.replace(homeFragment, getActivity().getSupportFragmentManager(), R.id.frame_home_cycle, null, null);
                break;
        }
    }


    @OnClick(R.id.relative_write)
    public void onViewClicked() {

        HelperMethods.disappearKeypad(getActivity(),getView());
    }
}



