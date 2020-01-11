package com.huda.submission_5_made.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.huda.submission_5_made.model.DataFilm

@Database(entities = [DataFilm::class], version = 1)
abstract class DatabaseFavorite : RoomDatabase(), FavoriteDao {

    abstract fun favoriteDao() : FavoriteDao
    companion object{
        private var INSTANCE : DatabaseFavorite? = null
        fun getDatabase(context: Context) : DatabaseFavorite? {
            if (INSTANCE == null){
                synchronized(DatabaseFavorite::class){
                        INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseFavorite::class.java, "datafilm.db"
                    ).allowMainThreadQueries().build()
                }
            }
            return INSTANCE as DatabaseFavorite
        }
    }
}