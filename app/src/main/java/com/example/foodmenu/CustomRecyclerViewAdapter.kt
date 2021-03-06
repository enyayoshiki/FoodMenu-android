package com.example.foodmenu

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import io.realm.RealmResults
import kotlinx.android.synthetic.main.activity_edit.*

class CustomRecyclerViewAdapter(realmResults: RealmResults<FoodMenu>): RecyclerView.Adapter<ViewHolder>() {

    private val rResults: RealmResults<FoodMenu> = realmResults

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.one_result, parent, false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun getItemCount(): Int {
        return rResults.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foodMenu = rResults[position]
        holder.foodName?.text = foodMenu?.name
        holder.foodDifficult?.text = foodMenu?.difficult
        holder.foodTimeCost?.text = foodMenu?.timecost
        Picasso.get().load(Uri.parse(foodMenu?.image)).into(holder.foodImage)

        holder.itemView.setOnClickListener {
            val intent = Intent(it.context, EditActivity::class.java)
            intent.putExtra("id", foodMenu?.id)
            it.context.startActivity(intent)
        }
    }
}
