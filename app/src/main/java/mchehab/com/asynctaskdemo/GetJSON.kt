package mchehab.com.asynctaskdemo

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v4.content.LocalBroadcastManager
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by muhammadchehab on 10/31/17.
 */
class GetJSON(val applicationContext: WeakReference<Context>, val broadcastIntent: String, val
httpMethod: String?="GET", val postData: String?="") :
        AsyncTask<String, Int, String>() {

    override fun doInBackground(vararg params: String?): String {
        try {
            val url = URL(params[0])
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = httpMethod
            httpURLConnection.connect()
            if(httpMethod.equals("POST")){
                httpURLConnection.outputStream.bufferedWriter().use { it.write(postData); it.flush() }
            }

            val result = httpURLConnection.inputStream.bufferedReader().readText()
            return result
        }catch (exception: Exception){
            exception.printStackTrace()
        }
        return ""
    }

    override fun onPostExecute(result: String?) {
        val intent = Intent(broadcastIntent)
        intent.putExtra("result", result)
        LocalBroadcastManager.getInstance(applicationContext.get()!!).sendBroadcast(intent)
    }
}