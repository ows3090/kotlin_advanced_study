package ows.kotlinstudy.camera_application.util

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import ows.kotlinstudy.sns_application.R
import java.io.File

object PathUtil {

    fun getOutputDirectory(activity: Activity): File = with(activity){
        Log.d("msg", "externalMediaDirs ${externalMediaDirs.firstOrNull()?.absolutePath}")
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, getString(R.string.app_name)).apply { mkdir() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else filesDir
    }

    fun getPath(context: Context, uri: Uri): String? {
        Log.d("msg","getPath : ${uri}")

        // android에서 4.4(api 19) 이후부터 SAF가 도입되어 문서 제공자 전체에서 문서를 탐색하고, 여는 작업을 손쉽게 도와
        // DocumentProvider에서 지원하는 URI인지 확인
        if(DocumentsContract.isDocumentUri(context, uri)){
            Log.d("msg","DocumentsContract is DocumentUri")
            if(isExternalStorageDocument(uri)){
                val docId = DocumentsContract.getDocumentId(uri)
                Log.d("msg","docId : ${docId}")

                val split = docId.split(":").toTypedArray()
                val type = split[0]
                if("primary".equals(type, ignoreCase = true)){
                    Log.d("msg","getExternalFilesDir : ${context.getExternalFilesDir(null)?.absolutePath.toString() + "/" + split[1]}")
                    return context.getExternalFilesDir(null)?.absolutePath.toString() + "/"+split[1]
                }
            }else if(isDownloadsDocument(uri)){
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), id.toLong()
                )

                return getDataColumn(context, contentUri, null, null)
            }else if(isMediaDocument(uri)){
                val docId = DocumentsContract.getDocumentId(uri)
                Log.d("msg","docId : ${docId}")
                val split = docId.split(":").toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if("image" == type){
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                }else if("video" == type){
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                }else if("audio" == type){
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }

                val selection = "_id=?"
                val selectionArgs = arrayOf(
                    split[1]
                )
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        }else if("content".equals(uri.scheme, ignoreCase = true)){
            Log.d("msg","content uri scheme")
            return getDataColumn(context, uri, null, null)
        }else if("file".equals(uri.scheme, ignoreCase = true)){
            Log.d("msg","file uri scheme")
            return uri.path
        }
        return null
    }

    private fun getDataColumn(
        context: Context,
        uri: Uri?,
        selection: String?,
        selectionArgs: Array<String>?
    ): String?{
        var cursor: Cursor? = null
        val column = "_data"
        val projection = arrayOf(column)
        try{
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,null
            )

            if(cursor != null && cursor.moveToFirst()){
                val column_index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            cursor?.close()
        }
        return null
    }

    private fun isExternalStorageDocument(uri: Uri): Boolean{
        return "com.android.externalstorage.documents" == uri.authority
    }

    private fun isDownloadsDocument(uri: Uri): Boolean{
        return "com.android.providers.downloads.documents" == uri.authority
    }

    private fun isMediaDocument(uri: Uri): Boolean{
        return "com.android.providers.media.documents" == uri.authority
    }

}