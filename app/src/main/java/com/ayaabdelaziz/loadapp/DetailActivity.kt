package com.ayaabdelaziz.loadapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ayaabdelaziz.loadapp.MainActivity.Companion.DOWNLOAD_STATUS
import com.ayaabdelaziz.loadapp.MainActivity.Companion.FILE_NAME
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        val intent = intent
        val fileName = intent.getStringExtra(FILE_NAME)
        val status = intent.getStringExtra(DOWNLOAD_STATUS)
        Log.d("TAG", "onCreate: $fileName $status")
        txt_file_name.setText(intent.getStringExtra(FILE_NAME).toString())
        txt_download_status.setText(intent.getStringExtra(DOWNLOAD_STATUS).toString())

        btn_ok.setOnClickListener {
            val intent = Intent(this@DetailActivity,MainActivity::class.java)
            startActivity(intent)

        }


    }

}
