package mchehab.com.asynctaskdemo

import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

class MainActivity : AppCompatActivity(), AsyncListener, AsyncImageListener {

    lateinit var textView: TextView
    lateinit var imageView: ImageView
    lateinit var progressBar: ProgressBar
    val URL = "http://validate.jsontest.com/?json=%7B%22key%22:%22value%22%7D"
    val IMAGE_URL = "https://www.w3schools.com/bootstrap/paris.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progressBar)

        val button = findViewById<Button>(R.id.button)
        val buttonImage = findViewById<Button>(R.id.buttonImage)

        button.setOnClickListener{ GetJSON(this).execute(URL) }
        buttonImage.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            AsyncImageDownload(this).execute(IMAGE_URL)
        }
    }

    override fun getResult(result: String?) {
        textView.text = result
    }

    override fun getBitmap(bitmap: Bitmap?) {
        progressBar.visibility = View.GONE
        imageView.setImageBitmap(bitmap)
    }
}