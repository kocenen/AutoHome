package com.AutoHome.homework.Activity

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.AutoHome.homework.R
import com.AutoHome.homework.Service.MyRealmService
import io.realm.Realm
import io.realm.RealmResults
import io.realm.exceptions.RealmException
import kotlinx.android.synthetic.main.activity_edit.*
import java.util.*

class EditActivity:AppCompatActivity()
{

    val realm= Realm.getDefaultInstance() //인스턴스 얻기

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val btn_regi = findViewById<Button>(R.id.btn_Register) as Button
        btn_regi.setOnClickListener {
            insertName()
        }
        val btn_show : Button = findViewById<Button>(R.id.btn_ShowData) as Button
        btn_show.setOnClickListener {
            showRealmData()
        }
        val btn_clear : Button = findViewById<Button>(R.id.btn_ClearRealm) as Button
        btn_clear.setOnClickListener {
            cleanRealmData()
        }
    }
    fun cleanRealmData()
    {

    }
    fun showRealmData()
    {
        val database : RealmResults<MyRealmService> = realm.where(MyRealmService::class.java).findAll()
        txt_RealData.text = database.toString()
        Log.d(ContentValues.TAG,database.toString())
    }

    private fun insertName(){
        Log.d(ContentValues.TAG ,"Insert Function Has been loaded")
        try{
            realm.beginTransaction()
            val currentID = realm.where(MyRealmService::class.java).max("id")

            val newObj:MyRealmService = realm.createObject(MyRealmService::class.java)
            newObj.age =  tb_EditAge.toString()
            newObj.name = tb_EditName.toString()

            realm.commitTransaction()
            Log.d(ContentValues.TAG ,"INSERT COMPLETE")

        }
        catch(e: RealmException)
        {
            Log.d(ContentValues.TAG,e.printStackTrace().toString())
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close() //인스턴스해제
    }

}


