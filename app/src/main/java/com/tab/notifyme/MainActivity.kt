package com.tab.notifyme

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.core.app.NotificationCompat
import com.tab.notifyme.databinding.ActivityMainBinding

const val PRIMARY_CHANNEL_ID: String = "primary_notification_channel"
const val NOTIFICATION_ID: Int = 0
const val ACTION_UPDATE_NOTIFICATION = "com.tab.notifyme.ACTION_UPDATE_NOTIFICATION"

class MainActivity : AppCompatActivity() {

    private lateinit var mNotifyManager: NotificationManager
    private lateinit var mReceiver: BroadcastReceiver

    private lateinit var binding: ActivityMainBinding

    private lateinit var button_notify: Button
    private lateinit var button_cancel: Button
    private lateinit var button_update: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mReceiver = NotificationReceiver()
        registerReceiver(mReceiver, IntentFilter(ACTION_UPDATE_NOTIFICATION))

        createNotificationChannel()

        button_notify = binding.notify
        button_cancel = binding.cancel
        button_update = binding.update

        setNotificationButtonState(true, false, false)

        button_notify.setOnClickListener { view: View? ->
            val notficationBUilder = getNotificationBuilder()
            sendNotification(notficationBUilder)
        }
        button_cancel.setOnClickListener { view -> cancelNotification() }
        button_update.setOnClickListener { view -> updateNotification() }
    }

    override fun onDestroy() {
        unregisterReceiver(mReceiver)
        super.onDestroy()
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

    private fun getNotificationBuilder(): NotificationCompat.Builder {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val notificationPendingIntent = PendingIntent.getActivity(
            this, NOTIFICATION_ID,
            notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notifyBuilder = NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID)
            .setContentTitle("You've been notified!")
            .setContentText("This is your notification text.")
            .setSmallIcon(R.drawable.ic_android)
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        return notifyBuilder
    }

    private fun sendNotification(notificationBuilder: NotificationCompat.Builder) {
        var updateIntent = Intent(ACTION_UPDATE_NOTIFICATION)
        var updatePendingIntent = PendingIntent.getBroadcast(this, NOTIFICATION_ID, updateIntent, PendingIntent.FLAG_ONE_SHOT)

        notificationBuilder.addAction(R.drawable.ic_update, "Update Notificaion", updatePendingIntent)
        mNotifyManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        setNotificationButtonState(false, true, true)
    }

    private fun cancelNotification() {
        mNotifyManager.cancel(NOTIFICATION_ID)
        setNotificationButtonState(true, false, false)
    }

    private fun updateNotification() {
        val androidImage = BitmapFactory.decodeResource(resources, R.drawable.mascot_1)
        val notificationBuilder = getNotificationBuilder()

        notificationBuilder.setStyle(NotificationCompat.BigPictureStyle()
            .bigPicture(androidImage)
            .setBigContentTitle("Notification Updated!!!")
        )

        sendNotification(notificationBuilder)
        setNotificationButtonState(false, false, true)
    }

    private fun setNotificationButtonState(isNotifyEnabled: Boolean,
                                           isUpdateEnabled: Boolean,
                                           isCancelEnabled: Boolean) {
        button_notify.isEnabled = isNotifyEnabled
        button_update.isEnabled = isUpdateEnabled
        button_cancel.isEnabled = isCancelEnabled
    }

    inner class NotificationReceiver: BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            updateNotification()
        }
    }

}

