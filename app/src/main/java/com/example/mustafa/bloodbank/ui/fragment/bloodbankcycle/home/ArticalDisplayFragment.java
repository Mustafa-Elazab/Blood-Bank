package com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.ui.fragment.BaseFragment;
import com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.NotificationsFragment;

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
public class ArticalDisplayFragment extends BaseFragment {
    public ArticalData articaldata;
    @BindView(R.id.Fragment_Display_Artical_image_post)
    ImageView FragmentDisplayArticalImagePost;
    @BindView(R.id.Fragment_Display_Artical_tv_post_post)
    TextView FragmentDisplayArticalTvPostPost;
    @BindView(R.id.Fragment_Display_Artical_tv_post)
    TextView FragmentDisplayArticalTvPost;
    @BindView(R.id.Fragment_Display_Artical_image_like)
    ImageView FragmentDisplayArticalImageLike;
    @BindView(R.id.Fragment_Display_Artical_rlt)
    RelativeLayout FragmentDisplayArticalRlt;
    @BindView(R.id.Fragmet_Display_Artical_img_menu)
    ImageView FragmetDisplayArticalImgMenu;
    @BindView(R.id.Fragmet_Display_Artical_img_notifications)
    ImageView FragmetDisplayArticalImgNotifications;
    @BindView(R.id.Fragmet_Display_Artical_Tool_bar_title)
    TextView FragmetDisplayArticalToolBarTitle;
    @BindView(R.id.Fragmet_Display_Artical_img_back)
    ImageView FragmetDisplayArticalImgBack;
    Unbinder unbinder;
    private API ApiServices;
    private String api_token="";

    public ArticalDisplayFragment() {


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SetUpAvtivity();
        View view = inflater.inflate(R.layout.fragment_display_artical, container, false);
        unbinder = ButterKnife.bind(this, view);
        ApiServices= RetrofitClient.getClient().create(API.class);
        FragmetDisplayArticalImgMenu.setVisibility(View.INVISIBLE);
        FragmetDisplayArticalImgNotifications.setVisibility(View.VISIBLE);
        FragmetDisplayArticalImgBack.setVisibility(View.VISIBLE);
        FragmetDisplayArticalToolBarTitle.setText(articaldata.getTitle());
        getArticalDetail();
        return view;
    }

    private void getArticalDetail() {
        Glide.with(getContext()).load(articaldata.getThumbnailFullPath()).into(FragmentDisplayArticalImagePost);
        FragmentDisplayArticalTvPost.setText(articaldata.getTitle());
        FragmentDisplayArticalTvPostPost.setText(articaldata.getContent());
        if (articaldata.getIsFavourite()) {
            FragmentDisplayArticalImageLike.setImageResource(R.drawable.ic_favorite);
        } else {
            FragmentDisplayArticalImageLike.setImageResource(R.drawable.likes);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.Fragment_Display_Artical_image_like)
    public void onViewClicked() {



        FragmentDisplayArticalImageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                api_token=SharedPreferencesManger.LoadData(getActivity(),USER_API_TOKEN);
                ApiServices.addposttogglefavourite(articaldata.getId(),api_token)
                        .enqueue(new Callback<PostToggleFavourite>() {
                            @Override
                            public void onResponse(Call<PostToggleFavourite> call, Response<PostToggleFavourite> response) {

                                if (response.body().getStatus()==1) {

                                    articaldata.setIsFavourite(!articaldata.getIsFavourite());
                                    if (articaldata.getIsFavourite()) {

                                        FragmentDisplayArticalImageLike.setImageResource(R.drawable.ic_favorite);
                                        Toast.makeText(getActivity(), "تم الاضافه", Toast.LENGTH_SHORT).show();
                                    } else {

                                        FragmentDisplayArticalImageLike.setImageResource(R.drawable.likes);
                                        Toast.makeText(getActivity(), "تم الازالة", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {

                                    articaldata.setIsFavourite(!articaldata.getIsFavourite());
                                    if(articaldata.getIsFavourite()){
                                        FragmentDisplayArticalImageLike.setImageResource(R.drawable.ic_favorite);
                                        Toast.makeText(getActivity(), "تم الاضافة", Toast.LENGTH_SHORT).show();
                                    }else {
                                        FragmentDisplayArticalImageLike.setImageResource(R.drawable.likes);
                                        Toast.makeText(getActivity(), "تم الازالة", Toast.LENGTH_SHORT).show();
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

    @OnClick({R.id.Fragmet_Display_Artical_img_notifications, R.id.Fragmet_Display_Artical_img_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.Fragmet_Display_Artical_img_notifications:
                NotificationsFragment notificationsFragment=new NotificationsFragment();
                HelperMethods.replace(notificationsFragment,getActivity().getSupportFragmentManager(),R.id.frame_home_cycle,null,null);
                break;
            case R.id.Fragmet_Display_Artical_img_back:
                homeFragment homeFragment = new homeFragment();
                HelperMethods.replace(homeFragment,getActivity().getSupportFragmentManager(), R.id.frame_home_cycle, null, null);
                break;
        }
    }

    @Override
    public void onBack() {
        homeFragment homeFragment = new homeFragment();
        HelperMethods.replace(homeFragment,getActivity().getSupportFragmentManager(), R.id.frame_home_cycle, null, null);
    }
}
