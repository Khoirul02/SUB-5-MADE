package com.huda.submission_5_made_favorite.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.huda.submission_5_made_favorite.R
import com.huda.submission_5_made_favorite.adapter.ListFavoriteMovieAdapter
import com.huda.submission_5_made_favorite.model.DataFilm
import kotlinx.android.synthetic.main.activity_detail_favorite.*

class DetailFavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: ListFavoriteMovieAdapter
    companion object {
        const val EXTRA_FILM = "extra_person"
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_favorite)
        val title = resources.getString(R.string.title_favorite_movie_detail)
        supportActionBar?.title = title
        val data = intent.getParcelableExtra(EXTRA_FILM) as? DataFilm
        val idFilm = data!!.idfilm
        tv_item_name_detail.text = data.name
        tv_item_description_detail.text = data.description
        Glide.with(this)
            .load("https://image.tmdb.org/t/p/w185_and_h278_bestv2" + data.photo)
            .apply(RequestOptions().override(512, 512))
            .into(img_item_photo_detail)
        val percent = data.rate * 10
        tv_nilai_rate_detail.text = "$percent%"

        adapter.setOnItemClickCallback(object : ListFavoriteMovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: DataFilm) {
                val intent = Intent(this@DetailFavoriteActivity, DetailFavoriteActivity::class.java)
                intent.putExtra(DetailFavoriteActivity.EXTRA_FILM, data)
                startActivity(intent)
            }
        })
    }
}
