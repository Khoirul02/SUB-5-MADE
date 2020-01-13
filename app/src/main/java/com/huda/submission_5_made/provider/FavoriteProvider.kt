package com.huda.submission_5_made.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log
import com.huda.submission_5_made.database.DatabaseFavorite
import com.huda.submission_5_made.database.FavoriteDao

class FavoriteProvider : ContentProvider() {

    companion object{
        private const val CODE_FAVORITE_DIR = 1
        private const val CODE_FAVORITE_ITEM = 2
        private const val TABLE_NAME = "favorite"
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

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        context?.contentResolver?.notifyChange(uri, null)
        return uri
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        context?.contentResolver?.notifyChange(uri, null)
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }
}