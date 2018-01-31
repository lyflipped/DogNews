package com.example.liyang.meterialdesigntest;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadFactory;

import Adapter.CardAdapter;
import DataShare.Data_Share;
import Entity.Data_Source;
import Entity.News;
import Entity.card_image;
import it.gmariotti.recyclerview.adapter.AlphaAnimatorAdapter;
import it.gmariotti.recyclerview.itemanimator.SlideInOutLeftItemAnimator;
import jp.wasabeef.glide.transformations.BlurTransformation;
import okhttp3.Call;
import okhttp3.Response;
import tools.DateUtil;
import tools.HttpUtil;

public class MainActivity extends AppCompatActivity {
    private static String ADDRESS="http://v.juhe.cn/toutiao/index?type=";
    private static String PARAMETERS="&key=b7fdf3137d678d201541cdaa735a8c8d";
    private static String[] TYPE={ "top","keji","shehui","guoji","yule","tiyu","junshi","keji","caijing","shishang"};
    private DrawerLayout mDrawerLayout;
    private ImageView bg_header;
    private ImageView icon_header;
    private card_image[] cards;
    private List<card_image> cardlist = new ArrayList<>();
    private CardAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private static String ShowMode = "Simple";
    private String Data="";
    private Data_Source data_source;
    private List<News> newslist;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCards();
        initImage();
        LayoutInflater inflater=(LayoutInflater)this.getSystemService(LAYOUT_INFLATER_SERVICE);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);
        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.main_collapse);
        ImageView jieri = (ImageView)findViewById(R.id.jieri_main);
        toolbarLayout.setTitle(DateUtil.getWeek());
        View header = navView.inflateHeaderView(R.layout.nav_header);
        bg_header = (ImageView) header.findViewById(R.id.bg_header);
        icon_header = (ImageView) header.findViewById(R.id.icon_image);
        //Glide.with(this).load(R.drawable.qlx2).apply(RequestOptions.bitmapTransform(new BlurTransformation(15))).into(jieri);
        Glide.with(this).load(R.drawable.ly_hashiqi).into(jieri);
        Glide.with(navView.getContext()).load(R.drawable.me).apply(RequestOptions.bitmapTransform(new BlurTransformation(25))).into(bg_header);
        Glide.with(navView.getContext()).load(R.drawable.me).into(icon_header);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setHomeAsUpIndicator(R.drawable.menu_16px);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem item){
                mDrawerLayout.closeDrawers();
                return true;
            }

        });

        //加载图片卡片
        initCardList();
        recyclerView = (RecyclerView) findViewById(R.id.recyle_view);

        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(),DividerItemDecoration.VERTICAL));
        adapter = new CardAdapter(cardlist);
//        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
//        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setAdapter(adapter);
        DoubleShow(recyclerView);

        //floatingactionbutton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//             Snackbar.make(view,"Data delete!",Snackbar.LENGTH_INDEFINITE)
//                        .setAction("Undo", new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(MainActivity.this,"Data restored",Toast.LENGTH_SHORT).show();
//                            }
//                        }).show();
               Toast.makeText(getApplicationContext(),"新功能，敬请期待！",Toast.LENGTH_SHORT).show();
            }
        });



        //处理下拉刷新
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {

                refreshCards();
            }
        });

        //请求数据
        GetNews();
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //Log.d("TAG", data_source.getReason());
                    //Log.d("Error_Code", data_source.getError_code());
                    //update title
                    for(int i=0;i<30;i++){
                        cards[i].setName(newslist.get(i).getTitle());
                        cards[i].setInfo_other(newslist.get(i).getDate()+"\n"
                                +newslist.get(i).getAuthou_name());
                        cards[i].setCategory(newslist.get(i).getCategory());
                        cards[i].setImage_url(newslist.get(i).getThumbnail_pic_s());
                        cards[i].setUrl(newslist.get(i).getUrl());
                    }
                    initCardList();
                    adapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    public void GetNews(){
        Random random = new Random();
        int num = random.nextInt(TYPE.length);
        HttpUtil.sendOkHttpRequest(ADDRESS+TYPE[num]+PARAMETERS,new okhttp3.Callback(){
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Data = response.body().string();
                Log.d("TAG",Data);
                update(Data);
                //getTX_News(Data);
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
    public  void initCards(){
        cards = new card_image[30];
        for(int i=0;i<30;i++){
            card_image tmp = new card_image("default",R.drawable.cat);
            cards[i]=tmp;
        }
    }
    public void update(String data){
        newslist = new ArrayList<News>();
       try{
           JSONObject json_object = new JSONObject(data);
           String ss  = json_object.getString("result").toString();
           JSONObject news = new JSONObject(ss);
           String source = news.getString("data").toString();
           JSONArray jsonArray = new JSONArray(source);
           Log.d("TAG","length="+jsonArray.length());
           for(int i=0;i<jsonArray.length();i++){
               JSONObject object = jsonArray.getJSONObject(i);
               News onenew = new News();
               onenew.setUniquekey(object.getString("uniquekey"));
               onenew.setAuthou_name(object.getString("author_name"));
               onenew.setTitle(object.getString("title"));
               onenew.setDate(object.getString("date"));
               onenew.setCategory(object.getString("category"));
               Log.d("TAG","title is "+onenew.getTitle());
               onenew.setUrl(object.getString("url"));
               onenew.setThumbnail_pic_s(object.getString("thumbnail_pic_s"));
              newslist.add(onenew);
           }
           for(News onenew:newslist){
               Log.d("TAG","title is "+onenew.getTitle());
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }
    public void getTX_News(String data){
        newslist = new ArrayList<News>();
        try{
            JSONObject object = new JSONObject(data);
            String txdata = object.getString("data").toString();
            JSONArray jsonArray = new JSONArray(txdata);
            for(int i=0;i<jsonArray.length();i++){
                JSONObject tmp = jsonArray.getJSONObject(i);
                News onenew = new News();
                onenew.setTitle(tmp.getString("title"));
                onenew.setDate(tmp.getString("time"));
                onenew.setCategory(tmp.getString("column"));
                onenew.setUrl(tmp.getString("url"));
                newslist.add(onenew);
            }
            for(News onenew:newslist){
                Log.d("TAG","title is "+onenew.getTitle());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void SimpleShow(RecyclerView recyclerView){
        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);
        AlphaAnimatorAdapter alphaAnimatorAdapter = new AlphaAnimatorAdapter(adapter,recyclerView);
        recyclerView.setAdapter(alphaAnimatorAdapter);
       // recyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(recyclerView));
    }
    public void DoubleShow(RecyclerView recyclerView){
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager1);
      //recyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(recyclerView));
        AlphaAnimatorAdapter alphaAnimatorAdapter = new AlphaAnimatorAdapter(adapter,recyclerView);
        recyclerView.setAdapter(alphaAnimatorAdapter);

    }
    public void initImage(){

        //old_image = (ImageView) findViewById(R.id.old_image);
        //new_image = (ImageView) findViewById(R.id.new_image);
        //Glide.with(this).load(R.drawable.cat).into(old_image);
        //Glide.with(this).load(R.drawable.cat).apply(RequestOptions.bitmapTransform(new BlurTransformation(25))).into(new_image);
    }
    public void initCardList(){

        cardlist.clear();
        for(int i=0;i<30;i++){
           // Random random = new Random();
            //int index = random.nextInt(cards.length);
            cardlist.add(cards[i]);
        }
    }
    public void notic(){
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("My First Notice!")
                .setContentText("忽如一夜春风来，千树万树梨花开")
                .setSmallIcon(R.drawable.garbage_24px)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.email_48px))
                .setVibrate(new long[]{0,1000,1000,1000})
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),R.drawable.tianye)))
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();
        manager.notify(1,notification);

    }
    private static void startAlarm(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        if (notification == null) return;
        Ringtone r = RingtoneManager.getRingtone(context, notification);
        r.play();
    }
    public void refreshCards(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(2000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startAlarm(getApplicationContext());
                        GetNews();
                        update(Data);
                        initCardList();
                        adapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                        //notic();
                    }
                });
            }
        }).start();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                Toast.makeText(this,"back up",Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete:
                Toast.makeText(this,"delete",Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Toast.makeText(this,"settings",Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }
}
