package com.example.mustafa.bloodbank.ui.fragment.bloodbankcycle.home;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mustafa.bloodbank.R;
import com.example.mustafa.bloodbank.adapter.ArticalAdapter;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.models.artical.Artical;
import com.example.mustafa.bloodbank.data.models.artical.ArticalData;
import com.example.mustafa.bloodbank.data.models.gerneral.GeneralResponse;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;
import com.example.mustafa.bloodbank.helper.HelperMethods;
import com.example.mustafa.bloodbank.helper.OnEndless;
import com.example.mustafa.bloodbank.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

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
public class postsFragment extends BaseFragment {


    @BindView(R.id.Fragment_post_spinner)
    Spinner FragmentPostSpinner;
    @BindView(R.id.Fragment_post_recyclerview)
    RecyclerView FragmentPostRecyclerview;
    @BindView(R.id.Fragment_posts_img_search)
    ImageView FragmentPostsImgSearch;
    @BindView(R.id.Fragment_post_ed_search)
    EditText FragmentPostEdSearch;
    @BindView(R.id.Fragment_post_relative)
    LinearLayout FragmentPostRelative;
    Unbinder unbinder;
    private API ApiServices;
    private ArticalAdapter adapter;
    private String api_token = "";
    private int categorie_id;
    private List<Integer> ids;
    private String keyword;
    private List<ArticalData> articalData=new ArrayList<>();
    private List<ArticalData> data;
    private Integer max = 0;
    private OnEndless onEndless;
    private int finalCurrent_page = 0;
    private boolean checkFilterPost = true;


    public postsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        SetUpAvtivity();
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        unbinder = ButterKnife.bind(this, view);
        api_token = SharedPreferencesManger.LoadData(getActivity(), USER_API_TOKEN);
        ApiServices = RetrofitClient.getClient().create(API.class);
        Log.i( "apiToken",api_token);
        getData(1);
        getCategories();
        SetupRecyclerView();
        return view;
    }

    private void getCategories() {
        ApiServices.getcategories().enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                if (response.body().getStatus() == 1) {
                    try {
                        List<String> categories = new ArrayList<>();
                        ids = new ArrayList<>();
                        categories.add("كل المقالات");
                        ids.add(0);
                        for (int i = 0; i < response.body().getData().size(); i++) {
                            categories.add(response.body().getData().get(i).getName());
                            ids.add(response.body().getData().get(i).getId());
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                                    android.R.layout.simple_spinner_item, categories
                            );

                            FragmentPostSpinner.setAdapter(adapter);
                            FragmentPostSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    if (position == 0) {
                                    } else {
                                        categorie_id = ids.get(position);
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }

                    } catch (Exception e) {
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }


    private void getData(int page) {
        ApiServices.getposts(api_token, page).enqueue(new Callback<Artical>() {
            @Override
            public void onResponse(Call<Artical> call, Response<Artical> response) {
//                Log.i("onPostData",response.body().getData().getData().toString());
                try{

                    if (response.body().getStatus()==1) {
                        data = response.body().getData().getData();
                        max = response.body().getData().getLastPage();
                        finalCurrent_page=response.body().getData().getCurrentPage();
                        articalData.addAll(data);
                        adapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
                    e.getMessage();
                }

            }

            @Override
            public void onFailure(Call<Artical> call, Throwable t) {

            }
        });
    }




    private void SetupRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        FragmentPostRecyclerview.setLayoutManager(manager);

        onEndless = new OnEndless(manager, 1) {
            @Override
            public void onLoadMore(int current_page) {

                if (current_page <= max || max != 0 || current_page == 1) {
                    if (checkFilterPost) {
                        getData(1);
                    } else {
                        getDataFilter(1);
                    }
                }
            }
        };
        FragmentPostRecyclerview.addOnScrollListener(onEndless);
        adapter=new ArticalAdapter(getActivity(),getActivity(),articalData);
        FragmentPostRecyclerview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @OnClick(R.id.Fragment_posts_img_search)
    public void onViewClicked() {

        if (categorie_id == 0 && keyword == "") {
            getData(1);
        } else {
             getDataFilter(1);
        }
    }

    private void getDataFilter(int page) {

        keyword = FragmentPostEdSearch.getText().toString();

        ApiServices.getpostfilter(api_token,page,keyword,categorie_id).enqueue(new Callback<Artical>() {
            @Override
            public void onResponse(Call<Artical> call, Response<Artical> response) {
            try {
                if (response.body().getStatus()==1) {
                    List<ArticalData> data = response.body().getData().getData();
                    articalData.addAll(data);
                    adapter.notifyDataSetChanged();
                }

            }catch (Exception e){
                e.getMessage();
            }
            }

            @Override
            public void onFailure(Call<Artical> call, Throwable t) {

            }
        });
    }
    @Override
    public void onBack() {
        homeFragment homeFragment = new homeFragment();
        HelperMethods.replace(homeFragment,getActivity().getSupportFragmentManager(), R.id.frame_home_cycle, null, null);
    }
}

