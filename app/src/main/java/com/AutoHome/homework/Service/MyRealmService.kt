package com.AutoHome.homework.Service

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class MyRealmService(
    var id : Long = 0,
    var name: String = "",
    var age: String = ""
) : RealmObject() {
}
