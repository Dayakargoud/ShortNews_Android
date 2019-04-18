package com.example.nikhil.Shortnews;
import android.content.Intent;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class NewsDetailsActivity extends AppCompatActivity {
   private ImageView newsImage;
   private TextView title,desc,content,publishedAt;
   private Toolbar mToolbar;
   private WebView webView;
   private LottieAnimationView webProgress;
    private String titleValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        mToolbar=(Toolbar)findViewById(R.id.newsDetailsToolbar);
        newsImage=(ImageView)findViewById(R.id.newsDetailsImage);
        title=(TextView)findViewById(R.id.newsdetailsTitle);
        desc=(TextView)findViewById(R.id.newsdescDetails);
        content=(TextView)findViewById(R.id.newsContentDetails);
        publishedAt=(TextView)findViewById(R.id.newsPublishedAtDetails);
        webProgress=(LottieAnimationView) findViewById(R.id.animation_networkweb);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        String imagevalue = intent.getStringExtra("image");
        titleValue=intent.getStringExtra("title");
        String descValue=intent.getStringExtra("desc");
        String contentValue=intent.getStringExtra("content");
        String publishedAtValue=intent.getStringExtra("publishedAt");
        String urls=intent.getStringExtra("url");

        Picasso.get().load(imagevalue).fit().centerCrop().into(newsImage);

        title.setText(titleValue);
        publishedAt.setText(timeStomp(publishedAtValue));
        desc.setText(descValue);

        content.setText(contentValue);

        initializeWebView(urls);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.news_details_toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
            case  R.id.shareNews: {
                String shareTitle=titleValue;
                shareApp(shareTitle);
                break;
            }
        }
        return super.onOptionsItemSelected(item);


    }
    private CharSequence timeStomp(String time){

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        // 2019-01-07T11:17:51Z
        sdf.setTimeZone(TimeZone.getDefault());
        try{
            long receivedtime=sdf.parse(time).getTime();
            long now=System.currentTimeMillis();
            CharSequence ago= DateUtils.getRelativeTimeSpanString(receivedtime,now,DateUtils.MINUTE_IN_MILLIS);
            return ago;
        }catch (Exception e){
            e.getMessage();
            return time;

        }
    }
    private void initializeWebView(String url){
        webView=(WebView) findViewById(R.id.newsurl);
         webView.getSettings().setLoadsImagesAutomatically(true);
         webView.getSettings().setJavaScriptEnabled(true);
         webView.getSettings().setDomStorageEnabled(true);
         webView.getSettings().setSupportZoom(true);
         webView.getSettings().setDisplayZoomControls(false);
         webView.getSettings().setBuiltInZoomControls(true);
         webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
         webView.setWebViewClient(new AppWebViewClients());
         webView.loadUrl(url);

    }

    public class AppWebViewClients extends WebViewClient {
        private ProgressBar progressBar;


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            webProgress.setVisibility(View.GONE);
        }
    }
    private void shareApp(String newsTitle){

        String body=newsTitle+"\n"+
                "To read Full Article download Short News app at Free of cost \n"+
                "https://drive.google.com/folderview?id=1XNLmD49jVCpE7sq7InXkLEemnsuZMUmy";

        String mimeType="text/plain";
        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Share App with: ")
                .setText(body)
                .startChooser();
    }

}
