package com.huda.submission_5_made.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.huda.submission_5_made.R
import com.huda.submission_5_made.adapter.ListFavoriteMovieAdapter
import com.huda.submission_5_made.database.DatabaseFavorite
import com.huda.submission_5_made.model.DataFilm
import com.huda.submission_5_made.model.RootData
import com.huda.submission_5_made.ui.activity.DetailFavoriteActivity
import com.huda.submission_5_made.viewmodel.FavoriteViewModel
import kotlinx.android.synthetic.main.favorite_movie_fragment.*

class FavoriteMovieFragment : Fragment() {

    private lateinit var adapter: ListFavoriteMovieAdapter
    private var databaseMovie: DatabaseFavorite? = null
    private lateinit var mainViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_movie_fragment, container, false)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseMovie = activity?.let { DatabaseFavorite.getDatabase(it) }

        adapter = ListFavoriteMovieAdapter()
        adapter.notifyDataSetChanged()

        rv_movies.layoutManager = LinearLayoutManager(activity)
        rv_movies.adapter = adapter

        mainViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()).get(FavoriteViewModel::class.java)
        showLoading(true)
        mainViewModel.getFilm().observe(this, Observer { movieList->
            if (movieList!!.isNotEmpty()) {
                adapter.setData(movieList)
                showLoading(false)
            } else{
                val empety = resources.getString(R.string.empety)
                adapter.listMovie = ArrayList()
                showSnackbarMessage(empety)
                showLoading(false)
            }
        })
        adapter.setOnItemClickCallback(object : ListFavoriteMovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataFilm) {
                val intent = Intent(activity, DetailFavoriteActivity::class.java)
                intent.putExtra(DetailFavoriteActivity.EXTRA_FILM, data)
                startActivity(intent)
            }
        })
        getDataMovie(this).execute()
    }
    private class getDataMovie(var context: FavoriteMovieFragment) : AsyncTask<Void, Void, List<DataFilm>>() {
        override fun doInBackground(vararg params: Void?): List<DataFilm> {
            return context.databaseMovie!!.favoriteDao().getMovie()
        }
        override fun onPostExecute(movieList: List<DataFilm>?) {
            if (movieList!!.isNotEmpty()) {
                context.mainViewModel.setFilm(movieList)
            } else {
                context.mainViewModel.setFilm(movieList)
            }
        }
    }
    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_movies, message, Snackbar.LENGTH_SHORT).show()
    }
}

