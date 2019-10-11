package com.example.mustafa.bloodbank.data.rest;
import com.example.mustafa.bloodbank.data.models.artical.Artical;
import com.example.mustafa.bloodbank.data.models.contactUs.ContactUs;
import com.example.mustafa.bloodbank.data.models.donate.AddNewDonate.AddNewDonate;
import com.example.mustafa.bloodbank.data.models.donate.Donate;
import com.example.mustafa.bloodbank.data.models.donationrequestnotifi.DonationRequestNotifi;
import com.example.mustafa.bloodbank.data.models.gerneral.GeneralResponse;
import com.example.mustafa.bloodbank.data.models.notifications.Notifications;
import com.example.mustafa.bloodbank.data.models.notificationscount.NotificationsCount;
import com.example.mustafa.bloodbank.data.models.notificationssettings.NotificationsSettings;
import com.example.mustafa.bloodbank.data.models.posttogglefavourite.PostToggleFavourite;
import com.example.mustafa.bloodbank.data.models.publicresponse.PublicResponse;
import com.example.mustafa.bloodbank.data.models.resetpassword.Resetpassword;
import com.example.mustafa.bloodbank.data.models.settings.Settings;
import com.example.mustafa.bloodbank.data.models.userData.Client;

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
    Call<Client> addlogin(@Field("phone") String phone,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("register")
    Call<Client> addregister(@Field("name") String name,
                             @Field("email") String email,
                             @Field("birth_date") String birth_date,
                             @Field("city_id") int city_id,
                             @Field("phone") String phone,
                             @Field("donation_last_date") String donation_last_date,
                             @Field("password") String password,
                             @Field("password_confirmation") String password_confirmation,
                             @Field("blood_type_id") int blood_type_id);

    @GET("governorates")
    Call<GeneralResponse> getgovernorates();


    @GET("cities")
    Call<GeneralResponse> getCity(@Query("governorate_id") int governorate_id);

    @FormUrlEncoded
    @POST("reset-password")
    Call<Resetpassword> addresetpassword(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("new-password")
    Call<Resetpassword> addnewpassword(@Field("password") String password,
                                       @Field("password_confirmation") String password_confirmation,
                                       @Field("pin_code") int pin_code,
                                       @Field("phone") String phone);

    @GET("blood-types")
    Call<GeneralResponse> getbloodtype();

    @GET("posts")
    Call<Artical> getposts(@Query("api_token") String api_token,
                           @Query("page") int page);


    @GET("donation-requests")
    Call<Donate> getdonationrequest(@Query("api_token") String api_token,
                                    @Query("page") int page);

    @FormUrlEncoded
    @POST("post-toggle-favourite")
    Call<PostToggleFavourite> addposttogglefavourite(@Field("post_id") int post_id,
                                                     @Field("api_token") String api_token);

    @GET("my-favourites")
    Call<Artical> getallfavourite(@Query("api_token") String api_token);


    @FormUrlEncoded
    @POST("donation-request/create")
    Call<AddNewDonate> adddonaterequestcreate(@Field("api_token") String api_token,
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
                                              @Field("longitude") double longitude);

    @FormUrlEncoded
    @POST("profile")
    Call<Client> getprofile(@Field("api_token") String api_token);

    @GET("posts")
    Call<Artical> getpostfilter(@Query("api_token") String api_token,
                              @Query("page") int page,
                              @Query("keyword") String keyword,
                              @Query("category_id") int category_id);


    @GET("categories")
    Call<GeneralResponse> getcategories();


    @GET("donation-requests")
    Call<Donate> getorderfilter(@Query("api_token") String api_token,
                                          @Query("blood_type_id") int blood_type_id,
                                          @Query("city_id") int city_id,
                                          @Query("page") int page);

    @FormUrlEncoded
    @POST("contact")
    Call<ContactUs> addContact(@Field("api_token") String api_token,
                               @Field("title") String title,
                               @Field("message") String message);

    @GET("settings")
    Call<Settings> getSettings(@Query("api_token") String api_token);


    @FormUrlEncoded
    @POST("notifications-settings")
    Call<NotificationsSettings> getnotifications_settings(@Field("api_token") String api_token);


    @POST("notifications-settings")
    @FormUrlEncoded
    Call<NotificationsSettings> ChangeNotificationsSettings(@Field("api_token") String api_token,
                                                            @Field("governorates[]") List<Integer> governorates,
                                                            @Field("blood_types[]") List<Integer> blood_types);

    @FormUrlEncoded
    @POST("profile")
    Call<Client> profileEdit(@Field("name") String name,
            @Field("email") String email,
            @Field("birth_date") String birth_date,
            @Field("city_id") int city_id,
            @Field("phone") String phone,
            @Field("donation_last_date") String donation_last_date,
            @Field("password") String password,
            @Field("password_confirmation") String password_confirmation,
            @Field("blood_type_id") int blood_type_id,
            @Field("api_token") String api_token);


    @GET("notifications-count")
    Call<NotificationsCount> getnotificationscount(@Query("api_token") String api_token);

    @GET("notifications")
    Call<Notifications> getnotifications(@Query("api_token") String api_token);


    @FormUrlEncoded
    @POST("register-token")
    Call<PublicResponse> getRegisterToken(@Field("token") String token,
                                          @Field("api_token") String api_token,
                                          @Field("type") String type);

    @FormUrlEncoded
    @POST("remove-token")
    Call<PublicResponse> RemoveToken(@Field("token") String token,
                                  @Field("api_token") String api_token);

    @GET("donation-request")
    Call<DonationRequestNotifi> getDontationRequestNotifi(@Query("api_token") String api_token,
                                                          @Query("donation_id") int donation_id);

}
