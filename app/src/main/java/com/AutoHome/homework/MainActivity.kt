package com.AutoHome.homework

import android.media.session.MediaSession
import android.nfc.Tag
import android.nfc.tech.TagTechnology
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.R.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    fun runtimeEnableAutoInit()
    {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if(!task.isSuccessful) {
                    Log.w( "InstanceId를 불러오지 못했습니다:", task.exception)
                    return@addOnCompleteListener
                }
                val token :String? = task.result?.token
                    Log.d("TAG",token)
                    txt_token.setText(token.toString())
            }

    }



}
