package com.huda.submission_5_made_favorite.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.huda.submission_5_made_favorite.R
import com.huda.submission_5_made_favorite.adapter.ListFavoriteMovieAdapter
import com.huda.submission_5_made_favorite.model.DataFilm
import com.huda.submission_5_made_favorite.ui.activity.DetailFavoriteActivity
import kotlinx.android.synthetic.main.favorite_tv_show_fragment.*

class FavoriteTvShowFragment : Fragment() {

    private lateinit var adapter: ListFavoriteMovieAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorite_tv_show_fragment, container, false)
    }
    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListFavoriteMovieAdapter()
        adapter.notifyDataSetChanged()

        rv_movies.layoutManager = LinearLayoutManager(activity)
        rv_movies.adapter = adapter

        adapter.setOnItemClickCallback(object : ListFavoriteMovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataFilm) {
                val intent = Intent(activity, DetailFavoriteActivity::class.java)
                intent.putExtra(DetailFavoriteActivity.EXTRA_FILM, data)
                startActivity(intent)
            }
        })
    }
}