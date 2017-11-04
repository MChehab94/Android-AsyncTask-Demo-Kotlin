package mchehab.com.asynctaskdemo

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity

/**
 * Created by muhammadchehab on 11/4/17.
 */
abstract class BaseNetworkActivity : AppCompatActivity() {

    abstract fun noInternetConnection()
    abstract fun internetConnectionAvailable()

    private val broadcastReceiverConnectionChanged = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (!NetworkUtil.isNetworkAvailable(this@BaseNetworkActivity)) {
                noInternetConnection()
            } else {
                internetConnectionAvailable()
            }
        }
    }

    override fun onPause() {
//        remove broadcast receiver when activity stops
        unregisterReceiver(broadcastReceiverConnectionChanged)
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
//        register broadcast receiver after starting activity
        registerBroadcastReceiver()
    }

    private fun registerBroadcastReceiver() {
        val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(broadcastReceiverConnectionChanged, intentFilter)
    }

    protected fun hasInternetConnection(): Boolean{
        return NetworkUtil.isNetworkAvailable(this)
    }

    protected fun isWifiInternet(): Boolean{
        return NetworkUtil.isWifiNetwork(this)
    }

    protected fun isMobileData(): Boolean{
        return !(NetworkUtil.isWifiNetwork(this))
    }
}