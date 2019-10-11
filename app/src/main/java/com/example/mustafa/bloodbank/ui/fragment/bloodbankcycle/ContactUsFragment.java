package com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle;


import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.models.contactUs.ContactUs;
import com.example.mustafa.bloodbank.data.models.settings.Settings;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.fragment.BaseFragment;

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
public class ContactUsFragment extends BaseFragment {


    @BindView(R.id.Fragment_contactus_tv_phone_api)
    TextView FragmentContactusTvPhoneApi;
    @BindView(R.id.Fragment_contactus_tv_email_api)
    TextView FragmentContactusTvEmailApi;
    @BindView(R.id.Fragment_contactus_img_facebook)
    ImageView FragmentContactusImgFacebook;
    @BindView(R.id.Fragment_contactus_img_twitter)
    ImageView FragmentContactusImgTwitter;
    @BindView(R.id.Fragment_contactus_img_youtube)
    ImageView FragmentContactusImgYoutube;
    @BindView(R.id.Fragment_contactus_img_instagram)
    ImageView FragmentContactusImgInstagram;
    @BindView(R.id.Fragment_contactus_img_whatsapp)
    ImageView FragmentContactusImgWhatsapp;
    @BindView(R.id.Fragment_contactus_img_google)
    ImageView FragmentContactusImgGoogle;
    @BindView(R.id.Fragment_contactus_linear_social)
    LinearLayout FragmentContactusLinearSocial;
    @BindView(R.id.Fragment_contactus_btn_contact)
    Button FragmentContactusBtnContact;
    @BindView(R.id.Fragment_contactus_ed_msg_title)
    TextInputLayout FragmentContactusEdMsgTitle;
    @BindView(R.id.Fragment_contactus_ed_msg_content)
    TextInputLayout FragmentContactusEdMsgContent;
    @BindView(R.id.Fragment_contactus_btn_send)
    Button FragmentContactusBtnSend;
    Unbinder unbinder;
    public API ApiServices;
    @BindView(R.id.relative_write)
    RelativeLayout relativeWrite;

    public ContactUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SetUpAvtivity();
        View view = inflater.inflate(R.layout.fragment_contactus, container, false);
        unbinder = ButterKnife.bind(this, view);
        ApiServices = RetrofitClient.getClient().create(API.class);
        getContactInfo();
        return view;
    }

    private void getContactInfo() {

        ApiServices.getSettings(SharedPreferencesManger.LoadData(getActivity(), USER_API_TOKEN)).enqueue(new Callback<Settings>() {
            @Override
            public void onResponse(Call<Settings> call, Response<Settings> response) {

                if (response.body().getStatus() == 1) {

                    String phone = response.body().getData().getPhone();
                    String email = response.body().getData().getEmail();
                    FragmentContactusTvPhoneApi.setText(phone);
                    FragmentContactusTvEmailApi.setText(email);
                }
            }
            @Override
            public void onFailure(Call<Settings> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.Fragment_contactus_img_facebook, R.id.Fragment_contactus_img_twitter, R.id.Fragment_contactus_img_youtube, R.id.Fragment_contactus_img_instagram, R.id.Fragment_contactus_img_whatsapp, R.id.Fragment_contactus_img_google, R.id.Fragment_contactus_linear_social, R.id.Fragment_contactus_btn_contact, R.id.Fragment_contactus_btn_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Fragment_contactus_img_facebook:
                break;
            case R.id.Fragment_contactus_img_twitter:
                break;
            case R.id.Fragment_contactus_img_youtube:
                break;
            case R.id.Fragment_contactus_img_instagram:
                break;
            case R.id.Fragment_contactus_img_whatsapp:
                break;
            case R.id.Fragment_contactus_img_google:
                break;
            case R.id.Fragment_contactus_linear_social:
                break;
            case R.id.Fragment_contactus_btn_contact:
                break;
            case R.id.Fragment_contactus_btn_send:

                SendMessage();
                break;
        }


    }

    private void SendMessage() {

        String messageTitle = FragmentContactusEdMsgTitle.getEditText().getText().toString();
        String messageContent = FragmentContactusEdMsgContent.getEditText().getText().toString();

        if (TextUtils.isEmpty(messageTitle)) {
            Toast.makeText(getActivity(), "Requird", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(messageContent)) {
            Toast.makeText(getActivity(), "Content Requird!!", Toast.LENGTH_SHORT).show();
        } else {
            ApiServices.addContact(SharedPreferencesManger.LoadData(getActivity(), USER_API_TOKEN), messageTitle, messageContent).enqueue(new Callback<ContactUs>() {
                @Override
                public void onResponse(Call<ContactUs> call, Response<ContactUs> response) {

                    if (response.body().getStatus() == 1) {

                        Toast.makeText(getActivity(), "Message Send Done!", Toast.LENGTH_SHORT).show();
                        FragmentContactusEdMsgContent.getEditText().setText("");
                        FragmentContactusEdMsgTitle.getEditText().setText("");

                    }
                }

                @Override
                public void onFailure(Call<ContactUs> call, Throwable t) {

                }
            });
        }
    }


    @OnClick(R.id.relative_write)
    public void onViewClicked() {
        HelperMethods.disappearKeypad(getActivity(),getView());
    }
}
