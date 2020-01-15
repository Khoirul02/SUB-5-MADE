package com.huda.submission_5_made.scheduler

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.huda.submission_5_made.BuildConfig
import com.huda.submission_5_made.R
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS",
    "RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "JAVA_CLASS_ON_COMPANION"
)
class ReleaseAlarmReceiver : BroadcastReceiver() {
    companion object {
        const val EXTRA_ID = "id"
        private const val TIME_FORMAT = "HH:mm"
        private val TAG = ReleaseAlarmReceiver.javaClass.simpleName
        private const val API_KEY = BuildConfig.API_KEY

    }

    @SuppressLint("SimpleDateFormat")
    override fun onReceive(context: Context, intent: Intent) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val calender = Calendar.getInstance()
        calender.get(Calendar.YEAR)
        calender.get(Calendar.MONTH)
        calender.get(Calendar.DATE)
        val date = dateFormat.format(calender.time)
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=${API_KEY}&primary_release_date.gte=$date&primary_release_date.lte=$date"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val results = responseObject.getJSONArray("results")
                    for (i in 0 until results.length()) {
                        val title = results.getJSONObject(i).getString("original_title")
                        val description = results.getJSONObject(i).getString("overview")
                        val id = results.getJSONObject(i).getInt("id")
                        intent.putExtra(EXTRA_ID, id)
                        showAlarmNotificationRelease(context, title, description, id)
                    }
                    Log.d(TAG, "onSuccess: Selesai.....")
                } catch (e: Exception) {
                    Log.d(TAG, "onSuccess: Gagal.....")
                    e.printStackTrace()
                }
            }
            override fun onFailure(statusCode: Int, headers: Array<Header>, responseBody: ByteArray, error: Throwable) {
                Log.d(TAG, "onFailure: Gagal.....")
            }
        })
    }

    fun setReminderRelease(context: Context, time: String) {

        if (isDateInvalid(time, TIME_FORMAT)) return

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseAlarmReceiver::class.java)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val timeArray = time.split(":").toTypedArray()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeArray[0]))
        calendar.set(Calendar.MINUTE, Integer.parseInt(timeArray[1]))
        calendar.set(Calendar.SECOND, 0)

        val pendingIntent = PendingIntent.getBroadcast(context, id, intent, 0)
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)

        Toast.makeText(context, "Reminder Release Active", Toast.LENGTH_SHORT).show()
    }

    private fun showAlarmNotificationRelease(context: Context, title: String, message: String, idNotifValue: Int) {
        val CHANNEL_ID = "Channel_2"
        val CHANNEL_NAME = "Release"
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_android_black_24dp)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Create or update. */
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(idNotifValue, notification)
    }

    fun cancelReminderRelease(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReleaseAlarmReceiver::class.java)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val pendingIntent = PendingIntent.getBroadcast(context, id , intent, 0)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
        Toast.makeText(context, "Reminder Release Cancel", Toast.LENGTH_SHORT).show()
    }

    private fun isDateInvalid(date: String, format: String): Boolean {
        return try {
            val df = SimpleDateFormat(format, Locale.getDefault())
            df.isLenient = false
            df.parse(date)
            false
        } catch (e: ParseException) {
            true
        }
    }
}
