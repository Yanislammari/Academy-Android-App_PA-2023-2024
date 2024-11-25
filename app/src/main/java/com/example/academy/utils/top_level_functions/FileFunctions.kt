package com.example.academy.utils.top_level_functions

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.OpenableColumns
import java.io.File
import java.io.FileOutputStream

fun drawableToFile(context: Context, drawable: Drawable, fileName: String?): File? {
    val bitmap = (drawable as BitmapDrawable).bitmap
    val file = fileName?.let { File(context.cacheDir, it) }
    return try{
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()
        file
    }
    catch(e: Exception){
        e.printStackTrace()
        null
    }
}

fun getFileName(uri: Uri, contentResolver: ContentResolver): String? {
    val cursor = contentResolver.query(uri, null, null, null, null)
    cursor?.use {
        if (it.moveToFirst()) {
            val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (displayNameIndex >= 0) {
                return it.getString(displayNameIndex)
            }
        }
    }
    return null
}
