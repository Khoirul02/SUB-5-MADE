package com.huda.submission_5_made_favorite.ui.fragment

import android.annotation.SuppressLint
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.huda.submission_5_made_favorite.R
import com.huda.submission_5_made_favorite.adapter.ListFavoriteMovieAdapter
import com.huda.submission_5_made_favorite.database.DatabaseContract.MovieFavorite.Companion.CONTENT_URI
import com.huda.submission_5_made_favorite.model.DataFilm
import com.huda.submission_5_made_favorite.task.LoadFavoriteTvShowTask
import kotlinx.android.synthetic.main.favorite_tv_show_fragment.*

class FavoriteTvShowFragment : Fragment(), LoadFavoriteTvShowCallback {
    private lateinit var adapter: ListFavoriteMovieAdapter

    override fun onFavoriteLoaded(favorites: List<DataFilm>) {
        adapter.setData(favorites.filter {
            it.category == "tv" } as ArrayList<DataFilm>)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorite_tv_show_fragment, container, false)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListFavoriteMovieAdapter()

        rv_movies.layoutManager = LinearLayoutManager(activity)
        rv_movies.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                context?.let {
                    LoadFavoriteTvShowTask(it, this@FavoriteTvShowFragment).execute()
                }
            }
        }

        context?.let {
            it.contentResolver?.registerContentObserver(
                CONTENT_URI,
                true,
                myObserver
            )
            LoadFavoriteTvShowTask(it, this).execute()
        }
    }
}
interface LoadFavoriteTvShowCallback {
    fun onFavoriteLoaded(favorites: List<DataFilm>)
}
