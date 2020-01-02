package com.huda.submission_5_made.widget

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.RemoteViewsService

@SuppressLint("Registered")
class StackWidgetServiceFavorite : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackRemoteViewsFactoryFavorite(this.applicationContext)

}