package com.huda.submission_5_made_favorite.ui.fragment

import android.annotation.SuppressLint
import android.database.ContentObserver
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.huda.submission_5_made_favorite.R
import com.huda.submission_5_made_favorite.adapter.ListFavoriteMovieAdapter
import com.huda.submission_5_made_favorite.database.DatabaseContract.MovieFavorite.Companion.CONTENT_URI
import com.huda.submission_5_made_favorite.database.MappingHelper
import com.huda.submission_5_made_favorite.model.DataFilm
import kotlinx.android.synthetic.main.favorite_movie_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class FavoriteTvShowFragment : Fragment() {

    private lateinit var adapter: ListFavoriteMovieAdapter
    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorite_tv_show_fragment, container, false)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListFavoriteMovieAdapter()
        rv_movies.layoutManager = LinearLayoutManager(activity)
        rv_movies.setHasFixedSize(true)
        rv_movies.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                getDataMovie()
            }
        }
        context!!.contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)
        if (savedInstanceState == null) {
            getDataMovie()
        }else{
            val list = savedInstanceState.getParcelableArrayList<DataFilm>(EXTRA_STATE)
            if (list != null){
                adapter.listMovie = list
            }
        }
    }

    @SuppressLint("Recycle")
    private fun getDataMovie() {
        GlobalScope.launch(Dispatchers.Main) {
            progressBar.visibility = View.VISIBLE
            val deferredMovie = async(Dispatchers.IO) {
                val cursor = context?.contentResolver?.query(CONTENT_URI, null,null,null,null) as Cursor
                MappingHelper.mapmapCursorToArrayList(cursor)
            }
            val movies = deferredMovie.await()
            progressBar.visibility = View.INVISIBLE
            if (movies.size > 0){
                adapter.listMovie = movies
            }else{
                adapter.listMovie = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listMovie)
    }
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_movies, message, Snackbar.LENGTH_SHORT).show()
    }
}