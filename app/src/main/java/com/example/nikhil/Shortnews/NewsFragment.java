package com.example.nikhil.Shortnews;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {
    private static  int NEWS_URL_ID=1;
    private RecyclerView mRecyclerView;
    private ArrayList<Object> arrayList=new ArrayList<>();
    MainRecyclerAdapter adapter;
    public static  String NEWS_URL=null;
    private String LOG_TAG="NewsFragment.class";
    private  LottieAnimationView networkanimation;
    private  static TextView networkStatus;
    private  ShimmerFrameLayout shimmer;
    public Button retryButton;
    private int NUMBER_OF_ADS=5;
    private SwipeRefreshLayout refreshLayout;
    private AdLoader adloader;
    private ArrayList<Object> customData=new ArrayList<>();
    private  String ARRAYKEY="myarray";

    private List<UnifiedNativeAd> mNativeAd=new ArrayList<>();
    private List<UnifiedNativeAd> mNativeAds=new ArrayList<>();


    ArrayList<Object> items=new ArrayList<>();

    public NewsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Initialize the Google Mobile Ads SDK
        MobileAds.initialize(getContext(),
                getString(R.string.admob_app_id));


        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_news, container, false);
        //Getting URL From MainActivity
        NEWS_URL=this.getArguments().getString("url");

        //Initializing UI Views
        settingUpUIviews(view);

        //checking ConnectionStatus
        checkingConnectionStatus();


      //loadNativeAds();

        //loading News in background
         loadingBackgroundData();



        return view;
    }


    private void settingUpUIviews(View v){

        networkStatus=(TextView)v.findViewById(R.id.networkstatus);
        networkanimation=(LottieAnimationView)v.findViewById(R.id.animation_network);
        //Shimmer animation initalization
        shimmer =(ShimmerFrameLayout)v.findViewById(R.id.shimmer_view_container);
        retryButton=(Button)v.findViewById(R.id.retry_button);
        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingBackgroundData();
            }
        });

        //RecyclerView and Adapter setUp....
        mRecyclerView=(RecyclerView)v.findViewById(R.id.mainRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //refresh layout
        refreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setColorSchemeResources(R.color.colorAccent);
          refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
              @Override
              public void onRefresh() {
                  new Handler().postDelayed(new Runnable() {

                      @Override public void run() {
                            loadingBackgroundData();
                          refreshLayout.setRefreshing(false);

                      }

                  }, 4000); // Delay in millis

              }

          });

    }

    private void settingUp_Adapter(){

        adapter=new MainRecyclerAdapter(getContext(),arrayList);
        mRecyclerView.setAdapter(adapter);
    }

    private void insertAdsInMenuItems(){
        if(mNativeAd.size()<=0){
            return;
        }


        int offset=(arrayList.size()/mNativeAd.size())+1;
        int index=0;


        for(UnifiedNativeAd ad:mNativeAd) {
            mNativeAds.add(ad);
            index = index++;

        }
            adapter.notifyDataSetChanged();



    }





    private void checkingConnectionStatus(){

        ConnectivityManager
                cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();


        //checking connection Status if connected or not...
        if(activeNetwork != null &&  activeNetwork.isConnectedOrConnecting()) {
            networkanimation.cancelAnimation();
            retryButton.setVisibility(View.INVISIBLE);
            networkanimation.setVisibility(View.INVISIBLE);
            shimmer.startShimmer();
        }else {
            shimmer.setVisibility(View.INVISIBLE);
            networkStatus.setText("No internet Connection...");
            retryButton.setVisibility(View.VISIBLE);



        }
    }


    public void loadingBackgroundData(){
       //Loading data in background thread via AsyncTask...
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB){

            //this is for multiple asynckTask execution at a time...
            new BackgroundTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,NEWS_URL);

        }
        else {
            new BackgroundTask().execute(NEWS_URL);

        }


    }

    private void loadNativeAds() {

        AdLoader.Builder builder = new AdLoader.Builder(getContext(), getString(R.string.ad_unit_id));
        adloader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        mNativeAd.add(unifiedNativeAd);
                        if (!adloader.isLoading()) {
                            System.out.println("total ads "+mNativeAd);

                           insertAdsInMenuItems();
                            //loading News in background
                          // loadingBackgroundData();
                        }
                    }
                }).withAdListener(
                new AdListener() {
                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        // A native ad failed to load, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        Log.e("MainActivity", "The previous native ad failed to load. Attempting to"
                                + " load another.");
                        if (!adloader.isLoading()) {
                         //  insertAdsInMenuItems();

                            //loading News in background
                           // loadingBackgroundData();
                        }
                    }
                }).build();

        // Load the Native ads.
        adloader.loadAds(new AdRequest.Builder().build(), NUMBER_OF_ADS);
    }




    public class BackgroundTask extends AsyncTask<String,Void,String> {

        String xmltitle,desc,imageUrl,publishedtime,linkpage;
        String jsonResponse="";

        @Override
        protected String  doInBackground(String... urls) {
            String urldata=urls[0];
                    if(urldata.contains("/rss")){
                        try{
                            xmlParsing(urldata);
          //            ArrayList<> allNews=new ArrayList<Object>();              }catch (IOException e){

                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    else{

                  URL url=createURL(urls[0]);
            try{

                jsonResponse=makeHttpConnection(url);

                try{
                    JSONObject root=new JSONObject(jsonResponse);
                    JSONArray jsonArray=root.getJSONArray("articles");
                    for(int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject=jsonArray.getJSONObject(i);

                        String title=jsonObject.getString("title");
                        String content=jsonObject.getString("content");
                        String imageurl=jsonObject.getString("urlToImage");
                        String contenturl=jsonObject.getString("url");
                        String description=jsonObject.getString("description");
                        String publishedAt=jsonObject.getString("publishedAt");

                        NewsData data=new NewsData(title,imageurl,content,contenturl,description,publishedAt);
                        arrayList.add(data);

                    }

                }catch (JSONException e){
                    Log.e(LOG_TAG,"eerror while parsing json object");
                    return null;

                }

            }catch (IOException e){
                e.printStackTrace();
                return null;

            }

                   }


            return jsonResponse;
        }


        @Override
        protected void onPostExecute(String s) {

           settingUp_Adapter();
           //arrayList=s;

             if(adapter.getItemCount() !=0){
                 retryButton.setVisibility(View.INVISIBLE);
                 mRecyclerView.setVisibility(View.VISIBLE);
                 networkStatus.setVisibility(View.INVISIBLE);
                 networkanimation.cancelAnimation();
                 networkanimation.setVisibility(View.INVISIBLE);
                 shimmer.stopShimmer();
                 shimmer.setVisibility(View.INVISIBLE);

             }


            adapter.notifyDataSetChanged();

        }

        private String makeHttpConnection(URL url)throws IOException {



            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try{

                urlConnection=(HttpURLConnection)url.openConnection();
                urlConnection.setRequestMethod("GET");

                System.out.println("Response code is "+urlConnection.getResponseCode());
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();

                inputStream=urlConnection.getInputStream();
                jsonResponse=readFromInputStream(inputStream);



            }catch (NullPointerException e){
                Log.e(LOG_TAG,e.getMessage());

            }
            catch (IOException e){
                e.printStackTrace();

            }finally {
                if(urlConnection!=null){
                    urlConnection.disconnect();
                }
                if(inputStream !=null){
                    inputStream.close();
                }
            }

            return jsonResponse;
        }

        private String readFromInputStream(InputStream inputStream)throws IOException{
            StringBuilder output=new StringBuilder();
            if(inputStream !=null){

                InputStreamReader inputStreamReader=new InputStreamReader(inputStream, Charset.forName("UTF-8"));

                BufferedReader reader=new BufferedReader(inputStreamReader);
                String line=reader.readLine();
                while (line !=null){

                    output.append(line);
                    line=reader.readLine();
                }


            }

            return output.toString();
        }

        private URL createURL(String stringURL){
            URL url=null;
          if(TextUtils.isEmpty(stringURL)){

              Log.e(LOG_TAG,"Url is empty..");
              return null;

            }
            try{
                url=new URL(stringURL);


            }catch (MalformedURLException e){
                return null;

            }
            return url;
        }


        private String xmlParsing(String urltext) throws XmlPullParserException, IOException {

            ArrayList<Object> xmldata=new ArrayList<Object>();
            String url=new String(urltext);

            try{
            XmlPullParserFactory factory=XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp=factory.newPullParser();
            xpp.setInput(downloadUrl(url),null);
            boolean inside=false;
            int eventType=xpp.getEventType();

            while(eventType!=XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    if (xpp.getName().equalsIgnoreCase("item")) {

                        inside = true;


                    } else if (xpp.getName().equalsIgnoreCase("title")) {
                        if (inside) {

                             xmltitle=xpp.nextText();
                        }
                    } else if (xpp.getName().equalsIgnoreCase("link")) {

                        linkpage=xpp.nextText();


                    }

                    else if (xpp.getName().equalsIgnoreCase("summarytag")) {
                        if (inside) {
                            String text = Html.fromHtml(xpp.nextText()).toString();
                            String originaldesc = removeHtml(text);
                              desc=originaldesc;


                        }

                    } else if (xpp.getName().equalsIgnoreCase("image")) {
                        //   String value=xpp.getAttributeValue(null, "url");
                        //       image.add(value);
                         imageUrl=xpp.nextText();


                    } else if (xpp.getName().equalsIgnoreCase("pubDate")) {
                        //   String value=xpp.getAttributeValue(null, "url");
                        //       image.add(value);
                        publishedtime=xpp.nextText();


                    }


                } else if (eventType == XmlPullParser.END_TAG && xpp.getName().equalsIgnoreCase("item")) {

                    NewsData newsData=new NewsData(xmltitle,imageUrl,null,linkpage,desc,publishedtime);
                    xmldata.add(newsData);
                    arrayList=xmldata;
                    inside = false;
                }
//        NewsData data=new NewsData(title,imageurl,content,contenturl,description,publishedAt);

                /* adding data to list */


                eventType = xpp.next();

            }



            }catch (MalformedURLException e){
            e.printStackTrace();
            return null;

        }
            catch (
        XmlPullParserException e){
            e.printStackTrace();
            return null;

        }catch (IOException e){
            e.printStackTrace();
            return null;

        }
        return null;
    }
        private InputStream downloadUrl(String urlString) throws IOException {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            return conn.getInputStream();
        }
        private String removeHtml(String html) {
            html = html.replaceAll("<(.*?)\\>","");
            html = html.replaceAll("<(.*?)\\\n","");
            html = html.replaceFirst("(.*?)\\>", "");
            html = html.replaceAll("&nbsp;","");
            html = html.replaceAll("&amp;","");
            return html;
        }

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putParcelableArrayList("key",new ArrayList<Object>(arrayList));

      //  getSupportFragmentManager().putFragment(outState, "myFragmentName", mContent);

    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);




    }

}
