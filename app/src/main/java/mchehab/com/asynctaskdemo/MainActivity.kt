package mchehab.com.asynctaskdemo

import android.app.AlertDialog
import android.content.*
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import java.lang.ref.WeakReference

class MainActivity : BaseNetworkActivity(){

    lateinit var textView: TextView
    lateinit var imageView: ImageView
    lateinit var progressBar: ProgressBar

    var getJSON: GetJSON? = null
    var asyncImageDownload: AsyncImageDownload? = null

    val URL = "http://validate.jsontest.com/?json=%7B%22key%22:%22value%22%7D"
    val IMAGE_URL = "https://www.w3schools.com/html/workplace.jpg"

    var isImageDownloading = false
    var isJSONDownloading = false

    lateinit var alertDialogNoInternet: AlertDialog

    val broadcastReceiverImage = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?){
            val bundle = intent?.extras
            val imagePath = bundle?.getString("image")

            val bitmap = BitmapFactory.decodeFile(imagePath)
            imageView.setImageBitmap(bitmap)

            isImageDownloading = false
        }
    }

    val broadcastReceiverJSON = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val bundle = intent?.extras
            val jsonResult = bundle?.getString("result")
            textView.text = jsonResult

            isJSONDownloading = false
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        if(imageView.drawable != null){
            val bitmapDrawable = imageView.drawable as BitmapDrawable
            val path = Util.saveToInternalStorage("image", bitmapDrawable.bitmap, applicationContext)
            outState?.putString("image", path)
        }
        outState?.putString("text", textView.text.toString())
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverJSON,
                IntentFilter("json"))
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiverImage,
                IntentFilter("image"))
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverJSON)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiverImage)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        alertDialogNoInternet = AlertDialog.Builder(this)
                .setTitle("No Internet Connection")
                .setMessage("Please make sure you have a valid internet connection")
                .setPositiveButton("Ok", null)
                .create()

        textView = findViewById(R.id.textView)
        imageView = findViewById(R.id.imageView)
        progressBar = findViewById(R.id.progressBar)

        if(savedInstanceState != null){
            textView.text = savedInstanceState.getString("text")

            val path = savedInstanceState.getString("image")
            val bitmap = BitmapFactory.decodeFile(path)
            imageView.setImageBitmap(bitmap)
        }

        val button = findViewById<Button>(R.id.button)
        val buttonImage = findViewById<Button>(R.id.buttonImage)

        button.setOnClickListener{ executeGetJSON() }
        buttonImage.setOnClickListener { executeImageDownload() }
    }

    private fun executeGetJSON(){
        isJSONDownloading = true
        if(hasInternetConnection()){
            getJSON = GetJSON(WeakReference(this), "json")
            getJSON!!.execute(URL)
        }else{
            displayNoInternetDialog()
        }
    }

    private fun executeImageDownload(){
        isImageDownloading = true
        if(hasInternetConnection()){
            imageView.setImageDrawable(null)
            progressBar.visibility = View.VISIBLE
            asyncImageDownload = AsyncImageDownload(WeakReference(this), "image")
            asyncImageDownload!!.execute(IMAGE_URL)
        }else{
            displayNoInternetDialog()
        }
    }

    override fun noInternetConnection() {
        displayNoInternetDialog()
    }

    override fun internetConnectionAvailable() {
        if(alertDialogNoInternet.isShowing){
            alertDialogNoInternet.dismiss()
        }
        if(isImageDownloading){
            executeImageDownload()
        }
        if(isJSONDownloading){
            executeGetJSON()
        }
    }

    private fun displayNoInternetDialog(){
        alertDialogNoInternet.show()
    }
}