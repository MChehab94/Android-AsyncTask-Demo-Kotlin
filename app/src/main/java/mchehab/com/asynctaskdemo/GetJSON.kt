package mchehab.com.asynctaskdemo

import android.os.AsyncTask
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by muhammadchehab on 10/31/17.
 */
class GetJSON(private val asyncListener: AsyncListener) : AsyncTask<String, Int, String>() {

    override fun doInBackground(vararg params: String?): String {
        try {
            val url = URL(params[0])
            val httpURLConnection = url.openConnection() as HttpURLConnection
            httpURLConnection.requestMethod = "GET"
            httpURLConnection.connect()

            val result = httpURLConnection.inputStream.bufferedReader().readText()
            return result
        }catch (exception: Exception){
            exception.printStackTrace()
        }
        return ""
    }

    override fun onPostExecute(result: String?) {
        asyncListener.getResult(result)
    }
}