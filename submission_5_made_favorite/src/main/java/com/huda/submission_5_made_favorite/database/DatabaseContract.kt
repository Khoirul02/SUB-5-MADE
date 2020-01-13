package com.huda.submission_5_made_favorite.database

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {
    const val AUTHORITY = "com.huda.submission_5_made.provider"
    const val SCHEME = "content"

    class MovieFavorite : BaseColumns {

        companion object {
            private const val TABLE_NAME = "favorite"
            const val _ID = "id"
            const val IDFILM = "id_film"
            const val NAME = "original_title"
            const val PHOTO = "poster_path"
            const val DESCRIPTION = "overview"
            const val RATE = "vote_average"
            const val DATE = "release_date"
            const val CATEGORY = "category"

            val CONTENT_URI: Uri = Uri.Builder()
                .scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}