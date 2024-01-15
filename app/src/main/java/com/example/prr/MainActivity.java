package com.example.prr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategorClickInterface{
//9befd62b663d499fa10996e176679c01
    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModal> categoryRVModalArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRV=findViewById(R.id.idRVNews);
        categoryRV=findViewById(R.id.idRVCategories);
        loadingPB=findViewById(R.id.idPBLoading);
        articlesArrayList=new ArrayList<>();
        categoryRVModalArrayList=new ArrayList<>();
        newsRVAdapter=new NewsRVAdapter(articlesArrayList,this);
        categoryRVAdapter=new CategoryRVAdapter(categoryRVModalArrayList,this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();
    }
    private void getCategories(){
        categoryRVModalArrayList.add(new CategoryRVModal("All","https://media.istockphoto.com/id/1311148884/vector/abstract-globe-background.jpg?s=612x612&w=0&k=20&c=9rVQfrUGNtR5Q0ygmuQ9jviVUfrnYHUHcfiwaH5-WFE="));
        categoryRVModalArrayList.add(new CategoryRVModal("Technology","https://images.unsplash.com/photo-1504164996022-09080787b6b3?ixlib=rb-1.2.1&ixid=eyJhcHBFAWQiOjEyMDd9&auto=format&fit=crop&w=500&q=60"));
        categoryRVModalArrayList.add(new CategoryRVModal("Science","https://img.freepik.com/free-vector/science-word-theme_23-2148540555.jpg"));
        categoryRVModalArrayList.add(new CategoryRVModal("Sports","https://upload.wikimedia.org/wikipedia/commons/d/db/Sports_portal_bar_icon.png"));
        categoryRVModalArrayList.add(new CategoryRVModal("General","https://media.istockphoto.com/id/1345527119/video/graphical-modern-digital-world-news-studio-loop-background.jpg?s=640x640&k=20&c=cr1SYYf7Dix-TgBqiYRLquAmi7TgEE3oZcMUExQ25QY="));
        categoryRVModalArrayList.add(new CategoryRVModal("Business","https://as1.ftcdn.net/v2/jpg/05/71/39/98/1000_F_571399819_3H700ngoB1IQnQT4FoKFRq95BPSeHuWd.jpg"));
        categoryRVModalArrayList.add(new CategoryRVModal("Entertainment","https://w7.pngwing.com/pngs/793/477/png-transparent-entertainment-cinema-film-movie-theatre-miscellaneous-television-film-thumbnail.png"));
        categoryRVModalArrayList.add(new CategoryRVModal("Health","https://w7.pngwing.com/pngs/490/489/png-transparent-medical-background-doctors-medical-see-a-doctor-thumbnail.png"));
        categoryRVAdapter.notifyDataSetChanged();
    }
    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL="https://newsapi.org/v2/top-headlines?country=in&category="+category+"&apikey=9befd62b663d499fa10996e176679c01 ";
        String url="https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=9befd62b663d499fa10996e176679c01";
        String BASE_URL="https://newsapi.org/";
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitAPI retrofitAPI= retrofit.create(RetrofitAPI.class);
        Call<NewsModal> call;
        if(category.equals("All")){
            call= retrofitAPI.getAllNews(url);
        }
        else{
            call= retrofitAPI.getNewsByCategory(categoryURL);
        }
        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                NewsModal newsModal=response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles= newsModal.getArticles();
                for(int i=0;i<articles.size();i++){
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),articles.get(i).getDescription(),articles.get(i).getUrlToImage(),articles.get(i).getUrl(),articles.get(i).getContent()));
                }
                newsRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail to get News", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onCategoryClick(int position) {
       String category=categoryRVModalArrayList.get(position).getCategory();
        getNews(category);
    }
}