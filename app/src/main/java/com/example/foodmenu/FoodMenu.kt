package com.example.foodmenu

import android.graphics.Bitmap
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

class FoodMenu : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var name : Long = 0L
    var image: ByteArray? = null
}