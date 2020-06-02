package com.AutoHome.homework

import android.media.session.MediaSession
import android.nfc.Tag
import android.nfc.tech.TagTechnology
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.R.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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
            }

        val btn_send = findViewById<Button>(R.id.btn_senddata) as Button
            btn_send.setOnClickListener {
                BasicReadWrite(tb_sendval.text.toString())
            }

    }

    fun BasicReadWrite(SendData : String)
    {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        myRef.setValue(SendData)

        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java)
                Log.d("TAG","VALUE is $value")

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG","Failed to read value cause: ${databaseError.toException()}")
            }
        })
    }



}
