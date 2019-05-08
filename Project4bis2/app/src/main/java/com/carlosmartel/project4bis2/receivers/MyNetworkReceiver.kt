package com.carlosmartel.project4bis2.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.carlosmartel.project4bis2.data.webServices.WebData


class MyNetworkReceiver: BroadcastReceiver(){
    private var listener: NetworkCallback? = null
    private var isConnected = false

    override fun onReceive(context: Context?, intent: Intent?) {
        val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        isConnected = activeNetwork?.isConnected == true
        WebData.connected = isConnected
        sendMessage()
        //Toast.makeText(context, if(isConnected) "CONNECTION PRESENT" else "NO INTERNET CONNECTION", Toast.LENGTH_SHORT).show()
    }

    private fun sendMessage() {
        if (isConnected) listener?.onConnectionGained() else listener?.onConnectionLost()
    }

    fun setNetworkCallbackListener(listener: NetworkCallback){
        this.listener = listener
    }
    interface NetworkCallback{
        fun onConnectionGained()
        fun onConnectionLost()
    }
}