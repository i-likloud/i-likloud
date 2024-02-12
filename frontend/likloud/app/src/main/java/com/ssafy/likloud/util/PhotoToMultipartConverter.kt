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

/**
 * uri로 multipart 객체를 만듭니다.
 */
fun createMultipartFromUri(context: Context, uri: Uri): MultipartBody.Part? {
    val file: File? = getFileFromUri(context, uri)
    if (file == null) {
        // 파일을 가져오지 못한 경우 처리할 로직을 작성하세요.
        return null
    }

    val requestFile: RequestBody = createRequestBodyFromFile(file)
    return MultipartBody.Part.createFormData("multipartFiles", file.name, requestFile)
}


fun createMultipartFromUriNameFile(context: Context, uri: Uri): MultipartBody.Part? {
    val file: File? = getFileFromUri(context, uri)
    if (file == null) {
        // 파일을 가져오지 못한 경우 처리할 로직을 작성하세요.
        return null
    }

    val requestFile: RequestBody = createRequestBodyFromFile(file)
    return MultipartBody.Part.createFormData("file", file.name, requestFile)
}

/**
 * uri로 사진 파일을 가져옵니다
 * createMultipartFromUri로 결과값을 반환합니다
 */
fun getFileFromUri(context: Context, uri: Uri): File? {
    val filePath = uriToFilePath(context, uri)
    return if (filePath != null) File(filePath) else null
}

/**
 * 만들어진 uri를 파일로 변환합니다
 */
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


/**
 * 저장된 사진 파일의 body를 가져옵니다
 */
private fun createRequestBodyFromFile(file: File): RequestBody {
    val MEDIA_TYPE_IMAGE = "multipart/form-data".toMediaTypeOrNull()
    val inputStream: InputStream = FileInputStream(file)
    val byteArray = inputStream.readBytes()
    return RequestBody.create(MEDIA_TYPE_IMAGE, byteArray)
}


/**
 * 카메라에서 찍은 사진을 갤러리에 저장합니다.
 */
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

fun deleteImageFromGallery(context: Context, imageUri: String): Boolean {
    val contentResolver: ContentResolver = context.contentResolver
    try {
        // 이미지 URI를 Uri 객체로 파싱
        val uri = Uri.parse(imageUri)

        // 해당 이미지 삭제
        val deleted = contentResolver.delete(uri, null, null)

        // 삭제 성공 여부 반환
        return deleted > 0
    } catch (e: Exception) {
        // 삭제 실패 시 예외 처리
        e.printStackTrace()
    }
    return false
}


fun dontSaveImageToGallery(context: Context, bitmap: Bitmap, displayName: String): String? {
    val contentResolver: ContentResolver = context.contentResolver
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        put(MediaStore.Images.Media.WIDTH, bitmap.width)
        put(MediaStore.Images.Media.HEIGHT, bitmap.height)
    }

    // 이미지를 저장하지 않고, 가짜 URI 반환
    val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
    val imageUri = contentResolver.insert(collection, contentValues)
    if (imageUri != null) {
        return imageUri.toString()
    }

    return null
}


