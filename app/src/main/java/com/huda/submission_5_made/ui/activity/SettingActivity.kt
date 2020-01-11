package com.huda.submission_5_made.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import com.huda.submission_5_made.BuildConfig
import com.huda.submission_5_made.R
import com.huda.submission_5_made.scheduler.AlarmReceiver
import com.huda.submission_5_made.scheduler.ReleaseAlarmReceiver
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_setting.*
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*


@Suppress("NAME_SHADOWING", "JAVA_CLASS_ON_COMPANION")
class SettingActivity : AppCompatActivity() {

    private lateinit var alarmReceiver : AlarmReceiver
    private lateinit var releaseAlarmReceiver : ReleaseAlarmReceiver
    companion object {
        const val PREFS_SWITCH = "MyPrefsSwitch"
        const val PREFS_SWITCH_2 = "MyPrefsSwitch2"
        private val API_KEY = BuildConfig.API_KEY
        private val TAG = SettingActivity.javaClass.simpleName
    }

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.title = getString(R.string.Setting)
        alarmReceiver = AlarmReceiver()
        releaseAlarmReceiver = ReleaseAlarmReceiver()
        val settings = getSharedPreferences(PREFS_SWITCH, 0)
        val silent = settings.getBoolean("switchkey", false)
        switch_daily_reminder.isChecked = silent
        switch_daily_reminder.setOnCheckedChangeListener { compoundButton, b ->
            if (switch_daily_reminder.isChecked) {
                content_time.visibility = View.VISIBLE
                dailyReminderActive()
            } else {
                content_time.visibility = View.GONE
                dailyReminderNoActive()
            }
            val settings = getSharedPreferences(PREFS_SWITCH, 0)
            val editor = settings.edit()
            editor.putBoolean("switchkey", b)
            editor.apply()
         }
        val settings2 = getSharedPreferences(PREFS_SWITCH_2, 0)
        val silent2 = settings2.getBoolean("switchkey_2", false)
        switch_daily_reminder_today.isChecked = silent2
        switch_daily_reminder_today.setOnCheckedChangeListener { compoundButton, b ->
            if (switch_daily_reminder_today.isChecked) {
                dailyReminderTodayActive()
            } else {
                dailyReminderTodayNoActive()
            }
            val settings = getSharedPreferences(PREFS_SWITCH_2, 0)
            val editor = settings.edit()
            editor.putBoolean("switchkey_2", b)
            editor.apply()
        }
    }
    @SuppressLint("SimpleDateFormat")
    private fun dailyReminderActive(){
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        tv_time.text = simpleDateFormat.format(calendar.time)
        val reminder =simpleDateFormat.format(calendar.time)
        val message = "Please Back Open Application Catalog Movie"
        alarmReceiver.setReminder(this,reminder,message)
    }

    private fun dailyReminderNoActive(){
        alarmReceiver.cancelReminder(this)
    }
    @SuppressLint("SimpleDateFormat")
    private fun dailyReminderTodayActive() {
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val calender2 = Calendar.getInstance()
        calender2.get(Calendar.YEAR)
        calender2.get(Calendar.MONTH)
        calender2.get(Calendar.DATE)
        val date = dateFormat.format(calender2.time)
        val reminder = simpleDateFormat.format(calendar.time)
        getMovieReleaseToday(this,date,reminder)
    }
    private fun getMovieReleaseToday(context: Context,date: String, reminder: String) {
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$API_KEY&primary_release_date.gte=$date&primary_release_date.lte=$date"
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
                        releaseAlarmReceiver.setReminderRelease(context,title,reminder,description, id)
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

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun dailyReminderTodayNoActive(){
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val calender2 = Calendar.getInstance()
        calender2.get(Calendar.YEAR)
        calender2.get(Calendar.MONTH)
        calender2.get(Calendar.DATE)
        val date = dateFormat.format(calender2.time)
        cekIdReleaseToday(this, date)
    }
    private fun cekIdReleaseToday(context: Context, date: String) {
        val client = AsyncHttpClient()
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$API_KEY&primary_release_date.gte=$date&primary_release_date.lte=$date"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<Header>, responseBody: ByteArray) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val results = responseObject.getJSONArray("results")
                    for (i in 0 until results.length()) {
                        val id = results.getJSONObject(i).getInt("id")
                        releaseAlarmReceiver.cancelReminderRelease(context, id)
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
}
