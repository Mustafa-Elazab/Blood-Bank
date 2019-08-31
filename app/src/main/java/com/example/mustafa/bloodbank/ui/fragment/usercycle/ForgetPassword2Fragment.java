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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.data.model.resetpassword.Resetpassword;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.activity.HomeActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForgetPassword2Fragment extends Fragment {


    @BindView(R.id.Login_image)
    ImageView LoginImage;
    @BindView(R.id.Fragment_forget1_ed_phone)
    TextInputLayout FragmentForget1EdPhone;
    @BindView(R.id.Fragment_forget1_ed_password)
    TextInputLayout FragmentForget1EdPassword;
    @BindView(R.id.Fragment_forget1_ed_confirm_password)
    TextInputLayout FragmentForget1EdConfirmPassword;
    @BindView(R.id.Fragment_forget1_btn_next)
    Button FragmentForget1BtnNext;
    Unbinder unbinder;
    @BindView(R.id.relative_write)
    RelativeLayout relativeWrite;
    private API APIServices;
    private Integer pinCodeForTest;

    public ForgetPassword2Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password2, container, false);
        unbinder = ButterKnife.bind(this, view);
        APIServices = RetrofitClient.getClient().create(API.class);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void createnewpass(String phone, String password, String confirmpassword) {


        APIServices.addnewpassword(password, confirmpassword, pinCodeForTest, phone).enqueue(
                new Callback<Resetpassword>() {
                    @Override
                    public void onResponse(Call<Resetpassword> call, Response<Resetpassword> response) {

                        if (response.body().getStatus() == 1) {

                            try {
                                pinCodeForTest = response.body().getData().getPinCodeForTest();
                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<Resetpassword> call, Throwable t) {

                    }
                }
        );
    }

    @OnClick({R.id.Fragment_forget1_btn_next, R.id.relative_write})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Fragment_forget1_btn_next:
                String phone = FragmentForget1EdPhone.getEditText().getText().toString();
                String password = FragmentForget1EdPhone.getEditText().getText().toString();
                String confirmpassword = FragmentForget1EdPhone.getEditText().getText().toString();

                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getActivity(), "Phone is Requird..", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getActivity(), "Password is Requird..", Toast.LENGTH_SHORT).show();
                }
                if (TextUtils.isEmpty(confirmpassword)) {
                    Toast.makeText(getActivity(), "Password is Requird..", Toast.LENGTH_SHORT).show();
                }
                if (!TextUtils.equals(password, confirmpassword)) {
                    Toast.makeText(getActivity(), "Password didn't match..", Toast.LENGTH_SHORT).show();
                } else {

                    createnewpass(phone,password,confirmpassword);

                }

                break;
            case R.id.relative_write:
                HelperMethods.disappearKeypad(getActivity(),getView());
                break;
        }
    }
}