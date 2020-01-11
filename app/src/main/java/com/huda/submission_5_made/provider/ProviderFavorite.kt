package com.huda.submission_5_made.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.room.RoomMasterTable.TABLE_NAME
import com.huda.submission_5_made.database.DatabaseFavorite
import com.huda.submission_5_made.database.FavoriteDao

class ProviderFavorite : ContentProvider() {

    companion object{
        private const val CODE_FAVORITE_DIR = 1
        private const val CODE_FAVORITE_ITEM = 2
        private const val AUTHORITY = "com.huda.submission_5_made.provider"
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    }
    private lateinit var favoriteDao: FavoriteDao
    init {
        uriMatcher.addURI(AUTHORITY, TABLE_NAME, CODE_FAVORITE_DIR)
        uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", CODE_FAVORITE_ITEM)
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?, selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val code  = uriMatcher.match(uri)
        if (code == CODE_FAVORITE_DIR || code == CODE_FAVORITE_ITEM){
            if (context == null){
                Log.e("Movie Provider","Context null")
            }
            var cursor:Cursor? = null
            favoriteDao = context.let { DatabaseFavorite.getDatabase(it!!)?.favoriteDao() }!!
            if (code == CODE_FAVORITE_DIR){
                cursor = favoriteDao.getAllCursor()
            }

            cursor?.setNotificationUri(context!!.contentResolver,uri)
            return cursor
        }else{
            throw IllegalArgumentException("Unknown Uris : $uri")
        }
    }

    override fun getType(uri: Uri): String? {
        TODO("Implement this to handle requests for the MIME type of the data" + "at the given URI")
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }
}
