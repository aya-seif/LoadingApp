package com.ayaabdelaziz.loadapp

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.ayaabdelaziz.loadapp.utils.NotificationBuilder
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*


@RequiresApi(Build.VERSION_CODES.N)
class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private lateinit var detailIntent: Intent
    private lateinit var downloadManager: DownloadManager
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationBuilder
    private lateinit var name: String
    private lateinit var downLoadStaus: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        name = "none"
        downLoadStaus = "none"
        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        notificationBuilder = NotificationBuilder()
        createChannel()
        detailIntent = Intent(applicationContext, DetailActivity::class.java)
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        custom_button.setOnClickListener {

            when (radio_group.checkedRadioButtonId) {
                R.id.radioBtn_glide -> {
                    download(URL_GLIDE)
                    name = getString(R.string.glide_image_loading_library_by_bumptech)

                }
                R.id.radioBtn_retrofit -> {
                    name = getString(R.string.radio_text_retrofit)
                    download(URL_RETROFIT)
                }
                R.id.radioBtn_udacity -> {
                    download(URL)
                    name = getString(R.string.loadapp_current_repository_by_udacity)
                }
                else -> {
                    Toast.makeText(this, "Please select file to download", Toast.LENGTH_SHORT)
                        .show()
                }
            }


        }
    }

    private val receiver = object : BroadcastReceiver() {
        @SuppressLint("Range")
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            val cursor =
                downloadManager.query(id?.let { DownloadManager.Query().setFilterById(it) })
            if (cursor.moveToFirst()) {
                val status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    downLoadStaus = getString(R.string.status_success)
                    notificationBuilder.createNotification(this@MainActivity, name, downLoadStaus)
                } else if (status == DownloadManager.STATUS_FAILED) {
                    downLoadStaus = getString(R.string.status_fail)
                    notificationBuilder.createNotification(this@MainActivity, name, downLoadStaus)
                }
            }
            if (downloadID == id) {
                Toast.makeText(
                    this@MainActivity,
                    R.string.notification_description,
                    Toast.LENGTH_SHORT
                )
                    .show()
                custom_button.setBtnStatus(ButtonState.Completed)
            }

        }
    }

    private fun download(url: String) {
        custom_button.setBtnStatus(ButtonState.Loading)
        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }


    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                getString(R.string.channel_id),
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_HIGH
            )

            channel.enableLights(true)
            channel.enableVibration(true)
            channel.description = "Download Files Channel"
            notificationManager = ContextCompat.getSystemService(
                this, NotificationManager::class.java
            ) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }
    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val URL_GLIDE = "https://github.com/bumptech/glide/archive/master.zip"
        private const val URL_RETROFIT = "https://github.com/square/retrofit/archive/master.zip"
        const val DOWNLOAD_STATUS = "downloadStatus"
        const val FILE_NAME = "fileName"
    }

}
