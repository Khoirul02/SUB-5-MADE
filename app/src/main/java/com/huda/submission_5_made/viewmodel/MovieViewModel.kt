package com.huda.submission_5_made.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huda.submission_5_made.BuildConfig
import com.huda.submission_5_made.model.RootData
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MovieViewModel : ViewModel() {

    private val API_KEY = BuildConfig.API_KEY

    val listDataFilm = MutableLiveData<ArrayList<RootData>>()

    internal fun setFilm(language: String, category: String) {
        val client = AsyncHttpClient()
        val listItems = ArrayList<RootData>()
        val url = "https://api.themoviedb.org/3/$category/popular?api_key=$API_KEY&language=$language&page=1"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val results = responseObject.getJSONArray("results")
                    for (i in 0 until results.length()) {
                        val film = results.getJSONObject(i)
                        val filmItems = RootData()
                        if(category == "movie"){
                            filmItems.id = film.getInt("id")
                            filmItems.name = film.getString("original_title")
                            filmItems.description = film.getString("overview")
                            filmItems.photo = film.getString("poster_path")
                            filmItems.rate = film.getDouble("vote_average")
                            filmItems.date = film.getString("release_date")
                        }else{
                            filmItems.id = film.getInt("id")
                            filmItems.name = film.getString("original_name")
                            filmItems.description = film.getString("overview")
                            filmItems.photo = film.getString("poster_path")
                            filmItems.rate = film.getDouble("vote_average")
                            filmItems.date = film.getString("first_air_date")
                        }
                        listItems.add(filmItems)
                    }
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