package mchehab.com.asynctaskdemo

import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var textView: TextView
    val URL = "http://validate.jsontest.com/?json=%7B%22key%22:%22value%22%7D"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        GetJSON().execute(URL)
    }

    inner class GetJSON: AsyncTask<String, Int, String>() {

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
            textView.text = result
        }
    }
}