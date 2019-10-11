package com.example.mustafa.bloodbank.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.models.artical.ArticalData;
import com.example.mustafa.bloodbank.data.models.posttogglefavourite.PostToggleFavourite;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.USER_API_TOKEN;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.favouriteholder> {
    private final Context context;
    private final Activity activity;
    private final List<ArticalData> favouriteArtical;
    private API ApiServices;
    private String api_token = "";

    public FavouriteAdapter(Context context, Activity activity, List<ArticalData> favouriteArtical) {
        this.context = context;
        this.activity = activity;
        this.favouriteArtical = favouriteArtical;
    }

    @NonNull
    @Override
    public favouriteholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.grid_favourite, parent, false);
        ApiServices = RetrofitClient.getClient().create(API.class);
        return new favouriteholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull favouriteholder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setAction(final favouriteholder holder, final int position) {
        api_token = SharedPreferencesManger.LoadData(activity, USER_API_TOKEN);
        holder.FragmentFavouriteImageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiServices.addposttogglefavourite(favouriteArtical.get(position).getId(), api_token)
                        .enqueue(new Callback<PostToggleFavourite>() {
                            @Override
                            public void onResponse(Call<PostToggleFavourite> call, Response<PostToggleFavourite> response) {
                                if (response.body().getStatus() == 1) {
                                    favouriteArtical.get(position).setIsFavourite(!favouriteArtical.get(position).getIsFavourite());
                                    if (favouriteArtical.get(position).getIsFavourite()) {
                                        holder.FragmentFavouriteImageLike.setImageResource(R.drawable.ic_favorite);
                                        Toast.makeText(activity, "تم الاضافة", Toast.LENGTH_SHORT).show();
                                    } else {
                                        holder.FragmentFavouriteImageLike.setImageResource(R.drawable.likes);
                                        notifyItemRemoved(position);
                                        notifyDataSetChanged();
                                        Toast.makeText(activity, "تم الازالة", Toast.LENGTH_SHORT).show();
                                    }
                                } else {

                                    favouriteArtical.get(position).setIsFavourite(!favouriteArtical.get(position).getIsFavourite());
                                    if (favouriteArtical.get(position).getIsFavourite()) {
                                        holder.FragmentFavouriteImageLike.setImageResource(R.drawable.ic_favorite);
                                        Toast.makeText(activity, "تم الاضافة", Toast.LENGTH_SHORT).show();
                                    } else {

                                        holder.FragmentFavouriteImageLike.setImageResource(R.drawable.likes);
                                        notifyItemRemoved(position);
                                        notifyDataSetChanged();
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

    private void setData(favouriteholder holder, int position) {
        Glide.with(context).load(favouriteArtical.get(position).getThumbnailFullPath()).into(holder.FragmentFavouriteImagePost);
        holder.FragmentFavouriteTvPost.setText(favouriteArtical.get(position).getTitle());
        if (favouriteArtical.get(position).getIsFavourite()) {
            holder.FragmentFavouriteImageLike.setImageResource(R.drawable.ic_favorite);
        } else {
            holder.FragmentFavouriteImageLike.setImageResource(R.drawable.likes);
        }
    }

    @Override
    public int getItemCount() {
        return favouriteArtical.size();
    }


    public class favouriteholder extends RecyclerView.ViewHolder {
        private View view;
        @BindView(R.id.Fragment_favourite_tv_post)
        TextView FragmentFavouriteTvPost;
        @BindView(R.id.Fragment_favourite_image_like)
        ImageView FragmentFavouriteImageLike;
        @BindView(R.id.Fragment_favourite_relative)
        RelativeLayout FragmentFavouriteRelative;
        @BindView(R.id.Fragment_favourite_image_post)
        ImageView FragmentFavouriteImagePost;
        @BindView(R.id.Fragment_favourite_card)
        CardView FragmentFavouriteCard;
        public favouriteholder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);

        }
    }
}





