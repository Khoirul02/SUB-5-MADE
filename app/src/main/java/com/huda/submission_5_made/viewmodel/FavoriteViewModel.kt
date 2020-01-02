package com.huda.submission_5_made.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huda.submission_5_made.model.DataFilm

class FavoriteViewModel : ViewModel() {

    private val listDataFilm = MutableLiveData<List<DataFilm>>()
    fun setFilm(movieList: List<DataFilm>) {
        this.listDataFilm.postValue(movieList)
    }
    fun getFilm(): LiveData<List<DataFilm>> {
        return listDataFilm
    }
}