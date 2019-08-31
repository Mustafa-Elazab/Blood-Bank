package com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.model.bloodtypes.Bloodtypes;
import com.example.mustafa.bloodbank.data.model.cities.Cities;
import com.example.mustafa.bloodbank.data.model.governorates.Governorates;
import com.example.mustafa.bloodbank.data.model.profile.Profile;
import com.example.mustafa.bloodbank.data.model.profileedit.ProfileEdit;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.activity.HomeActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.BLOOD_TYPE;
import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.CITY_NAME;
import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.GOV_NAME;
import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.PASSWORD;
import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.USER_API_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment {


    @BindView(R.id.Fragment_editprofile_ed_name)
    TextInputLayout FragmentEditprofileEdName;
    @BindView(R.id.Fragment_editprofile_ed_email)
    TextInputLayout FragmentEditprofileEdEmail;
    @BindView(R.id.Fragment_editprofile_tv_birthdate)
    TextView FragmentEditprofileTvBirthdate;
    @BindView(R.id.Fragment_editprofile_sp_bloodtype)
    Spinner FragmentEditprofileSpBloodtype;
    @BindView(R.id.Fragment_editprofile_tv_last_donate)
    TextView FragmentEditprofileTvLastDonate;
    @BindView(R.id.Fragment_editprofile_sp_gov)
    Spinner FragmentEditprofileSpGov;
    @BindView(R.id.Fragment_editprofile_sp_city)
    Spinner FragmentEditprofileSpCity;
    @BindView(R.id.Fragment_editprofile_ed_phone)
    TextInputLayout FragmentEditprofileEdPhone;
    @BindView(R.id.Fragment_editprofile_ed_password)
    TextInputLayout FragmentEditprofileEdPassword;
    @BindView(R.id.Fragment_editprofile_ed_confirm_password)
    TextInputLayout FragmentEditprofileEdConfirmPassword;
    @BindView(R.id.Fragment_editprofile_btn_editprofile)
    Button FragmentEditprofileBtnEditprofile;
    @BindView(R.id.relative_write)
    RelativeLayout relativeWrite;
    private int city_id, blood_id, gov_id;

    private API ApiServices;
    private ArrayList<String> BloodTypes = new ArrayList<>();
    private List<Integer> blood_ids = new ArrayList<>();

    private ArrayList<String> CityNames = new ArrayList<>();
    private List<Integer> city_ids = new ArrayList<>();

    private ArrayList<String> GovNames = new ArrayList<>();
    private List<Integer> Govids = new ArrayList<>();

    private String name, email, phone, password, confirm_password;
    private String donation_last_date, birth_date;

    Unbinder unbinder;


    public EditProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_editprofile, container, false);

        unbinder = ButterKnife.bind(this, view);
        ApiServices = RetrofitClient.getClient().create(API.class);
        getUserData();

        return view;
    }

    private void getUserData() {


        String api_token = SharedPreferencesManger.LoadData(getActivity(), USER_API_TOKEN);
        ApiServices.getprofile(api_token).enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {

                if (response.body().getStatus() == 1) {

                    try {

                        FragmentEditprofileEdName.setHint(response.body().getData().getClient().getName());
                        FragmentEditprofileEdEmail.setHint(response.body().getData().getClient().getEmail());
                        FragmentEditprofileTvBirthdate.setText(response.body().getData().getClient().getBirthDate());
                        FragmentEditprofileTvLastDonate.setText(response.body().getData().getClient().getDonationLastDate());
                        FragmentEditprofileEdPhone.setHint(response.body().getData().getClient().getPhone());
                        String blood_type_name = SharedPreferencesManger.LoadData(getActivity(), BLOOD_TYPE);
                        String gov_name = SharedPreferencesManger.LoadData(getActivity(), GOV_NAME);

                        String city_name = SharedPreferencesManger.LoadData(getActivity(), CITY_NAME);
                        Toast.makeText(getActivity(), city_name, Toast.LENGTH_SHORT).show();

                        FragmentEditprofileSpBloodtype.setSelection((response.body().getData().getClient().getBloodType().getId()));
//                        FragmentEditprofileSpGov.setSelection((response.body().getData().getClient().getCity().getGovernorate().getId()));
//                        FragmentEditprofileSpCity.setSelection(response.body().getData().getClient().getCity().getId());
                        String password = SharedPreferencesManger.LoadData(getActivity(), PASSWORD);


                        getGoverment(response.body().getData().getClient().getCity().getGovernorate().getId(),
                                response.body().getData().getClient().getCity().getId());
                        getBloodType(response.body().getData().getClient().getBloodType().getId());
                        FragmentEditprofileEdPassword.setHint(password);
                        FragmentEditprofileEdConfirmPassword.setHint(password);


                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {

            }
        });
    }

    private void getBloodType(final Integer id) {

        ApiServices.getbloodtype().enqueue(new Callback<Bloodtypes>() {
            @Override
            public void onResponse(Call<Bloodtypes> call, Response<Bloodtypes> response) {

                if (response.body().getStatus() == 1) {

                    try {


                        BloodTypes.add("اختر الفصيلة");
                        blood_ids.add(0);

                        int pos = 0;

                        for (int i = 0; i < response.body().getData().size(); i++) {

                            BloodTypes.add(response.body().getData().get(i).getName());
                            blood_ids.add(response.body().getData().get(i).getId());

                            if (response.body().getData().get(i).getId().equals(id)) {
                                pos = 1 + i;
                            }

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item
                                , BloodTypes);

                        FragmentEditprofileSpBloodtype.setAdapter(adapter);

                        FragmentEditprofileSpBloodtype.setSelection(pos);
                        FragmentEditprofileSpBloodtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position == 0) {

                                } else {
                                    blood_id = blood_ids.get(position);

                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

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

    private void getGoverment(final Integer id, final Integer integer) {

        ApiServices.getgovernorates().enqueue(new Callback<Governorates>() {
            @Override
            public void onResponse(Call<Governorates> call, Response<Governorates> response) {
                try {
                    if (response.body().getStatus() == 1) {


                        GovNames.add("اختر المحافظة");
                        Govids.add(0);

                        int pos = 0;

                        for (int i = 0; i < response.body().getData().size(); i++) {

                            GovNames.add(response.body().getData().get(i).getName());
                            Govids.add(response.body().getData().get(i).getId());

                            if (response.body().getData().get(i).getId().equals(id)) {
                                pos = 1 + i;
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_item, GovNames);

                        FragmentEditprofileSpGov.setAdapter(adapter);

                        FragmentEditprofileSpGov.setSelection(pos);

                        FragmentEditprofileSpGov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position == 0) {

                                } else {

                                    getCities(Govids.get(position), integer);
                                }
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

            @Override
            public void onFailure(Call<Governorates> call, Throwable t) {

            }
        });
    }

    private void getCities(int i, final Integer integer) {
        ApiServices.getCity(i).enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
                try {
                    if (response.body().getStatus() == 1) {


                        CityNames.add("اختر المدينة");
                        city_ids.add(0);

                        int pos = 0;

                        for (int i = 0; i < response.body().getData().size(); i++) {

                            CityNames.add(response.body().getData().get(i).getName());
                            city_ids.add(response.body().getData().get(i).getId());
                            if (response.body().getData().get(i).getId().equals(integer)) {
                                pos = 1 + i;
                            }
                        }


                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_item, CityNames);

                        FragmentEditprofileSpCity.setAdapter(adapter);

                        FragmentEditprofileSpCity.setSelection(pos);
                        FragmentEditprofileSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position == 0) {

                                } else {
                                    city_id = city_ids.get(position);
                                }
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


    private void SetUserEditProfile() {

        city_id = city_ids.get(FragmentEditprofileSpCity.getSelectedItemPosition());
        if (city_id == 0) {

            return;
        }

        blood_id = blood_ids.get(FragmentEditprofileSpBloodtype.getSelectedItemPosition());
        if (blood_id == 0) {

            return;
        }

        ApiServices.profileEdit(name, email, birth_date, city_id, phone, donation_last_date, password, confirm_password
                , blood_id, SharedPreferencesManger.LoadData(getActivity(), USER_API_TOKEN)).enqueue(new Callback<ProfileEdit>() {
            @Override
            public void onResponse(Call<ProfileEdit> call, Response<ProfileEdit> response) {

                if (response.body().getStatus() == 1) {

                    HelperMethods.showProgressDialog(getActivity(), "انتظر قليلا");
                    Toast.makeText(getActivity(), "Done edit Profile", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    getActivity().startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ProfileEdit> call, Throwable t) {

            }
        });

    }


    @OnClick({R.id.Fragment_editprofile_btn_editprofile, R.id.relative_write})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Fragment_editprofile_btn_editprofile:


                name = FragmentEditprofileEdName.getEditText().getText().toString();
                email = FragmentEditprofileEdEmail.getEditText().getText().toString();
                phone = FragmentEditprofileEdPhone.getEditText().getText().toString();
                password = FragmentEditprofileEdPassword.getEditText().getText().toString();
                confirm_password = FragmentEditprofileEdConfirmPassword.getEditText().getText().toString();
                donation_last_date = FragmentEditprofileTvLastDonate.getText().toString();
                birth_date = FragmentEditprofileTvBirthdate.getText().toString();


                if (TextUtils.isEmpty(name)) {

                    name = (String) FragmentEditprofileEdName.getHint();

                    return;
                }
                if (TextUtils.isEmpty(email)) {

                    email = (String) FragmentEditprofileEdEmail.getHint();
                    return;
                }
                if (TextUtils.isEmpty(phone)) {

                    phone = (String) FragmentEditprofileEdPhone.getHint();
                    return;
                }
                if (TextUtils.isEmpty(password)) {

                    password = (String) FragmentEditprofileEdPassword.getHint();
                    return;
                }
                if (TextUtils.isEmpty(confirm_password)) {

                    confirm_password = (String) FragmentEditprofileEdConfirmPassword.getHint();
                    return;
                }
                if (!TextUtils.equals(password, confirm_password)) {

                    Toast.makeText(getActivity(), "Password didn't Match", Toast.LENGTH_SHORT).show();
                    return;
                }

                SetUserEditProfile();


                break;
            case R.id.relative_write:
                HelperMethods.disappearKeypad(getActivity(),getView());

                break;
        }
    }
}



