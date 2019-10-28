package com.dish.seekdish.util

import android.content.Context
import android.net.NetworkInfo

/**
 * Created by SFS on 5/14/16.
 */
class ConnectivityManager(private val _context: Context) {

    /**
     * Checking for all possible internet providers
     */
    /*    android.net.ConnectivityManager connectivity = (android.net.ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null)
        {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }

        }
        return false;*/ val isConnectingToInternet: Boolean
        get() {


            val connectivity =
                _context.getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager

            if (connectivity != null) {

                val activeNetwork = connectivity.activeNetworkInfo

                if (activeNetwork != null && activeNetwork.isConnected) {

                    return true
                }
            }
            return false
        }


}
