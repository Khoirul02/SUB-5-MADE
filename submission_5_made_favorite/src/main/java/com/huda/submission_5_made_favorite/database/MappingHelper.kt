package com.huda.submission_5_made_favorite.database
import android.database.Cursor
import com.huda.submission_5_made_favorite.model.DataFilm

object MappingHelper {
    fun mapmapCursorToArrayList(filmCursor: Cursor?): ArrayList<DataFilm> {
        if (filmCursor == null) {
            return arrayListOf()
        } else {
            val movies = ArrayList<DataFilm>()
            while (filmCursor.moveToNext()) {
                val id =
                    filmCursor.getLong(filmCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite._ID))
                val idfilm =
                    filmCursor.getInt(filmCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.IDFILM))
                val name =
                    filmCursor.getString(filmCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.NAME))
                val photo =
                    filmCursor.getString(filmCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.PHOTO))
                val description =
                    filmCursor.getString(filmCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.DESCRIPTION))
                val rate =
                    filmCursor.getDouble(filmCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.RATE))
                val date =
                    filmCursor.getString(filmCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.DATE))
                val category =
                    filmCursor.getString(filmCursor.getColumnIndexOrThrow(DatabaseContract.MovieFavorite.CATEGORY))
                movies.add(
                    DataFilm(
                        idfilm, name, photo, description, rate, date, category, id
                    )
                )
            }
            return movies
        }
    }
}