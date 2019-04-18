package com.example.nikhil.Shortnews;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // A menu item view type.
    private static final int MENU_ITEM_VIEW_TYPE = 0;


    // The unified native ad view type.
    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

    private List<UnifiedNativeAd> mNativeAd=new ArrayList<>();

    private AdLoader adloader;

    public Context mContext;
  public ArrayList<Object> arrayList=new ArrayList<Object>();

    public MainRecyclerAdapter(Context mContext, ArrayList<Object> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.mNativeAd=mNativeAd;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


//        View view= LayoutInflater.from(mContext).inflate(R.layout.recyclerview_single_item,parent,false);
//
//        return new MyHolder(view);


        switch (viewType) {

            case UNIFIED_NATIVE_AD_VIEW_TYPE:
                View unifiedNativeLayoutView = LayoutInflater.from(mContext).inflate(R.layout.ad_unified, parent, false);


                return new Ad_ViewHolder(unifiedNativeLayoutView);
            case MENU_ITEM_VIEW_TYPE:
            default:

                View menuItemLayoutView =  LayoutInflater.from(mContext).inflate(R.layout.recyclerview_single_item,parent,false);
                return new MyHolder(menuItemLayoutView);
        }


    }




    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
//
//        NewsData newsData=arrayList.get(position);
//        holder.title.setText(newsData.getTitle());
//
//          String urlofImage=newsData.getUrlToImage();
//          if(urlofImage==null){
//                  try{
//                      Picasso.get().load(R.drawable.newsimage).placeholder(R.drawable.loadingcolor).fit().into(holder.newsImage);
//
//                  }catch (Exception e){
//                      Log.e("MainREcyclerHolder",e.getMessage());
//                  }
//
//          }
//      Picasso.get().load(newsData.getUrlToImage()).fit().into(holder.newsImage);
//

        int viewType = getItemViewType(position);
        switch (viewType) {
            case UNIFIED_NATIVE_AD_VIEW_TYPE:

               // UnifiedNativeAd nativeAd = (UnifiedNativeAd) mNativeAd.get(position);
//
//               populateNativeAdView(mNativeAd.get(position), ((Ad_ViewHolder) holder).getAdView());
//                System.out.println("adapter recived ads "+mNativeAd);
                final View v=((Ad_ViewHolder) holder).getAdView();


                AdLoader.Builder builder = new AdLoader.Builder(mContext, mContext.getString(R.string.ad_unit_id));
                      adloader=  builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                            @Override
                            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {

                                populateNativeAdView(unifiedNativeAd, ((Ad_ViewHolder) holder).getAdView());

                            }
                        }).build();
                adloader.loadAds(new AdRequest.Builder().build(),1);


                break;
            case MENU_ITEM_VIEW_TYPE:
                // fall through
            default:

                MyHolder menuItemHolder = (MyHolder) holder;
               // MenuItem menuItem = (MenuItem) arrayList.get(position);


                NewsData newsData= (NewsData) arrayList.get(position);
                menuItemHolder.title.setText(newsData.getTitle());

                if(!TextUtils.isEmpty(newsData.getUrlToImage())){
                    try{
                        Picasso.get().load(newsData.getUrlToImage()).placeholder(R.drawable.newsimage).fit().into(menuItemHolder.newsImage);

                    }catch (Exception e){
                        Log.e("MainREcyclerHolder",e.getMessage());
                        Picasso.get().load(R.drawable.newsimage).fit().into(menuItemHolder.newsImage);

                    }

                }


        }




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    /**
     * Determines the view type for the given position.
     */
    @Override
    public int getItemViewType(int position) {

//        Object recyclerViewItem=arrayList.get(position);
//        if(recyclerViewItem instanceof UnifiedNativeAd){
//
//
        //    return UNIFIED_NATIVE_AD_VIEW_TYPE;
        //}
        if(position%5==0&position!=0)
        {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        return MENU_ITEM_VIEW_TYPE;
    }


    public class MyHolder extends RecyclerView.ViewHolder{
          TextView title;
          ImageView newsImage;

        public MyHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.newsTitle);
            newsImage=(ImageView)itemView.findViewById(R.id.newsimage);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsData nd=(NewsData)arrayList.get(getAdapterPosition());
                    final Intent intent=new Intent(mContext,NewsDetailsActivity.class);
                    intent.putExtra("title",nd.getTitle());
                    intent.putExtra("image",nd.getUrlToImage());
                    intent.putExtra("publishedAt",nd.getPublishedAt());
                    intent.putExtra("desc",nd.getDescription());
                    intent.putExtra("content",nd.getContent());
                    intent.putExtra("url",nd.getUrl());

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) mContext,
                                    newsImage,
                                    ViewCompat.getTransitionName(newsImage));

                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
                    mContext.startActivity(intent,options.toBundle());}
                    else{
                        mContext.startActivity(intent);
                    }

                }
            });


        }
    }



    private void loadNativeAds() {

        AdLoader.Builder builder = new AdLoader.Builder(mContext, mContext.getString(R.string.ad_unit_id));
        adloader = builder.forUnifiedNativeAd(
                new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                    @Override
                    public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                        // A native ad loaded successfully, check if the ad loader has finished loading
                        // and if so, insert the ads into the list.
                        mNativeAd.add(unifiedNativeAd);
                        if (!adloader.isLoading()) {

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
        adloader.loadAds(new AdRequest.Builder().build(),5);
    }

    private void insertAdsInMenuItems(){
        if(mNativeAd.size()<=0){
            return;
        }



        for(UnifiedNativeAd ad:mNativeAd) {
            mNativeAd.add(ad);


        }




    }


    private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                      UnifiedNativeAdView adView) {
        // Some assets are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        NativeAd.Image icon = nativeAd.getIcon();

        if (icon == null) {
            adView.getIconView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            adView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAd);
    }
}
