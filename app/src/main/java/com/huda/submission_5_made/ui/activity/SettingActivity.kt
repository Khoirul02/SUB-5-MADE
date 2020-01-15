package com.huda.submission_5_made.ui.activity

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.huda.submission_5_made.R
import com.huda.submission_5_made.scheduler.AlarmReceiver
import com.huda.submission_5_made.scheduler.ReleaseAlarmReceiver
import kotlinx.android.synthetic.main.activity_setting.*
import java.text.SimpleDateFormat
import java.util.*


@Suppress("NAME_SHADOWING", "JAVA_CLASS_ON_COMPANION")
class SettingActivity : AppCompatActivity() {

    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var releaseAlarmReceiver: ReleaseAlarmReceiver

    companion object {
        const val PREFS_SWITCH = "MyPrefsSwitch"
        const val PREFS_SWITCH_2 = "MyPrefsSwitch2"
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
    private fun dailyReminderActive() {
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        tv_time.text = simpleDateFormat.format(calendar.time)
        val reminder = simpleDateFormat.format(calendar.time)
        val message = "Please Back Open Application Catalog Movie"
        alarmReceiver.setReminder(this, reminder, message)
    }

    private fun dailyReminderNoActive() {
        alarmReceiver.cancelReminder(this)
    }
    @SuppressLint("SimpleDateFormat")
    private fun dailyReminderTodayActive() {
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val reminder = simpleDateFormat.format(calendar.time)
        releaseAlarmReceiver.setReminderRelease(this, reminder)
    }
    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun dailyReminderTodayNoActive() {
        releaseAlarmReceiver.cancelReminderRelease(this)
    }
}
