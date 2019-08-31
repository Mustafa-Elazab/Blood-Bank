package com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.data.model.donation_request_create.DonationRequest;
import com.example.mustafa.bloodbank.data.model.donationrequestnotifi.DonationRequestNotifi;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.home.homeFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
public class DetailFragment extends Fragment {

    public DonationRequest donationsdata;
    @BindView(R.id.Fragment_detail_tv_name)
    TextView FragmentDetailTvName;
    @BindView(R.id.Fragment_detail_tv_age)
    TextView FragmentDetailTvAge;
    @BindView(R.id.Fragment_detail_tv_bloodtype)
    TextView FragmentDetailTvBloodtype;
    @BindView(R.id.Fragment_detail_tv_bagsnumber)
    TextView FragmentDetailTvBagsnumber;
    @BindView(R.id.Fragment_detail_tv_hospital_name)
    TextView FragmentDetailTvHospitalName;
    @BindView(R.id.Fragment_detail_tv_hospital_address)
    TextView FragmentDetailTvHospitalAddress;
    @BindView(R.id.Fragment_detail_tv_phone_numer)
    TextView FragmentDetailTvPhoneNumer;
    @BindView(R.id.Fragment_detail_tv_notes)
    TextView FragmentDetailTvNotes;
    @BindView(R.id.Fragment_detail_btn_call)
    Button FragmentDetailBtnCall;
    @BindView(R.id.Fragment_detail_img_menu)
    ImageView FragmentDetailImgMenu;
    @BindView(R.id.Fragment_detail_img_notifications)
    ImageView FragmentDetailImgNotifications;
    @BindView(R.id.Fragment_detail_Tool_bar_title)
    TextView FragmentDetailToolBarTitle;
    @BindView(R.id.Fragment_detail_img_back)
    ImageView FragmentDetailImgBack;
    MapView mMapView;
    private int returnResult = 0;
    private String getDonationRequestId;
    private GoogleMap googleMap;

    private Activity activity;
    Unbinder unbinder;
    private API ApiServices;
    private String Api_token = "W4mx3VMIWetLcvEcyF554CfxjZHwdtQldbdlCl2XAaBTDIpNjKO1f7CHuwKl";

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        unbinder = ButterKnife.bind(this, view);

        FragmentDetailImgMenu.setVisibility(View.INVISIBLE);
        FragmentDetailImgNotifications.setVisibility(View.VISIBLE);
        FragmentDetailImgBack.setVisibility(View.VISIBLE);
        FragmentDetailToolBarTitle.setText(donationsdata.getPatientName());

        ApiServices = RetrofitClient.getClient().create(API.class);
//
//      getDataReturnDetails();

        setDonationData();


        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // For showing a move to my location button
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                googleMap.setMyLocationEnabled(true);

                // For dropping a marker at a point on the Map

                String latitude = donationsdata.getLatitude();
                String longitude = donationsdata.getLongitude();

                LatLng sydney = new LatLng(Double.valueOf(latitude), Double.valueOf(longitude));
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    private void setDonationData() {


        FragmentDetailTvName.setText(getResources().getString(R.string.patientname) + " :" + donationsdata.getPatientName());
        FragmentDetailTvAge.setText(getResources().getString(R.string.age) + " :" + donationsdata.getPatientAge());
        FragmentDetailTvPhoneNumer.setText(getResources().getString(R.string.phone) + " :" + donationsdata.getPhone());
        FragmentDetailTvBagsnumber.setText(getResources().getString(R.string.bugs_number) + " :" + donationsdata.getBagsNum());
        FragmentDetailTvBloodtype.setText(getResources().getString(R.string.bloodtype) + " :" + donationsdata.getBloodType().getName());
        FragmentDetailTvHospitalName.setText(getResources().getString(R.string.hospital) + " :" + donationsdata.getHospitalName());
        FragmentDetailTvHospitalAddress.setText(getResources().getString(R.string.hospital_address) + " :" + donationsdata.getHospitalAddress());
        FragmentDetailTvNotes.setText(getResources().getString(R.string.note) + " :" + donationsdata.getNotes());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.Fragment_detail_btn_call)
    public void onViewClicked() {

        String phone = donationsdata.getPhone();
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
        startActivity(intent);
    }

    @OnClick({R.id.Fragment_detail_img_notifications, R.id.Fragment_detail_img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Fragment_detail_img_notifications:

                NotificationsFragment notificationsFragment = new NotificationsFragment();
                HelperMethods.replace(notificationsFragment, getActivity().getSupportFragmentManager(), R.id.Activity_home_Frame, null, null);
                break;

            case R.id.Fragment_detail_img_back:
                homeFragment homeFragment = new homeFragment();
                HelperMethods.replace(homeFragment, getActivity().getSupportFragmentManager(), R.id.frame_home_cycle, null, null);
                break;
        }
    }

    private void getDataReturnDetails() {

        // get data Donation Requests Adapter Recycler returnResult == 0   and return notification adapter   returnResult == 1
        Bundle bundle = getArguments();
        if (bundle != null) {
            returnResult = bundle.getInt("returnResult");
            if (returnResult == 0) {
                // get change is read Notification and get data notify
                getDonationData(bundle.getInt("getId"));

            } else if (returnResult == 1) {
                // get data Notification Requests Adapter Recycler
                getDonationRequestId = bundle.getString("getDonationRequestId");
                // get change is read Notification and get data notify
                assert getDonationRequestId != null;
                getDonationData(Integer.parseInt(getDonationRequestId));

            }
        }
    }

    private void getDonationData(int getId) {


        ApiServices.getDontationRequestNotifi(Api_token, getId).enqueue(new Callback<DonationRequestNotifi>() {
            @Override
            public void onResponse(Call<DonationRequestNotifi> call, Response<DonationRequestNotifi> response) {

                try {

                    if (response.body().getStatus() == 1) {

                        FragmentDetailTvName.setText("الاسم :" + response.body().getData().getPatientName());
                        FragmentDetailTvAge.setText("العمر :" + response.body().getData().getPatientAge());
                        FragmentDetailTvPhoneNumer.setText("رقم الهاتف :" + response.body().getData().getPhone());
                        FragmentDetailTvBagsnumber.setText("عدد الاكياس :" + response.body().getData().getBagsNum());
                        FragmentDetailTvBloodtype.setText("فصيلة الدم :" + response.body().getData().getBloodType().getName());
                        FragmentDetailTvHospitalName.setText("اسم المستشفي :" + response.body().getData().getHospitalName());
                        FragmentDetailTvHospitalAddress.setText("عنوان المستشفي :" + response.body().getData().getHospitalAddress());
                        FragmentDetailTvNotes.setText("ملاحظات :" + response.body().getData().getNotes());

                    }

                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DonationRequestNotifi> call, Throwable t) {

            }
        });

    }
}