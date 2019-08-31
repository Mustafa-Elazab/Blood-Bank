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
import com.example.mustafa.bloodbank.data.model.myfavourites.Datum;
import com.example.mustafa.bloodbank.data.model.posttogglefavourite.PostToggleFavourite;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;

import java.util.List;

import butterknife.BindView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.USER_API_TOKEN;
import static com.example.mustafa.bloodbank.helper.HelperMethods.onLoadImageFromUrl;

public class FAVOURITE_FRAGMENT_ADAPTER extends RecyclerView.Adapter<FAVOURITE_FRAGMENT_ADAPTER.FAVOURITEHOLDER> {
    private final Context context;
    private final Activity activity;
    private API ApiServices;
    private List<Datum> data;


    public FAVOURITE_FRAGMENT_ADAPTER(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public FAVOURITEHOLDER onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(context).inflate(R.layout.grid_favourite, parent, false);
        ApiServices = RetrofitClient.getClient().create(API.class);
        return new FAVOURITEHOLDER(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FAVOURITEHOLDER holder, int position) {


        data.get(position);
        setData(holder, position);
        setAction(holder, position);


    }

    private void setAction(final FAVOURITEHOLDER holder, final int position) {


        holder.FragmentFavouriteImageLike.setOnClickListener(new View.OnClickListener() {
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

                                        holder.FragmentFavouriteImageLike.setImageResource(R.drawable.ic_favorite);
                                        Toast.makeText(activity, "تم الاضافة", Toast.LENGTH_SHORT).show();
                                    }else {

                                        holder.FragmentFavouriteImageLike.setImageResource(R.drawable.likes);
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

    private void setData(FAVOURITEHOLDER holder, int position) {

        onLoadImageFromUrl(holder.FragmentPostImagePost, data.get(position).getThumbnailFullPath(), context);

        holder.FragmentFavouriteTvPost.setText(data.get(position).getTitle());

        if (data.get(position).getIsFavourite()) {

            holder.FragmentFavouriteImageLike.setImageResource(R.drawable.ic_favorite);

        } else {

            holder.FragmentFavouriteImageLike.setImageResource(R.drawable.likes);

        }
    }


    @Override
    public int getItemCount() {
        return data != null ? data.size() : 0;
    }

    public void sendDataToAdapter(List<Datum> data) {
        this.data = data;
    }


    public class FAVOURITEHOLDER extends RecyclerView.ViewHolder {


        @BindView(R.id.Fragment_favourite_tv_post)
        TextView FragmentFavouriteTvPost;
        @BindView(R.id.Fragment_favourite_image_like)
        ImageView FragmentFavouriteImageLike;
        @BindView(R.id.Fragment_post_image_post)
        ImageView FragmentPostImagePost;
        public FAVOURITEHOLDER(View itemView) {
            super(itemView);


        }
    }
}





