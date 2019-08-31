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
import com.example.mustafa.bloodbank.adapter.POST_FRAGMENT_ADAPTER;
import com.example.mustafa.bloodbank.data.local.SharedPreferencesManger;
import com.example.mustafa.bloodbank.data.model.categories.Categories;
import com.example.mustafa.bloodbank.data.model.post_filter.PostFilter;
import com.example.mustafa.bloodbank.data.model.posts.Datum;
import com.example.mustafa.bloodbank.data.model.posts.Posts;
import com.example.mustafa.bloodbank.data.rest.API;
import com.example.mustafa.bloodbank.data.rest.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;
import static com.example.mustafa.bloodbank.data.local.SharedPreferencesManger.USER_API_TOKEN;

/**
 * A simple {@link Fragment} subclass.
 */
public class postsFragment extends Fragment {


    @BindView(R.id.Fragment_post_spinner)
    Spinner FragmentPostSpinner;
    @BindView(R.id.Fragment_post_recyclerview)
    RecyclerView FragmentPostRecyclerview;
    Unbinder unbinder;
    @BindView(R.id.Fragment_posts_img_search)
    ImageView FragmentPostsImgSearch;
    @BindView(R.id.Fragment_post_ed_search)
    EditText FragmentPostEdSearch;
    @BindView(R.id.Fragment_post_relative)
    LinearLayout FragmentPostRelative;
    private API ApiServices;
    private POST_FRAGMENT_ADAPTER adapter;
    private String api_token;
    private int categorie_id;
    private List<Integer> ids;
    private String keyword;
    private Posts postsResponse;

    public postsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        unbinder = ButterKnife.bind(this, view);

        api_token = SharedPreferencesManger.LoadData(getActivity(), USER_API_TOKEN);
        ApiServices = RetrofitClient.getClient().create(API.class);
        SetupRecyclerView();
        getData();
        getCategories();
        return view;
    }

    private void getCategories() {

        ApiServices.getcategories().enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {

                if (response.body().getStatus() == 1) {

                    try {

                        Categories body = response.body();

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
            public void onFailure(Call<Categories> call, Throwable t) {

            }
        });
    }


    private void getData() {


        ApiServices.getposts(api_token, 1).enqueue(new Callback<Posts>() {
            @Override
            public void onResponse(Call<Posts> call, Response<Posts> response) {

                if (response.body().getStatus() == 1) {
                    try {

                        postsResponse = response.body();
                        viewposts(postsResponse);

                    } catch (Exception e) {

                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onFailure(Call<Posts> call, Throwable t) {

            }
        });
    }

    private void viewposts(Posts postsResponse) {

        List<Datum> data = postsResponse.getData().getData();

        adapter.getData(data);
        adapter.notifyDataSetChanged();
    }

    private void SetupRecyclerView() {

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        FragmentPostRecyclerview.setLayoutManager(manager);

        adapter = new POST_FRAGMENT_ADAPTER(getContext(),getActivity());
        FragmentPostRecyclerview.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void getPostsFilter() {


        String keyword = FragmentPostEdSearch.getText().toString();


        List<Datum> data = postsResponse.getData().getData();
        adapter.getData(data);
        adapter.notifyDataSetChanged();


        try {


            ApiServices.getpostfilter(api_token, 1, keyword, categorie_id).enqueue(new Callback<Posts>() {
                @Override
                public void onResponse(Call<Posts> call, Response<Posts> response) {


                    if (response.body().getStatus() == 1) {

                        Posts postsResponse = response.body();
                        viewposts(postsResponse);
                    }
                }

                @Override
                public void onFailure(Call<Posts> call, Throwable t) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.Fragment_posts_img_search)
    public void onViewClicked() {

        if (categorie_id == 0 && keyword == "") {
            getData();
        } else {
            getPostsFilter();
        }
    }
}

