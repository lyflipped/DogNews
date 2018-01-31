package com.example.liyang.meterialdesigntest;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Response;
import tools.HttpUtil;
import tools.ParseNews;
import tools.TypeFace;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class News_Detail extends AppCompatActivity {
    public static final String News_Title = "news_title";
    public static final String News_Content = "news_content";
    public static final String News_Image_Id = "news_image_id";
    public static final String News_Image_Url = "news_image_url";
    public static final String News_Url = "news_url";
    public String source="";
    private Intent intent;
    private String title;
    private String url;
    private int ImageId;
    private String ImageUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
        initdata();
        getSource();
    }
    public void initdata(){
        intent = getIntent();
        title = intent.getStringExtra(News_Title);
        url = intent.getStringExtra(News_Url);
        ImageId = intent.getIntExtra(News_Image_Id,0);
        ImageUrl = intent.getStringExtra(News_Image_Url);
    }
    public void initview(){

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        ImageView imageView = (ImageView) findViewById(R.id.newspic);
       // TextView news_title = (TextView) findViewById(R.id.news_title);
        ParseNews.setDocument(source);
       // ArrayList<String> texts;
      //  texts = ParseNews.getText();
//        news_title.setTypeface(TypeFace.typeface);
//        news_title.setText(title);
     //   LinearLayout lly = (LinearLayout)findViewById(R.id.lly);
//        for(int i=0;i<texts.size();i++){
//            TextView tv =new TextView(this);
//            tv.setText(texts.get(i));
//            tv.setTypeface(TypeFace.typeface);
//            tv.setTextSize(20);
//            lly.addView(tv);
//        }
        WebView webView = (WebView) findViewById(R.id.web_view);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        collapsingToolbar.setTitle(title);
        if(ImageUrl.equals("")){
            Glide.with(this).load(ImageId).into(imageView);
        }else{
            Glide.with(this).load(ImageUrl).into(imageView);
        }

        //contentText.setText(title);
        webView.loadData(source, "text/html; charset=UTF-8", null);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case android.R.id.home:
               finish();
               return true;
       }
        return super.onOptionsItemSelected(item);
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    DeleteAdv(source);
                    initview();
                    break;
                default:
                    break;
            }
        }
    };
    public void getSource(){
        HttpUtil.sendOkHttpRequest(url,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                source = response.body().string();
                Message message = new Message();
                message.what=1;
                handler.sendMessage(message);
            }
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TAG!!!", "onFailure ");
            }
        });
    }
    public void DeleteAdv(String source){
       // Log.d("html", source);
        Pattern p = Pattern.compile("<script.*></script>");
        Matcher m = p.matcher(source);
        source = m.replaceAll("");
        //Log.d("html", source);
    }
}
