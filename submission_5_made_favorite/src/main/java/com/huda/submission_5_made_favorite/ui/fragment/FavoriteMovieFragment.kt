package com.huda.submission_5_made_favorite.ui.fragment
import android.database.ContentObserver
import android.media.tv.TvContract.Channels.CONTENT_URI
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.huda.submission_5_made_favorite.R
import com.huda.submission_5_made_favorite.adapter.ListFavoriteMovieAdapter
import com.huda.submission_5_made_favorite.model.DataFilm
import kotlinx.android.synthetic.main.favorite_movie_fragment.*

class FavoriteMovieFragment : Fragment(), LoadFavoriteCallback {

    private lateinit var adapter: ListFavoriteMovieAdapter

    override fun onFavoriteLoaded(favorites: List<DataFilm>) {
//        adapter.setData(favorites as ArrayList<DataFilm>)
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
            }
        }
        context?.contentResolver?.registerContentObserver(CONTENT_URI, true, myObserver)

    }

}
interface LoadFavoriteCallback {
    fun onFavoriteLoaded(favorites: List<DataFilm>)
}
