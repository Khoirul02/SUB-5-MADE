package com.huda.submission_5_made_favorite.task

import android.content.Context
import android.os.AsyncTask
import com.huda.submission_5_made_favorite.database.DatabaseContract
import com.huda.submission_5_made_favorite.database.MappingHelper
import com.huda.submission_5_made_favorite.model.DataFilm
import com.huda.submission_5_made_favorite.ui.fragment.FavoriteTvShowFragment
import java.lang.ref.WeakReference

class LoadFavoriteTvShowTask(context: Context, private val callback: FavoriteTvShowFragment) : AsyncTask<Void, Void, ArrayList<DataFilm>>() {
    private var contextReference: WeakReference<Context> = WeakReference(context)
    override fun doInBackground(vararg p0: Void?): ArrayList<DataFilm> {
        val favoriteCursor = contextReference.get()?.contentResolver?.query(
            DatabaseContract.MovieFavorite.CONTENT_URI, null, null, null, null
        )
        return MappingHelper.mapmapCursorToArrayList(favoriteCursor)
    }

    override fun onPostExecute(result: ArrayList<DataFilm>?) {
        super.onPostExecute(result)
        callback.onFavoriteLoaded(result ?: arrayListOf())
    }
}