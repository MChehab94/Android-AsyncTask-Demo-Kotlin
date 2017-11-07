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
import org.json.JSONException
import org.json.JSONObject

class MainActivity : BaseNetworkActivity(){

    lateinit var textView: TextView
    lateinit var imageView: ImageView
    lateinit var progressBar: ProgressBar

    var getJSON: GetJSON? = null
    var asyncImageDownload: AsyncImageDownload? = null

    val URL = "http://httpbin.org/get"
    val IMAGE_URL = "https://www.w3schools.com/html/workplace.jpg"
    val POST_URL = "https://httpbin.org/post"

    var isImageDownloading = false
    var isJSONDownloading = false
    var isJSONPosting = false

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
            isJSONPosting = false
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
        val buttonPost = findViewById<Button>(R.id.buttonPost)
        val buttonImage = findViewById<Button>(R.id.buttonImage)

        button.setOnClickListener{ executeGetJSON() }
        buttonPost.setOnClickListener { executePostJSON() }
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

    private fun executePostJSON(){
        isJSONPosting = true
        if (hasInternetConnection()) {
            try {
                val jsonObject = JSONObject()
                jsonObject.put("comments", "just deliver")
                jsonObject.put("custemail", "myemail")
                jsonObject.put("size", "medium")

                val jsonObjectForm = JSONObject()
                jsonObjectForm.put("form", jsonObject)

                getJSON = GetJSON(WeakReference<Context>(this), "json", "POST",
                        jsonObjectForm.toString())
                getJSON!!.execute(POST_URL)

            } catch (jsonException: JSONException) {
                jsonException.printStackTrace()
            }
        } else {
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
        if(isJSONPosting){
            executeImageDownload()
        }
    }

    private fun displayNoInternetDialog(){
        alertDialogNoInternet.show()
    }
}