package com.ssafy.likloud.util

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

fun createMultipartFromUri(context: Context, uri: Uri): MultipartBody.Part? {
    val file: File? = getFileFromUri(context, uri)
    if (file == null) {
        // 파일을 가져오지 못한 경우 처리할 로직을 작성하세요.
        return null
    }

    val requestFile: RequestBody = createRequestBodyFromFile(file)
    return MultipartBody.Part.createFormData("multipartFiles", file.name, requestFile)
}

fun getFileFromUri(context: Context, uri: Uri): File? {
    val filePath = uriToFilePath(context, uri)
    return if (filePath != null) File(filePath) else null
}


@SuppressLint("Range")
fun uriToFilePath(context: Context, uri: Uri): String? {
    lateinit var filePath: String
    context.contentResolver.query(uri, null, null, null, null).use { cursor ->
        cursor?.let {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                val file = File(context.cacheDir, displayName)
                try {
                    val inputStream = context.contentResolver.openInputStream(uri)
                    val outputStream = FileOutputStream(file)
                    inputStream?.copyTo(outputStream)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                filePath = file.absolutePath
            }
        }
    }
    return filePath
}


private fun createRequestBodyFromFile(file: File): RequestBody {
    val MEDIA_TYPE_IMAGE = "multipart/form-data".toMediaTypeOrNull()
    val inputStream: InputStream = FileInputStream(file)
    val byteArray = inputStream.readBytes()
    return RequestBody.create(MEDIA_TYPE_IMAGE, byteArray)
}


fun saveImageToGallery(context: Context, bitmap: Bitmap, displayName: String): String? {
    val contentResolver: ContentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.WIDTH, bitmap.width)
        put(MediaStore.Images.Media.HEIGHT, bitmap.height)
    }

    var outputStream: OutputStream? = null
    try {
        val collection =
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val imageUri = contentResolver.insert(collection, contentValues)
        if (imageUri != null) {
            outputStream = contentResolver.openOutputStream(imageUri)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            return imageUri.toString()
        }
    } catch (e: Exception) {
        // 저장 실패 시 예외 처리
        e.printStackTrace()
    } finally {
        outputStream?.close()
    }
    return null
}