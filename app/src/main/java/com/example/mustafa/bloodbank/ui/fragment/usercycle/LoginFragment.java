package com.example.mustafa.bloodbank.ui.fragment.usercycle;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.models.userData.Client;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.activity.HomeActivity;
import com.example.mustafa.bloodbank.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.PASSWORD;
import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.REMEBER;
import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.USER_API_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends BaseFragment {


//    @BindView(R.id.Login_image)
//    ImageView LoginImage;
    @BindView(R.id.Fragment_Login_ed_phone)
    TextInputLayout LoginEdPhone;
    @BindView(R.id.Fragment_Login_ed_password)
    TextInputLayout LoginEdPassword;
    @BindView(R.id.Fragment_Login_check_remember)
    CheckBox LoginCheckRemember;
    @BindView(R.id.Fragment_Login_tv_forget_password)
    TextView LoginTvForgetPassword;
    @BindView(R.id.linear)
    LinearLayout linear;
    @BindView(R.id.Fragment_Login_btn_login)
    Button LoginBtnLogin;
    @BindView(R.id.Fragment_Login_btn_rigster)
    Button LoginBtnRigster;
    Unbinder unbinder;
    @BindView(R.id.relative_write)
    RelativeLayout relativeWrite;
    private String Login_Phone_Number, Login_Password_Number;
    private API API;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SetUpAvtivity();
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        unbinder = ButterKnife.bind(this, view);
        API = RetrofitClient.getClient().create(API.class);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.Fragment_Login_tv_forget_password)
    public void onLoginTvForgetPasswordClicked() {
        ForgetPassword1Fragment fragment = new ForgetPassword1Fragment();
        HelperMethods.replace(fragment, getActivity().getSupportFragmentManager(), R.id.frame_user_cycle, null, null);
    }

    @OnClick(R.id.Fragment_Login_btn_login)
    public void onLoginBtnLoginClicked() {
        Login_Phone_Number = LoginEdPhone.getEditText().getText().toString();
        Login_Password_Number = LoginEdPassword.getEditText().getText().toString();
        if (TextUtils.isEmpty(Login_Phone_Number)) {
            Toast.makeText(getActivity(), "Phone is Required..", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Login_Password_Number)) {
            Toast.makeText(getActivity(), "Password is Required..", Toast.LENGTH_SHORT).show();
        } else {
            SignIn(Login_Phone_Number, Login_Password_Number);
        }
    }

    private void SignIn(String login_phone_number, final String login_password_number) {

        Call<Client> call = API.addlogin(login_phone_number, login_password_number);
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                Toast.makeText(getActivity(), response.message(), Toast.LENGTH_SHORT).show();
                try {
                    if (response.body().getStatus() == 1) {
                        SharedPreferencesManger.SaveData(getActivity(), USER_API_TOKEN, response.body().getData().getApiToken());
                        SharedPreferencesManger.SaveData(getActivity(), PASSWORD,login_password_number);
                        SharedPreferencesManger.SaveBoolean(getActivity(), REMEBER, LoginCheckRemember.isChecked());
                        Toast.makeText(getActivity(), "Done SignIN", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), HomeActivity.class);
                        getActivity().startActivity(intent);
                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {

            }
        });
    }


    @OnClick(R.id.Fragment_Login_btn_rigster)
    public void onLoginBtnRigsterClicked() {

        RegisterFragment registerFragment = new RegisterFragment();
        HelperMethods.replace(registerFragment, getActivity().getSupportFragmentManager(), R.id.frame_user_cycle, null, null);
    }

    @OnClick(R.id.relative_write)
    public void onViewClicked() {

        HelperMethods.disappearKeypad(getActivity(),getView());
    }

    @Override
    public void onBack() {
        getActivity().finish();
    }
}
