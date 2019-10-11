package com.example.mustafa.bloodbank.ui.fragment.usercycle;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.models.gerneral.GeneralResponse;
import com.example.mustafa.bloodbank.data.models.userData.Client;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.activity.HomeActivity;
import com.example.mustafa.bloodbank.ui.fragment.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.USER_API_TOKEN;


public class RegisterFragment extends BaseFragment {

    @BindView(R.id.Fragment_register_ed_name)
    EditText FragmentRegisterEdName;
    @BindView(R.id.Fragment_register_ed_email)
    EditText FragmentRegisterEdEmail;
    @BindView(R.id.Fragment_register_tv_birthdate)
    TextView FragmentRegisterTvBirthdate;
    @BindView(R.id.Fragment_register_sp_bloodtype)
    Spinner FragmentRegisterSpBloodtype;
    @BindView(R.id.Fragment_register_tv_last_donate)
    TextView FragmentRegisterTvLastDonate;
    @BindView(R.id.Fragment_register_sp_city)
    Spinner FragmentRegisterSpCity;
    @BindView(R.id.Fragment_register_sp_gov)
    Spinner FragmentRegisterSpGov;
    @BindView(R.id.Fragment_register_rlt_sp_city)
    RelativeLayout FragmentRegisterRltSpCity;
    @BindView(R.id.Fragment_register_ed_phone)
    EditText FragmentRegisterEdPhone;
    @BindView(R.id.Fragment_register_ed_password)
    EditText FragmentRegisterEdPassword;
    @BindView(R.id.Fragment_register_ed_confirm_password)
    EditText FragmentRegisterEdConfirmPassword;
    @BindView(R.id.Fragment_register_btn_register)
    Button FragmentRegisterBtnRegister;
    @BindView(R.id.relative_write)
    RelativeLayout relativeWrite;
    private Unbinder unbinder;
    private String name, email, phone, password, confirm_password;
    private String donation_last_date, birth_date;
    private DatePickerDialog.OnDateSetListener get_date, get_date_last_donate;
    private Calendar calendar;
    private API APIServices;
    private Calendar mCalendar;
    private List<Integer> city_ids;
    private List<Integer> blood_ids;
    private int city_id, blood_id;
    private String Gov_name, City_name, Blood_type_name;

    public RegisterFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        SetUpAvtivity();
        unbinder = ButterKnife.bind(this, view);
        APIServices = RetrofitClient.getClient().create(API.class);
        getClander();
        getGovermantes();
        getbloodtype();
        return view;
    }

    private void getClander() {


        mCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener last_donate_date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                getLastDonationDate();
            }

        };

        FragmentRegisterTvLastDonate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), last_donate_date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        final DatePickerDialog.OnDateSetListener birth_date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                mCalendar.set(Calendar.YEAR, year);
                mCalendar.set(Calendar.MONTH, monthOfYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                getBirthDate();
            }

        };


        FragmentRegisterTvBirthdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), birth_date, mCalendar
                        .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    private void getLastDonationDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        FragmentRegisterTvLastDonate.setText(sdf.format(mCalendar.getTime()));
    }


    private void getBirthDate() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        FragmentRegisterTvBirthdate.setText(sdf.format(mCalendar.getTime()));
    }

    private void getbloodtype() {

        APIServices.getbloodtype().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {

                if (response.body().getStatus() == 1) {

                    try {

                        final List<String> blood_type = new ArrayList<>();
                        blood_ids = new ArrayList<>();
                        blood_type.add("فصيلة الدم ");
                        blood_ids.add(0);

                        for (int i = 0; i < response.body().getData().size(); i++) {

                            blood_type.add(response.body().getData().get(i).getName());
                            blood_ids.add(response.body().getData().get(i).getId());


                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item
                                , blood_type);

                        FragmentRegisterSpBloodtype.setAdapter(adapter);

                        FragmentRegisterSpBloodtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position == 0) {

                                } else {

                                    Blood_type_name = blood_type.get(position);
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
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    private void getGovermantes() {

        APIServices.getgovernorates().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    if (response.body().getStatus() == 1) {

                        final List<String> names = new ArrayList<>();
                        final List<Integer> ids = new ArrayList<>();

                        names.add("المحافظة");
                        ids.add(0);

                        for (int i = 0; i < response.body().getData().size(); i++) {

                            names.add(response.body().getData().get(i).getName());
                            ids.add(response.body().getData().get(i).getId());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_item, names);

                        FragmentRegisterSpGov.setAdapter(adapter);

                        FragmentRegisterSpGov.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position == 0) {

                                } else {

                                    Gov_name = names.get(position);
                                    getCities(ids.get(position));
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
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    private void getCities(Integer id) {
        APIServices.getCity(id).enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    if (response.body().getStatus() == 1) {

                        final List<String> names = new ArrayList<>();
                        city_ids = new ArrayList<>();
                        names.add("المدينة");
                        city_ids.add(0);

                        for (int i = 0; i < response.body().getData().size(); i++) {

                            names.add(response.body().getData().get(i).getName());
                            city_ids.add(response.body().getData().get(i).getId());
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                                android.R.layout.simple_spinner_item, names);

                        FragmentRegisterSpCity.setAdapter(adapter);


                        FragmentRegisterSpCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                if (position == 0) {

                                } else {
                                    City_name = names.get(position);
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
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick({R.id.Fragment_register_tv_birthdate, R.id.Fragment_register_tv_last_donate, R.id.Fragment_register_btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {

            case R.id.Fragment_register_btn_register:
                onRegister();
                break;
        }
    }

    public void onRegister() {

        name = FragmentRegisterEdName.getText().toString();
        email = FragmentRegisterEdEmail.getText().toString();
        phone = FragmentRegisterEdPhone.getText().toString();
        password = FragmentRegisterEdPassword.getText().toString();
        confirm_password = FragmentRegisterEdConfirmPassword.getText().toString();
        donation_last_date = FragmentRegisterTvLastDonate.getText().toString();
        birth_date = FragmentRegisterTvBirthdate.getText().toString();


        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getActivity(), "Name is Requird..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Email is Requird..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(getActivity(), "Phone is Requird..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Password is Requird..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(confirm_password)) {
            Toast.makeText(getActivity(), "Password is Requird..", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!TextUtils.equals(password, confirm_password)) {
            Toast.makeText(getActivity(), "Password didn't match..", Toast.LENGTH_SHORT).show();
            return;
        }

        SetUserRegister();


    }

    private void SetUserRegister() {
        city_id = city_ids.get(FragmentRegisterSpCity.getSelectedItemPosition());
        if (city_id == 0) {

            return;
        }

        blood_id = blood_ids.get(FragmentRegisterSpBloodtype.getSelectedItemPosition());
        if (blood_id == 0) {

            return;
        }

        APIServices.addregister(name, email, birth_date, city_id,
                phone, donation_last_date, password, confirm_password, blood_id)
                .enqueue(new Callback<Client>() {
                             @Override
                             public void onResponse(Call<Client> call, Response<Client> response) {
                                 Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                                 try {
                                 if (response.body().getStatus() == 1) {
                                         SharedPreferencesManger.SaveData(getActivity(), USER_API_TOKEN, response.body().getData()
                                                 .getApiToken());
                                         SharedPreferencesManger.SaveData(getActivity(), "PASSWORD", password);
                                         HelperMethods.showProgressDialog(getActivity(), "انتظر قليلا");
                                         Intent intent = new Intent(getActivity(), HomeActivity.class);
                                         startActivity(intent);
                                         getActivity().finish();

                                     }
                                     }catch (Exception e) {
                                         Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                 }
                             }

                             @Override
                             public void onFailure(Call<Client> call, Throwable t) {

                             }
                         }
                );
    }

    @OnClick(R.id.relative_write)
    public void onViewClicked() {
        HelperMethods.disappearKeypad(getActivity(),getView());
    }

    @Override
    public void onBack() {
        LoginFragment Fragment = new LoginFragment();
        HelperMethods.replace(Fragment, getActivity().getSupportFragmentManager(), R.id.frame_user_cycle, null, null);    }
}
