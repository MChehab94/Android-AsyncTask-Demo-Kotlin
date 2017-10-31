package mchehab.com.asynctaskdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity(), AsyncListener {

    lateinit var textView: TextView
    val URL = "http://validate.jsontest.com/?json=%7B%22key%22:%22value%22%7D"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener({
            GetJSON(this).execute(URL)
        })
    }

    override fun getResult(result: String?) {
        textView.text = result
    }
}