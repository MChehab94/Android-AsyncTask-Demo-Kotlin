package mchehab.com.asynctaskdemo

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.support.v4.content.LocalBroadcastManager
import java.io.IOException
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

/**
 * Created by muhammadchehab on 11/1/17.
 */
class AsyncImageDownload(private val applicationContext: WeakReference<Context>, private val broadcastIntent: String) : AsyncTask<String, Int, Bitmap?>() {

    override fun doInBackground(vararg params: String): Bitmap? {

        try {
            val url = URL(params[0])
            val httpURLConnection = url.openConnection() as HttpURLConnection

            return BitmapFactory.decodeStream(httpURLConnection.inputStream)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    override fun onPostExecute(bitmap: Bitmap?) {
        var imagePath = ""
        bitmap.let {
            imagePath = Util.saveToInternalStorage("image", bitmap!!, applicationContext
                    .get()!!)
        }
        val intent = Intent(broadcastIntent)
        intent.putExtra("image", imagePath)
        LocalBroadcastManager.getInstance(applicationContext.get()).sendBroadcast(intent)
    }
}