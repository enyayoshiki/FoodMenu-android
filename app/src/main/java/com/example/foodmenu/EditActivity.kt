package com.example.foodmenu

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.core.graphics.scaleMatrix
import com.squareup.picasso.Picasso
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*


class EditActivity : AppCompatActivity() {
    private lateinit var realm: Realm
     var foodName : String = ""
     var foodImage : String? = ""
     var foodDifficult : String = ""
     var foodTimeCost : String = ""

     val difficultItem = arrayOf("EASY", "NORMAL", "HARD")
     val timecostItem = arrayOf("SHORT", "AVERAGE", "LONG")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        realm = Realm.getDefaultInstance()

        val spinnerDifficult = findViewById<Spinner>(R.id.difficultEdit)
        val adapterDifficult= ArrayAdapter(this, android.R.layout.simple_spinner_item, difficultItem)
        adapterDifficult.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerDifficult.adapter = adapterDifficult

        val spinerTimeCost = findViewById<Spinner>(R.id.timecostEdit)
        val adapterTimeCost = ArrayAdapter(this, android.R.layout.simple_spinner_item, timecostItem)
        adapterTimeCost.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinerTimeCost.adapter = adapterTimeCost

        difficultEdit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    val text = parent?.selectedItem as String
                    foodDifficult = text.toString()
                }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

         timecostEdit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
             override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                 val text01 = parent?.selectedItem as String
                 foodTimeCost = text01.toString()
             }
             override fun onNothingSelected(parent: AdapterView<*>?) {}
         }

        selectImageBtn.setOnClickListener {
            selectPhoto()
        }

        saveBtn.setOnClickListener{
            foodName = nameEdit.text.toString()

            realm.executeTransaction{
                val maxId = realm.where<FoodMenu>().max("id")
                val nextId = (maxId?.toLong() ?: 0L) + 1L
                val foodMenu = realm.createObject<FoodMenu>(nextId)
                foodMenu.name = foodName
                foodMenu.image = foodImage
                foodMenu.difficult = foodDifficult
                foodMenu.timecost = foodTimeCost
            }
            Toast.makeText(applicationContext,"保存しました",Toast.LENGTH_SHORT).show()
        }
    }

     private fun selectPhoto() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*"
        }
        startActivityForResult(intent, READ_REQUEST_CODE)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)


        if (resultCode != RESULT_OK) {
            return
        }
        when (requestCode) {
            READ_REQUEST_CODE -> {
                try {
                    resultData?.data?.also {
                        Picasso.get().load(it).into(imageEdit)
                        scaleMatrix()
                        foodImage = resultData?.data?.toString()
                    }
                } catch (e: Exception) {
                    Toast.makeText(this, "エラーが発生しました", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    companion object {
        private const val READ_REQUEST_CODE: Int = 42
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
