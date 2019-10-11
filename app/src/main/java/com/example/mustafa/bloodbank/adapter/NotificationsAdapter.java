package com.example.mustafa.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.models.donationrequestnotifi.DonationRequestNotifi;
import com.example.mustafa.bloodbank.data.models.notifications.DataNotify;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.activity.HomeActivity;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.DetailFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.USER_API_TOKEN;
import static com.example.mustafa.bloodbank.data.rest.RetrofitClient.getClient;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {


    private Context context;
    private Activity activity;
    private List<DataNotify> data;
    private API apiServices;
    // private List<Notifications> restaurantDataList = new ArrayList<>();

    public NotificationsAdapter(Context context, FragmentActivity activity) {
        this.activity = activity;
        this.context = context;
        apiServices = getClient().create(API.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_notifications,
                parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {

        DataNotify notificationsDatum = data.get(position);
        holder.listNotificationsRvText.setText(notificationsDatum.getTitle());
        holder.listNotificationsRvTime.setText(notificationsDatum.getCreatedAt());
        String isRead = notificationsDatum.getPivot().getIsRead();
        Toast.makeText(context, isRead, Toast.LENGTH_SHORT).show();
        if (isRead.equals("1")) {
            holder.listNotificationsRvNotifiy.setImageResource(R.drawable.ic_notifications_black_24dp);

        } else {
            holder.listNotificationsRvNotifiy.setImageResource(R.drawable.ic_notifications);
        }


    }

    private void setAction(ViewHolder holder, final int position) {

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDonationData(Integer.parseInt(data.get(position).getDonationRequestId()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void getData(List<DataNotify> data) {
        this.data = data;
    }


    private void getDonationData(int getId) {
        String api_token = SharedPreferencesManger.LoadData(activity, USER_API_TOKEN);
        apiServices.getDontationRequestNotifi(api_token, getId).enqueue(new Callback<DonationRequestNotifi>() {
            @Override
            public void onResponse(Call<DonationRequestNotifi> call, Response<DonationRequestNotifi> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        HomeActivity homeActivity = (HomeActivity) activity;
                        homeActivity.getVisibility(View.VISIBLE);
                        DetailFragment detailFragment = new DetailFragment();
                        detailFragment.donationsdata = response.body().getData();
                        HelperMethods.replace(detailFragment, ((HomeActivity) activity).getSupportFragmentManager(), R.id.Activity_home_Frame, null
                                , null);
                    }

                } catch (Exception e) {
                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<DonationRequestNotifi> call, Throwable t) {

            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_notifications_rv_notifiy)
        ImageView listNotificationsRvNotifiy;
        @BindView(R.id.list_notifications_rv_text)
        TextView listNotificationsRvText;
        @BindView(R.id.list_notifications_rv_time)
        TextView listNotificationsRvTime;
        private View view;


        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);


        }
    }

}

