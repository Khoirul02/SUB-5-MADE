package com.huda.submission_5_made.ui.activity

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.huda.submission_5_made.R
import com.huda.submission_5_made.database.DatabaseFavorite
import com.huda.submission_5_made.model.DataFilm
import kotlinx.android.synthetic.main.activity_detail_favorite.*

@Suppress("NAME_SHADOWING")
@SuppressLint("Registered")
class DetailFavoriteActivity : AppCompatActivity() {

    private var database: DatabaseFavorite? = null
    companion object {
        const val EXTRA_FILM = "extra_person"
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_favorite)
        val title = resources.getString(R.string.title_favorite_movie_detail)
        supportActionBar?.title = title
        database = DatabaseFavorite.getDatabase(this)

        val data = intent.getParcelableExtra(EXTRA_FILM) as? DataFilm
        val idFilm = data!!.idfilm
        if (idFilm != null) {
            CheckFavorite(this, idFilm).execute()
        }
        tv_item_name_detail.text = data.name
        tv_item_description_detail.text = data.description
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w185_and_h278_bestv2" + data.photo)
            .apply(RequestOptions().override(512, 512))
            .into(img_item_photo_detail)
        val percent = data.rate * 10
        tv_nilai_rate_detail.text = "$percent%"

        img_favorite.setOnClickListener {
            val idFilm = data.id
            DeleteTask(this,idFilm).execute()
        }
    }
    private class CheckFavorite(var context: DetailFavoriteActivity, var idFilm: Int) : AsyncTask<Void, Void, List<DataFilm>>() {
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
    private class DeleteTask(var context: DetailFavoriteActivity, var id: Long) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            context.database!!.favoriteDao().deleteById(id)
            return true
        }
        override fun onPostExecute(bool: Boolean?) {
            if (bool!!) {
                Toast.makeText(context, "Delete for Favorite", Toast.LENGTH_LONG).show()
                CheckFavorite(context,id.toInt()).execute()
            }
        }
    }
}