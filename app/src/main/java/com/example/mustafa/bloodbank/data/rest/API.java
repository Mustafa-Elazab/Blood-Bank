package com.example.mustafa.bloodbank.data.rest;

import com.example.mustafa.bloodbank.data.model.bloodtypes.Bloodtypes;
import com.example.mustafa.bloodbank.data.model.categories.Categories;
import com.example.mustafa.bloodbank.data.model.cities.Cities;
import com.example.mustafa.bloodbank.data.model.contact.Contact;
import com.example.mustafa.bloodbank.data.model.donation_request_create.DonationRequest;
import com.example.mustafa.bloodbank.data.model.donation_request_create.DonationRequestCreate;
import com.example.mustafa.bloodbank.data.model.donationrequestnotifi.DonationRequestNotifi;
import com.example.mustafa.bloodbank.data.model.donationrequests.DonationRequests;
import com.example.mustafa.bloodbank.data.model.governorates.Governorates;
import com.example.mustafa.bloodbank.data.model.login.Login;
import com.example.mustafa.bloodbank.data.model.myfavourites.MyFavourites;
import com.example.mustafa.bloodbank.data.model.notifications.Notifications;
import com.example.mustafa.bloodbank.data.model.notificationscount.NotificationsCount;
import com.example.mustafa.bloodbank.data.model.notificationssettings.NotificationsSettings;
import com.example.mustafa.bloodbank.data.model.posts.Posts;
import com.example.mustafa.bloodbank.data.model.posttogglefavourite.PostToggleFavourite;
import com.example.mustafa.bloodbank.data.model.profile.Profile;
import com.example.mustafa.bloodbank.data.model.profileedit.ProfileEdit;
import com.example.mustafa.bloodbank.data.model.register.Register;
import com.example.mustafa.bloodbank.data.model.registertoken.RegisterToken;
import com.example.mustafa.bloodbank.data.model.removetoken.RemoveToken;
import com.example.mustafa.bloodbank.data.model.resetpassword.Resetpassword;
import com.example.mustafa.bloodbank.data.model.settings.Settings;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {


    @FormUrlEncoded
    @POST("login")
    Call<Login> addlogin(
            @Field("phone") String phone,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register")
    Call<Register> addregister(

            @Field("name") String name,
            @Field("email") String email,
            @Field("birth_date") String birth_date,
            @Field("city_id") int city_id,
            @Field("phone") String phone,
            @Field("donation_last_date") String donation_last_date,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Field("blood_type_id") int blood_type_id


    );

    @GET("governorates")
    Call<Governorates> getgovernorates();


    @GET("cities")
    Call<Cities> getCity(

            @Query("governorate_id") int governorate_id
    );

    @FormUrlEncoded
    @POST("reset-password")
    Call<Resetpassword> addresetpassword(
            @Field("phone") String phone
    );

    @FormUrlEncoded
    @POST("new-password")
    Call<Resetpassword> addnewpassword(
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Field("pin_code") int pin_code,
            @Field("phone") String phone
    );

    @GET("blood-types")
    Call<Bloodtypes> getbloodtype();

    @GET("posts")
    Call<Posts> getposts(

            @Query("api_token") String api_token,
            @Query("page") int page
    );


    @GET("donation-requests")
    Call<DonationRequests> getdonationrequest(

            @Query("api_token") String api_token,
            @Query("page") int page
    );

    @FormUrlEncoded
    @POST("post-toggle-favourite")
    Call<PostToggleFavourite> addposttogglefavourite(
            @Field("post_id") int post_id,
            @Field("api_token") String api_token

    );

    @GET("my-favourites")
    Call<MyFavourites> getallfavourite(
            @Query("api_token") String api_token
    );


    @FormUrlEncoded
    @POST("donation-request/create")
    Call<DonationRequestCreate> adddonaterequestcreate(@Field("api_token") String api_token,
                                                       @Field("patient_name") String patient_name,
                                                       @Field("patient_age") String patient_age,
                                                       @Field("blood_type_id") int blood_type_id,
                                                       @Field("bags_num") int bags_num,
                                                       @Field("hospital_name") String hospital_name,
                                                       @Field("hospital_address") String hospital_address,
                                                       @Field("city_id") int city_id,
                                                       @Field("phone") String phone,
                                                       @Field("notes") String notes,
                                                       @Field("latitude") double latitude,
                                                       @Field("longitude") double longitude


    );

    @FormUrlEncoded
    @POST("profile")
    Call<Profile> getprofile(
            @Field("api_token") String api_token
    );

    @GET("posts")
    Call<Posts> getpostfilter(

            @Query("api_token") String api_token,
            @Query("page") int page,
            @Query("keyword") String keyword,
            @Query("category_id") int category_id

    );


    @GET("categories")
    Call<Categories> getcategories();


    @GET("donation-requests")
    Call<DonationRequests> getorderfilter(

            @Query("api_token") String api_token,
            @Query("blood_type_id") int blood_type_id,
            @Query("city_id") int city_id,
            @Query("page") int page
    );

    @FormUrlEncoded
    @POST("contact")
    Call<Contact> addContact(
            @Field("api_token") String api_token,
            @Field("title") String title,
            @Field("message") String message
    );

    @GET("settings")
    Call<Settings> getSettings(
            @Query("api_token") String api_token
    );

    @FormUrlEncoded
    @POST("notifications-settings")
    Call<NotificationsSettings> getnotifications_settings(
            @Field("api_token") String api_token
    );
    @POST("notifications-settings")
    @FormUrlEncoded
    Call<NotificationsSettings> ChangeNotificationsSettings(@Field("api_token") String api_token,
                                                            @Field("governorates[]") List<Integer> governorates,
                                                            @Field("blood_types[]") List<Integer> blood_types);


    @FormUrlEncoded
    @POST("profile")
    Call<ProfileEdit> profileEdit(

            @Field("name") String name,
            @Field("email") String email,
            @Field("birth_date") String birth_date,
            @Field("city_id") int city_id,
            @Field("phone") String phone,
            @Field("donation_last_date") String donation_last_date,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Field("blood_type_id") int blood_type_id,
            @Field("api_token") String api_token


    );

    @GET("notifications-count")
    Call<NotificationsCount> getnotificationscount(
            @Query("api_token") String api_token
    );

    @GET("notifications")
    Call<Notifications> getnotifications(
            @Query("api_token") String api_token
    );


    @FormUrlEncoded
    @POST("register-token")
    Call<RegisterToken> getRegisterToken(
            @Field("token") String token,
            @Field("api_token") String api_token,
            @Field("type") String type
    );

    @FormUrlEncoded
    @POST("remove-token")
    Call<RemoveToken> RemoveToken(
            @Field("token") String token,
            @Field("api_token") String api_token
    );

    @GET("donation-request")
    Call<DonationRequestNotifi> getDontationRequestNotifi(

            @Query("api_token") String api_token,
            @Query("donation_id") int donation_id
    );

}
