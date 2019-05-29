package com.krooms.hostel.rental.property.app.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnection {
	public static boolean isConnected(Context ctx){

		ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

		if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){

			return false;
		}
		return true;
	}
}

	/*public boolean isOnline() {
		ConnectivityManager conMgr = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

		if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){

			return false;
		}
		return true;
	}*/

