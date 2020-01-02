package com.huda.submission_5_made.ui.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.huda.submission_5_made.R
import com.huda.submission_5_made.scheduler.AlarmReceiver
import kotlinx.android.synthetic.main.activity_setting.*
import java.text.SimpleDateFormat
import java.util.*

@Suppress("NAME_SHADOWING")
class SettingActivity : AppCompatActivity() {
    private lateinit var alarmReceiver : AlarmReceiver
    companion object {
        const val PREFS_SWITCH = "MyPrefsSwitch"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        supportActionBar?.title = getString(R.string.Setting)
        alarmReceiver = AlarmReceiver()
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
}
