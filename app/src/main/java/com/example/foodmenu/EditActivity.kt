package com.example.foodmenu

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*


class EditActivity : AppCompatActivity() {
    private lateinit var realm: Realm
     var foodName = ""
     var foodImage : String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        realm = Realm.getDefaultInstance()

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
