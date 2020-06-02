package com.AutoHome.homework


import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.QuickContactBadge
import androidx.core.app.NotificationBuilderWithBuilderAccessor
import androidx.core.app.NotificationCompat
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
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

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O)
        {
            val notificationChannel = NotificationChannel(
                "CHANNEL_ID", "Noti",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        }
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
                basicWrite(tb_sendval.text.toString())
            }

        val btn_noti = findViewById<Button>(R.id.btn_notification) as Button
        btn_noti.setOnClickListener{
            showNoti(tb_notiTitle.text.toString(),tb_notiBody.text.toString())
        }


    }

    fun showNoti(title:String, body:String)
    {
        var not = getNotificationBuilder("channel1", "1st Channel")
        not.setTicker("Ticker")
        not.setSmallIcon(android.R.drawable.ic_menu_search)

        var bitmap = BitmapFactory.decodeResource(resources,R.mipmap.ic_launcher)
        not.setLargeIcon(bitmap)
        not.setNumber(100)
        not.setAutoCancel(true)
        not.setContentTitle(title)
        not.setContentText(body)

        var notification = not.build()
        var mng = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mng.notify(10,notification)

    }

    fun getNotificationBuilder(id:String,name:String) : NotificationCompat.Builder{
        var not : NotificationCompat.Builder? = null

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            var manager = getSystemService(Context.NOTIFICATION_SERVICE)  as NotificationManager
            var channel = NotificationChannel(id,name,NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
            not = NotificationCompat.Builder(this,id)

        }
        else
        {
            not = NotificationCompat.Builder(this)
        }
        return not
    }

    fun basicWrite(SendData : String)
    {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        myRef.setValue(SendData)
        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java)
                Log.d("TAG","VALUE is $value")
                txt_changedData.text = value

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG","Failed to read value cause: ${databaseError.toException()}")
            }

        })
    }




}
