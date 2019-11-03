package com.example.nikhil.Shortnews;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView mNavigationView;
    ConnectionReceiver receiver = new ConnectionReceiver();

    private ViewPager mPager;
    private TabLayout mTabLayout;
    private SliderPagerAdapter mPagerAdapter;
    private ArrayList<String> category = new ArrayList<String>();
    private  ArrayList<String> newsLinks=new ArrayList<String>();
    private SharedPreferences sharedPreferences;
    private boolean hyderabad,health,tollywood,business,education,local;
    private Fragment fm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            settingUpUiViews();


      //reading data from shared preferences
        loadDataFromsharedpref();


            //adding Basic categories
              addingDefaultCategories();

        //setting up the pagerAdapter
        mPagerAdapter = new SliderPagerAdapter(getSupportFragmentManager(), category,newsLinks);
        mPager.setAdapter(mPagerAdapter);
        mPager.setOffscreenPageLimit(5);
        mTabLayout.setupWithViewPager(mPager);


    }
    private void settingUpUiViews(){
        //linking all views with java code

        mDrawerLayout = (DrawerLayout) findViewById(R.id.mainDrawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.mainNavView);
        mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
        mNavigationView.bringToFront();
        mNavigationView.setNavigationItemSelectedListener(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Short News");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();


        mPager = (ViewPager) findViewById(R.id.mainViewPager);
        mTabLayout = (TabLayout) findViewById(R.id.mainTablayout);
    }

    public void loadDataFromsharedpref(){
          //assigning values to sharedpreference
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);

        hyderabad=sharedPreferences.getBoolean(getResources().getString(R.string.hyderabad_key),false);
        health=sharedPreferences.getBoolean(getResources().getString(R.string.health_key),false);
        tollywood=sharedPreferences.getBoolean(getResources().getString(R.string.tollywood_key),false);
        education=sharedPreferences.getBoolean(getResources().getString(R.string.education_key),false);
        business=sharedPreferences.getBoolean(getResources().getString(R.string.business_key),false);
        local=sharedPreferences.getBoolean(getResources().getString(R.string.local_news_key),false);
         //registering Sharedpref Listener
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

    }

    public void addingDefaultCategories(){

           category.clear();
           newsLinks.clear();
        //adding categories to arrayList wish is used for no.of tabs
        category.add("HeadLines");
        newsLinks.add("https://newsapi.org/v2/top-headlines?country=in&pageSize=100&"+getResources().getString(R.string.apiKey));

        category.add("Entertainment");
        newsLinks.add("https://newsapi.org/v2/top-headlines?country=in&category=entertainment&pageSize=100&"+getResources().getString(R.string.apiKey));

        category.add("Sports");
        newsLinks.add("https://newsapi.org/v2/top-headlines?country=in&category=sports&pageSize=100&"+getResources().getString(R.string.apiKey));


        category.add("Science");
        newsLinks.add("https://newsapi.org/v2/top-headlines?country=in&category=science&pageSize=100&"+getResources().getString(R.string.apiKey));

        category.add("Technology");
        newsLinks.add("https://newsapi.org/v2/top-headlines?country=in&category=technology&pageSize=100&"+getResources().getString(R.string.apiKey));

        category.add("Telangana");
        newsLinks.add("https://newsapi.org/v2/everything?q=telangana&pageSize=100&"+getResources().getString(R.string.apiKey));


        category.add("Political");
        newsLinks.add("https://newsapi.org/v2/top-headlines?country=in&category=politics&pageSize=100&"+getResources().getString(R.string.apiKey));

        customCategories();

    }

    public void customCategories(){

        if(hyderabad){
                        if(category.contains("Hyderabad")){
                        }else{
                          category.add("Hyderabad");
                          newsLinks.add( "https://newsapi.org/v2/everything?q=hyderabad&pageSize=100&apiKey=a3a7c65515f0426da9c5e5091c91ad27");
                        }

                    }

         if(business){
                        if(category.contains("Business")){
                              }else{ category.add("Business");
                        newsLinks.add("https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=a3a7c65515f0426da9c5e5091c91ad27");
                        }
                        }

         if(health){
            if(category.contains("Health")){
            }else{ category.add("Health");
            newsLinks.add("https://newsapi.org/v2/top-headlines?country=in&category=health&pageSize=100&apiKey=a3a7c65515f0426da9c5e5091c91ad27");
            }}

         if(education){
            if(category.contains("Education")){
            }else{ category.add("Education");
                    newsLinks.add( "https://newsapi.org/v2/everything?q=education&pageSize=100&apiKey=a3a7c65515f0426da9c5e5091c91ad27");
            }}

        if(tollywood){
            if(category.contains("Tollywood")){
            }else{ category.add("Tollywood");
                   newsLinks.add("https://newsapi.org/v2/everything?q=tollywood&apiKey=a3a7c65515f0426da9c5e5091c91ad27");
            }}



        if(local){
            if(category.contains("Local News")){
            }else{ category.add("Local News");

           // newsLinks.add("https://newsapi.org/v2/everything?q=telugu&pageSize=100&apiKey=a3a7c65515f0426da9c5e5091c91ad27");

           newsLinks.add("https://www.apherald.com/rss/tel");

            }}

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        mPagerAdapter.notifyDataSetChanged();


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        switch (item.getItemId()){
            case R.id.settings:{
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                mDrawerLayout.closeDrawer(Gravity.START);
                break;
            }
            case R.id.categories:{
                startActivity(new Intent(MainActivity.this,CategoriesActivity.class));
                mDrawerLayout.closeDrawer(Gravity.START);
                break;
            }
            case R.id.feedbackoption: {
                sendFeedback();
                mDrawerLayout.closeDrawer(Gravity.START);
                break;
            }
            case R.id.share:{
                //sharing app link via other apps
                shareApp();
                mDrawerLayout.closeDrawer(Gravity.START);
                 break;

            }

            default: break;

        }
                return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals(getResources().getString(R.string.hyderabad_key))){
            hyderabad=sharedPreferences.getBoolean(getResources().getString(R.string.hyderabad_key),false);
            if(hyderabad){
                mPagerAdapter.addingFragment();
            }else{
                if(category.contains("Hyderabad")){
                    category.remove("Hyderabad");
                    newsLinks.remove( "https://newsapi.org/v2/everything?q=hyderabad&pageSize=100&apiKey=a3a7c65515f0426da9c5e5091c91ad27");
                }}

        }
        if(key.equals(getResources().getString(R.string.tollywood_key))){
            tollywood=sharedPreferences.getBoolean(getResources().getString(R.string.tollywood_key),false);
            if(tollywood){ mPagerAdapter.addingFragment();

            }else {  if(category.contains("Tollywood")){
            category.remove("Tollywood");
                newsLinks.remove("https://newsapi.org/v2/everything?q=tollywood&apiKey=a3a7c65515f0426da9c5e5091c91ad27");
            }}}
        if(key.equals(getResources().getString(R.string.education_key))){
            education=sharedPreferences.getBoolean(getResources().getString(R.string.education_key),false);
            if(education){ mPagerAdapter.addingFragment();}
            else {
                if(category.contains("Education")){
                 category.remove("Education");
                    newsLinks.remove( "https://newsapi.org/v2/everything?q=education&pageSize=100&apiKey=a3a7c65515f0426da9c5e5091c91ad27");
                }
            }
        }
        if(key.equals(getResources().getString(R.string.business_key))){
            business=sharedPreferences.getBoolean(getResources().getString(R.string.business_key),false);
            if(business){mPagerAdapter.addingFragment();}
            else {
                if(category.contains("Business")){
                category.remove("Business");
                    newsLinks.remove("https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=a3a7c65515f0426da9c5e5091c91ad27");
                }
            }
        }
        if(key.equals(getResources().getString(R.string.local_news_key))){
            local=sharedPreferences.getBoolean(getResources().getString(R.string.local_news_key),false);
            if(local){ mPagerAdapter.addingFragment();}
            else{
                if(category.contains("Local News")){
                 category.remove("Local News");
                    newsLinks.remove("https://newsapi.org/v2/everything?q=telugu&pageSize=100&apiKey=a3a7c65515f0426da9c5e5091c91ad27");
                }
            }
        }
        if(key.equals(getResources().getString(R.string.health_key))){
            health=sharedPreferences.getBoolean(getResources().getString(R.string.health_key),false);
            if(health){mPagerAdapter.addingFragment();
            }else {
                if(category.contains("Health")){ category.remove("Health");
                    newsLinks.remove("https://newsapi.org/v2/top-headlines?country=in&category=health&pageSize=100&apiKey=a3a7c65515f0426da9c5e5091c91ad27");
                }
            }
        }


    }

    public class SliderPagerAdapter extends FragmentStatePagerAdapter {
        ArrayList<String> category = new ArrayList<String>();
        ArrayList<String> newsUrlLinks=new ArrayList<String>();

        public SliderPagerAdapter(FragmentManager fm, ArrayList<String> category,ArrayList<String> newsLinks) {
            super(fm);
            this.category = category;
            this.newsUrlLinks=newsLinks;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            NewsFragment fragment = new NewsFragment();
            bundle.putString("url",newsUrlLinks.get(position));
            fragment.setArguments(bundle);
            fm=fragment;
            return fragment;
        }

        @Override
        public int getCount() {
            return category.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return category.get(position);

        }


        @Override
        public int getItemPosition(@NonNull Object object) {



            return POSITION_NONE;
        }
        public void addingFragment(){
            customCategories();
            Toast.makeText(MainActivity.this, category.get(category.size()-1)+" is added to page", Toast.LENGTH_SHORT).show();
            mPagerAdapter.notifyDataSetChanged();


        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        //unRegistering Sharedpreference at activity destoyed
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
    private void shareApp(){

        String body="To get latest news from around world download Short News app at Free of cost \n"+
                "https://drive.google.com/folderview?id=1XNLmD49jVCpE7sq7InXkLEemnsuZMUmy";

        String mimeType="text/plain";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Share App with: ")
                .setText(body)
                .startChooser();
    }
    private void sendFeedback() {

        String mailto = "mailto:timetableapp214@gmail.com" +
                "?cc=" + "dayakargoudbandari@gmail.com" +
                "&subject=" + Uri.encode("Feedback about ShortNews") +
                "&body=" + Uri.encode("write your valuable feedback ");

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            e.getMessage();
            Toast.makeText(this, "No apps found to send email", Toast.LENGTH_SHORT).show();
        }

    }
}