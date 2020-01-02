package com.huda.submission_5_made.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.huda.submission_5_made.R
import com.huda.submission_5_made.database.DatabaseFavorite
import com.huda.submission_5_made.model.DataFilm

@Suppress("CAST_NEVER_SUCCEEDS", "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")

class StackRemoteViewsFactoryFavorite(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private var database: DatabaseFavorite? = null
    private var listMovie = ArrayList<DataFilm>()

    override fun onCreate() {

    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = 0

    override fun onDataSetChanged() {
        database = DatabaseFavorite.getDatabase(mContext)
        listMovie = database!!.favoriteDao().getAll() as ArrayList<DataFilm>
    }

    override fun hasStableIds(): Boolean = false

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.item_widget)
        val urlbitmap = Glide.with(mContext)
                .asBitmap()
                .load("https://image.tmdb.org/t/p/w185_and_h278_bestv2"+listMovie[position].photo)
                .apply(RequestOptions() )
                .submit()
                .get()
        rv.setImageViewBitmap(R.id.imageViewFavorite, urlbitmap)
        rv.setTextViewText(R.id.category, listMovie[position].category)
        val extras = bundleOf(
            FavoriteWidget.EXTRA_ITEM to listMovie[position].idfilm ,
            FavoriteWidget.EXTRA_CATEGORY to listMovie[position].category
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.detail_favorite, fillInIntent)
        return rv
    }

    override fun getCount(): Int = listMovie.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {

    }
}