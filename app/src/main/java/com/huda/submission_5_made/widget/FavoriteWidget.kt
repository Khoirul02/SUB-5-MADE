package com.huda.submission_5_made.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.huda.submission_5_made.R
import com.huda.submission_5_made.ui.activity.DetailFavoriteWidgetActivity

/**
 * Implementation of App Widget functionality.
 */
@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class FavoriteWidget : AppWidgetProvider() {
    companion object {

        const val EXTRA_CATEGORY = "com.submission_5_made.widget.EXTRA_CATEGORY"
        const val EXTRA_ITEM = "com.submission_5_made.widget.EXTRA_ITEM"
        private const val TOAST_ACTION = "com.submission_5_made.widget.TOAST_ACTION"

        private fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int
        ) {
            val intent = Intent(context, StackWidgetServiceFavorite::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val views = RemoteViews(context.packageName, R.layout.image_banner_widget_favorite)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            val toastIntent = Intent(context, FavoriteWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }


    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action != null) {
            val movies = intent.getIntExtra(EXTRA_ITEM,0)
            val category = intent.getStringExtra(EXTRA_CATEGORY)
            openDetailFilm(context,movies,category)
        }
    }

    private fun openDetailFilm(context: Context, movies: Int, category: String) {
        val intent = Intent(context, DetailFavoriteWidgetActivity::class.java)
        intent.putExtra(DetailFavoriteWidgetActivity.EXTRA_FILM, movies)
        intent.putExtra(DetailFavoriteWidgetActivity.EXTRA_FILM_2, category)
        context.startActivity(intent)
    }
}