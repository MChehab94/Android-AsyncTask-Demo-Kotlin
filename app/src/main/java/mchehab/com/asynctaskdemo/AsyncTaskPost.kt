package mchehab.com.asynctaskdemo

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v4.content.LocalBroadcastManager
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by muhammadchehab on 11/6/17.
 */
class AsyncTaskPost(val context: WeakReference<Context>, val postData: String, val
broadcastIntent: String): AsyncTask<String, Int, String>() {

    override fun doInBackground(vararg params: String?): String {
        try{
            val url = URL(params[0])
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "POST"
            httpURLConnection.connect()

            val bufferedWriter = BufferedWriter(OutputStreamWriter(httpURLConnection.outputStream))
            bufferedWriter.write(postData)
            bufferedWriter.flush()
            bufferedWriter.close()

            val responseCode = httpURLConnection.responseCode
            if(responseCode == 200){
                val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))
                return bufferedReader.use { it.readText() }
            }
        }catch (exception: Exception){
            exception.printStackTrace()
        }
        return ""
    }

    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
        val intent = Intent(broadcastIntent)
        intent.putExtra("json", result)
        LocalBroadcastManager.getInstance(context.get()!!.applicationContext).sendBroadcast(intent)
    }
}