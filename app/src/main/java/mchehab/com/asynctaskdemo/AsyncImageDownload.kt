package mchehab.com.asynctaskdemo

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by muhammadchehab on 11/1/17.
 */
class AsyncImageDownload(private val listener: AsyncImageListener) : AsyncTask<String, Int, Bitmap?>() {

    override fun doInBackground(vararg params: String?): Bitmap? {

        val url = URL(params[0])
        val httpURLConnection = url.openConnection() as HttpURLConnection

        return BitmapFactory.decodeStream(httpURLConnection.inputStream)
    }

    override fun onPostExecute(bitmap: Bitmap?) {
        listener.getBitmap(bitmap)
    }
}