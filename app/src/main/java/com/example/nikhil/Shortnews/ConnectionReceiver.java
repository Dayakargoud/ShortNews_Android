package com.example.nikhil.Shortnews;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class ConnectionReceiver extends BroadcastReceiver {


    public ConnectionReceiver() {
        super();
    }
    @Override
    public void onReceive(Context context, Intent intent) {

      try{
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {

            if (isConnected(context)) {



            } else {
//                Toast.makeText(context, " No Internet...", Toast.LENGTH_SHORT).show();



            }
        }}catch (Exception e){
          Toast.makeText(context, "Error while loading News..."+e.getMessage(), Toast.LENGTH_SHORT).show();
          Log.e("ConnectionReceiver",e.getMessage());

      }
    }


    public static boolean isConnected(Context context) {
        ConnectivityManager
                cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }


}
