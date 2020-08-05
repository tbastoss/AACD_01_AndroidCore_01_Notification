package com.tab.notifyme

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.tab.notifyme.databinding.ActivityMainBinding

const val PRIMARI_CHANNEL_ID: String = "primary_notification_channel"

class MainActivity : AppCompatActivity() {

    private lateinit var mNotifyManager: NotificationManager

    private lateinit var binding: ActivityMainBinding
    private lateinit var button_notify: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        button_notify = binding.notify
        button_notify.setOnClickListener({ view -> sendNotification() })
    }

    private fun createNotificationChannel() {
        mNotifyManager = getSystemService(
                Context.NOTIFICATION_SERVICE) as NotificationManager
        //Notification Channels are only available in API 26 of higher
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            // Create NotificationChannel
            var notificationChannel = NotificationChannel(
                    PRIMARI_CHANNEL_ID,
                    getString(R.string.noti_channel_name_mascot),
                    NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private fun sendNotification() {
        // TO-DO
    }

}