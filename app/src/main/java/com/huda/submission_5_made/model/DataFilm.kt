package com.huda.submission_5_made.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "favorite")
data class DataFilm(
    @ColumnInfo(name = "id_film") var idfilm: Int?,
    @ColumnInfo(name = "original_title") var name: String?,
    @ColumnInfo(name = "poster_path") var photo: String?,
    @ColumnInfo(name = "overview") var description: String?,
    @ColumnInfo(name = "vote_average") var rate: Double = 0.1,
    @ColumnInfo(name = "release_date") var date: String?,
    @ColumnInfo(name = "category") var category: String?,
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0
) : Parcelable