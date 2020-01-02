package com.huda.submission_5_made.viewmodel

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.huda.submission_5_made.BuildConfig
import com.huda.submission_5_made.model.RootData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class DetailMovieViewModel : ViewModel() {

    private val API_KEY = BuildConfig.API_KEY

    val listDataFilm = MutableLiveData<ArrayList<RootData>>()

    internal fun setFilm(
        context: Context,
        id: Int,
        language: String,
        name: TextView,
        description: TextView,
        photo: ImageView,
        rate: TextView,
        category: String
    ) {
        val client = AsyncHttpClient()
        val listItems = ArrayList<RootData>()
        val url = "https://api.themoviedb.org/3/$category/$id?api_key=$API_KEY&$language"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val filmItems = RootData()
                    if(category == "movie"){
                        filmItems.id = id
                        filmItems.name = responseObject.getString("original_title")
                        name.text = filmItems.name
                        filmItems.description = responseObject.getString("overview")
                        description.text = filmItems.description
                        filmItems.photo = responseObject.getString("poster_path")
                        Glide.with(context)
                            .load(
                                "https://image.tmdb.org/t/p/w185_and_h278_bestv2" + filmItems.photo
                            )
                            .apply(RequestOptions().override(512, 512))
                            .into(photo)
                        filmItems.rate = responseObject.getDouble("vote_average")
                        rate.text = filmItems.rate.toString()
                    }else{
                        filmItems.id = id
                        filmItems.name = responseObject.getString("original_name")
                        name.text = filmItems.name
                        filmItems.description = responseObject.getString("overview")
                        description.text = filmItems.description
                        filmItems.photo = responseObject.getString("poster_path")
                        Glide.with(context)
                            .load(
                                "https://image.tmdb.org/t/p/w185_and_h278_bestv2" + filmItems.photo
                            )
                            .apply(RequestOptions().override(512, 512))
                            .into(photo)
                        filmItems.rate = responseObject.getDouble("vote_average")
                        rate.text = filmItems.rate.toString()
                    }
                    listItems.add(filmItems)
                    listDataFilm.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    internal fun getFilm(): LiveData<ArrayList<RootData>> {
        return listDataFilm
    }
}