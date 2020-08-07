package com.tab.notifyme

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.tab.notifyme.databinding.ActivityMainBinding

const val PRIMARY_CHANNEL_ID: String = "primary_notification_channel"
const val NOTIFICATION_ID: Int = 0

class MainActivity : AppCompatActivity() {

    private lateinit var mNotifyManager: NotificationManager

    private lateinit var binding: ActivityMainBinding

    private lateinit var button_notify: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        createNotificationChannel()

        button_notify = binding.notify
        button_notify.setOnClickListener { view -> sendNotification() }
    }

    private fun createNotificationChannel() {
        mNotifyManager = getSystemService(
            Context.NOTIFICATION_SERVICE) as NotificationManager
        //Notification Channels are only available in API 26 of higher
        if (Build.VERSION.SDK_INT
            >= Build.VERSION_CODES.O) {
            // Create NotificationChannel
            val notificationChannel = NotificationChannel(
                PRIMARY_CHANNEL_ID,
                getString(R.string.noti_channel_name_mascot),
                NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription("Notification from Mascot");
            mNotifyManager.createNotificationChannel(notificationChannel);
        }
    }

    private fun getNotificationBuilder(): NotificationCompat.Builder =
        NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle("You've been notified!")
            .setContentText("This is your notification text.")
            .setSmallIcon(R.drawable.ic_android)

    private fun sendNotification() {
        mNotifyManager.notify(NOTIFICATION_ID, getNotificationBuilder().build())
        Toast.makeText(this, "pi", Toast.LENGTH_LONG).show()
    }

}