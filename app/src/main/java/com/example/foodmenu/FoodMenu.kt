package com.example.foodmenu

import android.graphics.Bitmap
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class FoodMenu : RealmObject() {
    @PrimaryKey
    var id: Long = 0
    var name : String = ""
    var image: String? = ""
    var difficult :String = ""
    var timecost :String = ""
    var numTextD = 0
    var numTextT = 0
    var resource = ""
    var method = ""
}