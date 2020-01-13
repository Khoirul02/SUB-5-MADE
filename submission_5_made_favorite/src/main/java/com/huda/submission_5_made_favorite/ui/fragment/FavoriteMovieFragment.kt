package com.huda.submission_5_made_favorite.ui.fragment
import android.content.Intent
import android.database.ContentObserver
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.huda.submission_5_made_favorite.R
import com.huda.submission_5_made_favorite.adapter.ListFavoriteMovieAdapter
import com.huda.submission_5_made_favorite.database.DatabaseContract
import com.huda.submission_5_made_favorite.model.DataFilm
import com.huda.submission_5_made_favorite.task.LoadFavoriteTask
import com.huda.submission_5_made_favorite.ui.activity.DetailFavoriteActivity
import kotlinx.android.synthetic.main.favorite_movie_fragment.*

class FavoriteMovieFragment : Fragment(), LoadFavoriteCallback {

    private lateinit var adapter: ListFavoriteMovieAdapter

    override fun onFavoriteLoaded(favorites: List<DataFilm>) {
        adapter.setData(favorites.filter {
            it.category == "movie" } as ArrayList<DataFilm>)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.favorite_movie_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
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
                    LoadFavoriteTask(it, this@FavoriteMovieFragment).execute()
                }
            }
        }

        context?.let {
            it.contentResolver?.registerContentObserver(
                DatabaseContract.MovieFavorite.CONTENT_URI,
                true,
                myObserver
            )
            LoadFavoriteTask(it, this).execute()
        }
        adapter.setOnItemClickCallback(object : ListFavoriteMovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataFilm) {
                val intent = Intent(activity, DetailFavoriteActivity::class.java)
                intent.putExtra(DetailFavoriteActivity.EXTRA_FILM, data)
                startActivity(intent)
            }
        })
    }
}
interface LoadFavoriteCallback {
    fun onFavoriteLoaded(favorites: List<DataFilm>)
}
