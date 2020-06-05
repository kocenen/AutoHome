package com.AutoHome.homework.Activity


import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.AutoHome.homework.Client.Client
import com.AutoHome.homework.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.JsonObject
import com.gun0912.tedpermission.TedPermission
import com.gun0912.tedpermission.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {


    var lat: Double = 0.0
    var lng: Double = 0.0

    fun runtimeEnableAutoInit() {
        FirebaseMessaging.getInstance().isAutoInitEnabled = true
    }

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        val permissionlistener: PermissionListener = object : PermissionListener {
            override fun onPermissionGranted() { }

            override fun onPermissionDenied(deniedPermissions: java.util.ArrayList<String>?) {
                Toast.makeText(this@MainActivity, "권한 거부됨\\n\" ${deniedPermissions.toString()}", Toast.LENGTH_LONG).show()
            }
        }
        TedPermission.with(this)
            .setPermissionListener(permissionlistener)
            .setRationaleMessage("내 위치를 표시하기 위해 장소 권한이 필요합니다")
            .setDeniedMessage("설정] > [권한] 에서 권한을 허용할 수 있습니다.")
            .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .check()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "CHANNEL_ID", "Noti",
                NotificationManager.IMPORTANCE_DEFAULT
            )
        }
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("InstanceId를 불러오지 못했습니다:", task.exception)
                    return@addOnCompleteListener
                }
                val token: String? = task.result?.token
                Log.d("TAG", token)
            }

        val btn_send = findViewById<Button>(R.id.btn_senddata) as Button
        btn_send.setOnClickListener {
            basicWrite(tb_sendval.text.toString())
        }

        val btn_noti = findViewById<Button>(R.id.btn_notification) as Button
        btn_noti.setOnClickListener {
            showNoti(tb_notiTitle.text.toString(), tb_notiBody.text.toString())
        }

        val btn_Weather = findViewById<Button>(R.id.btn_loadWeather) as Button
        btn_Weather.setOnClickListener {
            getCurrentWeather()
        }
    }

    @SuppressLint("MissingPermission")
    fun getCurrentWeather(): String? {
        var lm = getSystemService(LOCATION_SERVICE) as LocationManager?
        val location = lm?.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        lat = location!!.latitude
        lng = location!!.longitude
        Log.d(ContentValues.TAG,"lng $lng lat $lat")
        Log.i("test", "Latitute: $lat ; Longitute: $lng")
        var res: retrofit2.Call<JsonObject> = Client
            .getInstance()
            .buildRetrofit()
            .getCurrentWeather(lat.toString(), lng.toString(), "5973b1320d3b5b0ebea69c825b7d2999")

        var jsonObject: String
        Log.d(ContentValues.TAG, "LOGD: Test SUCSEX")
        res.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d(ContentValues.TAG, "Fail")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                var jsonObj = JSONObject(response.body().toString())
                Log.d(ContentValues.TAG, "LOGD2:" + jsonObj.toString())
                jsonObject = jsonObj.toString()
                val mainTemp = jsonObj.getJSONObject("main")
                var temp: Double = Math.ceil(mainTemp.getDouble("temp") - 273)
                var city: String = jsonObj.getString("name")
                Log.d(ContentValues.TAG, "temp = $temp , city = $city")
                txt_weatherview.text = "$city 의 온도는 $temp 입니다"

            }

        })
        return null;
    }

    fun showNoti(title: String, body: String) {
        var not = getNotificationBuilder("channel1", "1st Channel")
        not.setTicker("Ticker")
        not.setSmallIcon(android.R.drawable.ic_menu_search)

        var bitmap = BitmapFactory.decodeResource(
            resources,
            R.mipmap.ic_launcher
        )
        not.setLargeIcon(bitmap)
        not.setNumber(100)
        not.setAutoCancel(true)
        not.setContentTitle(title)
        not.setContentText(body)

        var notification = not.build()
        var mng = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        mng.notify(10, notification)

    }


    fun getNotificationBuilder(id: String, name: String): NotificationCompat.Builder {
        var not: NotificationCompat.Builder? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            manager.createNotificationChannel(channel)
            not = NotificationCompat.Builder(this, id)

        } else {
            not = NotificationCompat.Builder(this)
        }
        return not
    }

    fun basicWrite(SendData: String) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        myRef.setValue(SendData)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val value = dataSnapshot.getValue(String::class.java)
                Log.d("TAG", "VALUE is $value")
                txt_changedData.text = value
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("TAG", "Failed to read value cause: ${databaseError.toException()}")
            }
        })
    }

    @SuppressLint("MissingPermission")
    fun setLocation(location: Location?) {


    }

}