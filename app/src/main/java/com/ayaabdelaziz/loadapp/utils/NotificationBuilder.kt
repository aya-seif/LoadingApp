package com.ayaabdelaziz.loadapp.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ayaabdelaziz.loadapp.DetailActivity
import com.ayaabdelaziz.loadapp.MainActivity
import com.ayaabdelaziz.loadapp.MainActivity.Companion.DOWNLOAD_STATUS
import com.ayaabdelaziz.loadapp.R

class NotificationBuilder {
    private val notification_id = 0

    @RequiresApi(Build.VERSION_CODES.N)
    fun createNotification(context: Context, fileName: String, status: String) {

        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(
            MainActivity.FILE_NAME,
            fileName
        )
        intent.putExtra(DOWNLOAD_STATUS, status)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
           PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        )


        val builder = NotificationCompat.Builder(context, context.getString(R.string.channel_id))
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText(context.getString(R.string.notification_description))
            .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
            .setAutoCancel(true)
            .setOngoing(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_launcher_background, "Check the status", pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        NotificationManagerCompat.from(context).notify(notification_id, builder.build())

    }
}
