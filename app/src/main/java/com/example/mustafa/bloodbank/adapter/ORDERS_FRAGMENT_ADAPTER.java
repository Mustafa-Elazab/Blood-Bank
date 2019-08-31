package com.example.mustafa.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.data.model.donation_request_create.DonationRequest;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.activity.HomeActivity;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.DetailFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ORDERS_FRAGMENT_ADAPTER extends RecyclerView.Adapter<ORDERS_FRAGMENT_ADAPTER.ORDERHOLDER> {
    LinearLayout LinButtons;
    private Activity activity;
    private final Context context;

    private List<DonationRequest> data = new ArrayList<>();

    public ORDERS_FRAGMENT_ADAPTER(Context context, Activity activity) {

        this.context = context;
        this.activity = activity;

    }

    @NonNull
    @Override
    public ORDERHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.grid_order, parent, false);

        return new ORDERHOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ORDERHOLDER holder, int position) {

        DonationRequest datum = data.get(position);

        holder.FragmentOrdersTvBloodtype.setText(datum.getBloodType().getName());
        holder.FragmentOrdersTvHospital.setText("اسم المستشفي :" + datum.getHospitalName());
        holder.FragmentOrdersTvCity.setText("اسم المدينة :" + datum.getCity().getName());
        holder.FragmentOrdersTvName.setText("اسم الحالة :" + datum.getPatientName());


        setAction(holder, position);
    }

    private void setAction(final ORDERHOLDER holder, final int position) {

        holder.FragmentOrdersBtnDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeActivity homeActivity = (HomeActivity) activity;
                homeActivity.getVisibility(View.VISIBLE);
                DetailFragment detailFragment = new DetailFragment();
                detailFragment.donationsdata = data.get(position);
                HelperMethods.replace(detailFragment, ((HomeActivity) activity).getSupportFragmentManager(), R.id.Activity_home_Frame, null
                        , null);

            }
        });

        holder.FragmentOrdersBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone = data.get(position).getPhone();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }


    public void sendDataToAdapter(List<DonationRequest> data) {
        this.data = data;
    }

    public class ORDERHOLDER extends RecyclerView.ViewHolder {

        @BindView(R.id.Fragment_orders_tv_bloodtype)
        TextView FragmentOrdersTvBloodtype;
        @BindView(R.id.Fragment_orders_tv_name)
        TextView FragmentOrdersTvName;
        @BindView(R.id.Fragment_orders_tv_hospital)
        TextView FragmentOrdersTvHospital;
        @BindView(R.id.Fragment_orders_tv_city)
        TextView FragmentOrdersTvCity;
        @BindView(R.id.Fragment_orders_rlt_detail)
        LinearLayout FragmentOrdersRltDetail;
        @BindView(R.id.Fragment_orders_btn_detail)
        Button FragmentOrdersBtnDetail;
        @BindView(R.id.Fragment_orders_btn_call)
        Button FragmentOrdersBtnCall;
        public ORDERHOLDER(View itemView) {
            super(itemView);


        }
    }
}
