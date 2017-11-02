package mchehab.com.asynctaskdemo

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap

import java.io.File
import java.io.FileOutputStream

/**
 * Created by muhammadchehab on 11/2/17.
 */

object Util {

    fun saveToInternalStorage(fileName: String, bitmapImage: Bitmap, applicationContext: Context): String {
        val cw = ContextWrapper(applicationContext)
        // path to /data/data/yourapp/app_data/imageDir
        val directory = cw.getDir("imageDir", Context.MODE_PRIVATE)
        // Create imageDir
        val mypath = File(directory, fileName)

        var fos: FileOutputStream? = null
        fos = FileOutputStream(mypath)
        // Use the compress method on the BitMap object to write image to the OutputStream
        bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos)
        fos.close()

        return mypath.absolutePath
    }
}