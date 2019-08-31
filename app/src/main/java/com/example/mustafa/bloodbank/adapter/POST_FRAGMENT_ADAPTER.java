package com.example.mustafa.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.model.posts.Data;
import com.example.mustafa.bloodbank.data.model.posts.Datum;
import com.example.mustafa.bloodbank.data.model.posttogglefavourite.PostToggleFavourite;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.activity.HomeActivity;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.home.ArticalDisplayFragment;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.USER_API_TOKEN;

public class POST_FRAGMENT_ADAPTER extends RecyclerView.Adapter<POST_FRAGMENT_ADAPTER.POSTHOLDER> {


    private Activity activity;
    private final Context context;

    private List<Datum> data;
    private int post_id;
    private API ApiServices;

    public POST_FRAGMENT_ADAPTER(Context context,Activity activity) {

        this.context = context;
        this.activity=activity;
    }

    @NonNull
    @Override
    public POSTHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.grid_post, parent, false);

        ApiServices = RetrofitClient.getClient().create(API.class);

        return new POSTHOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final POSTHOLDER holder, int position) {

        Datum datum = data.get(position);


        post_id=datum.getId();
        holder.FragmentPostTvPost.setText(datum.getTitle());
        Glide.with(context).load(datum.getThumbnailFullPath()).into(holder.FragmentPostImagePost);

        if(datum.getIsFavourite()){

            holder.FragmentPostImageLike.setImageResource(R.drawable.ic_favorite);

        }else {
            holder.FragmentPostImageLike.setImageResource(R.drawable.likes);

        }



        setAction(holder , position);


    }

    private void setAction(final POSTHOLDER holder, final int position) {


        holder.FragmentPostImagePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HomeActivity homeActivity=(HomeActivity)activity;
                homeActivity.getVisibility(View.VISIBLE);

                ArticalDisplayFragment fragment=new ArticalDisplayFragment();
                fragment.articaldata=data.get(position);

                HelperMethods.replace(fragment,((HomeActivity) activity).getSupportFragmentManager(),R.id.Activity_home_Frame,null
                ,null);

            }
        });

        holder.FragmentPostImageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApiServices.addposttogglefavourite(data.get(position).getId(), SharedPreferencesManger.LoadData(activity,USER_API_TOKEN))
                        .enqueue(new Callback<PostToggleFavourite>() {
                            @Override
                            public void onResponse(Call<PostToggleFavourite> call, Response<PostToggleFavourite> response) {

                                if (response.body().getStatus()==1) {


                                }
                                else {

                                    data.get(position).setIsFavourite(!data.get(position).getIsFavourite());
                                    if(data.get(position).getIsFavourite()){

                                        holder.FragmentPostImageLike.setImageResource(R.drawable.ic_favorite);
                                        Toast.makeText(activity, "تم الاضافه", Toast.LENGTH_SHORT).show();
                                    }else {

                                        holder.FragmentPostImageLike.setImageResource(R.drawable.likes);
                                        Toast.makeText(activity, "تم الازالة", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }

                            @Override
                            public void onFailure(Call<PostToggleFavourite> call, Throwable t) {

                            }
                        });

            }
        });
    }

    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void getData(List<Datum> data) {
        this.data = data;
    }


    public class POSTHOLDER extends RecyclerView.ViewHolder {

        @BindView(R.id.Fragment_post_tv_post)
        TextView FragmentPostTvPost;
        @BindView(R.id.Fragment_post_image_like)
        ImageView FragmentPostImageLike;
        @BindView(R.id.Fragment_post_image_post)
        ImageView FragmentPostImagePost;
        public POSTHOLDER(View itemView) {
            super(itemView);


        }
    }

}
