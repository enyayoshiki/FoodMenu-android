package com.example.foodmenu

import android.content.ClipData
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import kotlinx.android.synthetic.main.one_result.view.*

class ViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {

        var foodName = itemView.resultName
       // var foodImage = itemView.resultImage

}