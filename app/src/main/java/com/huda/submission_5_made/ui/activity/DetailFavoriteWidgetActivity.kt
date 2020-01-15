package com.huda.submission_5_made.ui.activity

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.huda.submission_5_made.R
import com.huda.submission_5_made.database.DatabaseFavorite
import com.huda.submission_5_made.model.DataFilm
import com.huda.submission_5_made.viewmodel.DetailMovieViewModel
import kotlinx.android.synthetic.main.activity_detail_favorite_widget.*
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class DetailFavoriteWidgetActivity : AppCompatActivity() {

    private var database: DatabaseFavorite? = null

    private lateinit var mainViewModel: DetailMovieViewModel
    companion object {
        const val EXTRA_FILM = "id_film"
        const val EXTRA_FILM_2 = "category_film"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_favorite_widget)
        val title = resources.getString(R.string.title_favorite_movie_detail)
        supportActionBar?.title = title

        database = DatabaseFavorite.getDatabase(this)

        val language = resources.getString(R.string.language_string)
        val idFilm = intent.getIntExtra(EXTRA_FILM,0)
        val category = intent.getStringExtra(EXTRA_FILM_2)
        CheckFavorite(this, idFilm).execute()
        mainViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()).get(DetailMovieViewModel::class.java)
        mainViewModel.setFilm(this,idFilm,language, tv_item_name_detail,tv_item_description_detail,img_item_photo_detail,tv_nilai_rate_detail,category)
        showLoading(true)
        mainViewModel.getFilm().observe(this, Observer { filmItems->
            if (filmItems!!.isNotEmpty()) {
                showLoading(false)
            } else{
                val failed = getString(R.string.failed_load_data)
                Toast.makeText(this, failed, Toast.LENGTH_LONG).show()
                showLoading(false)
            }
        })
        img_favorite.setOnClickListener {
            DeleteTask(this,idFilm).execute()
        }

    }
    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarDetail.visibility = View.VISIBLE
            tv_item_name_detail.visibility = View.GONE
            tv_item_description_detail.visibility = View.GONE
            img_favorite.visibility = View.GONE
            tv_nilai_rate_detail.visibility = View.GONE
            img_item_photo_detail.visibility = View.GONE
            img_background_favorite.visibility = View.GONE
            text_rate.visibility = View.GONE
        } else {
            progressBarDetail.visibility = View.GONE
            tv_item_name_detail.visibility = View.VISIBLE
            tv_item_description_detail.visibility = View.VISIBLE
            img_favorite.visibility = View.VISIBLE
            tv_nilai_rate_detail.visibility = View.VISIBLE
            img_item_photo_detail.visibility = View.VISIBLE
            img_background_favorite.visibility = View.VISIBLE
            text_rate.visibility = View.VISIBLE
        }
    }
    private class CheckFavorite(var context: DetailFavoriteWidgetActivity, var idFilm: Int) : AsyncTask<Void, Void, List<DataFilm>>() {
        override fun doInBackground(vararg params: Void?): List<DataFilm> {
            return context.database!!.favoriteDao().getById(idFilm)
        }
        override fun onPostExecute(checkList : List<DataFilm>) {
            if (checkList.isNotEmpty()) {
                context.img_favorite.setImageResource(R.drawable.ic_favorite_black)
            }else{
                context.img_favorite.setImageResource(R.drawable.ic_favorite_border_black)
            }
        }
    }
    private class DeleteTask(var context: DetailFavoriteWidgetActivity, var id: Int) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            context.database!!.favoriteDao().deleteByIdFilm(id)
            return true
        }
        override fun onPostExecute(bool: Boolean?) {
            if (bool!!) {
                Toast.makeText(context, "Delete for Favorite", Toast.LENGTH_LONG).show()
                CheckFavorite(context,id).execute()
            }
        }
    }
}
