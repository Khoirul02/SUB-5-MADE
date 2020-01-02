package com.huda.submission_5_made.ui.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.huda.submission_5_made.R
import com.huda.submission_5_made.adapter.ListMovieAdapter
import com.huda.submission_5_made.model.RootData
import com.huda.submission_5_made.ui.activity.DetailMovieActivity
import com.huda.submission_5_made.viewmodel.MovieViewModel
import com.huda.submission_5_made.viewmodel.SearchMovieViewModel
import kotlinx.android.synthetic.main.fragment_movie.*

@Suppress("UNREACHABLE_CODE", "CAST_NEVER_SUCCEEDS")
class MovieFragment : Fragment() {

    private lateinit var adapter: ListMovieAdapter
    private lateinit var mainViewModel: MovieViewModel
    private lateinit var mainViewModelSearch: SearchMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("FragmentLiveDataObserve")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ListMovieAdapter()
        adapter.notifyDataSetChanged()

        val language = resources.getString(R.string.language_string)
        val category = getString(R.string.category_movie)


        rv_movies.layoutManager = activity?.let { LinearLayoutManager(it) }
        rv_movies.adapter = adapter
        mainViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(MovieViewModel::class.java)
        mainViewModel.setFilm(language,category)
        showLoading(true)
        mainViewModel.getFilm().observe(this, Observer { filmItems ->
            if (filmItems != null) {
                adapter.setData(filmItems)
                showLoading(false)
            }
        })
        adapter.setOnItemClickCallback(object : ListMovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: RootData) {
                val intent = Intent(activity, DetailMovieActivity::class.java)
                intent.putExtra(DetailMovieActivity.EXTRA_FILM, data)
                startActivity(intent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                adapter = ListMovieAdapter()
                adapter.notifyDataSetChanged()
                val language = resources.getString(R.string.language_string)
                val category = getString(R.string.category_movie)
                rv_movies.layoutManager = activity?.let { LinearLayoutManager(it) }
                rv_movies.adapter = adapter

                mainViewModelSearch = ViewModelProvider(
                    this@MovieFragment, ViewModelProvider.NewInstanceFactory()
                ).get(SearchMovieViewModel::class.java)

                mainViewModelSearch.setFilm(query,language,category)

                showLoading(true)
                mainViewModelSearch.getFilm().observe(this@MovieFragment, Observer { filmItems ->
                    if (filmItems != null) {
                        adapter.setData(filmItems)
                        showLoading( false)
                    }else{
                        adapter.setData(filmItems)
                        val movieSearchEmpety = getString(R.string.empety_search_movie)
                        Toast.makeText(activity, movieSearchEmpety,Toast.LENGTH_LONG).show()
                        showLoading(false)
                    }
                })
                adapter.setOnItemClickCallback(object : ListMovieAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: RootData) {
                        val intent = Intent(activity, DetailMovieActivity::class.java)
                        intent.putExtra(DetailMovieActivity.EXTRA_FILM, data)
                        startActivity(intent)
                    }
                })

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }
}