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
import androidx.core.view.get
import com.squareup.picasso.Picasso
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_edit.view.*


class EditActivity : AppCompatActivity() {
    private lateinit var realm: Realm
    var foodImage: String? = ""
    var foodDifficult: String = ""
    var foodTimeCost: String = ""
    val difficultItem = arrayOf("EASY", "NORMAL", "HARD")
    var arrayTextD = 0
    val timecostItem = arrayOf("SHORT", "AVERAGE", "LONG")
    var arrayTextT = 0



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        realm = Realm.getDefaultInstance()


        val spinerDifficult = findViewById<Spinner>(R.id.difficultEdit)
        val adapterDifficult = ArrayAdapter(this, android.R.layout.simple_spinner_item, difficultItem)
        adapterDifficult.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinerDifficult.adapter = adapterDifficult


        val spinerTimeCost = findViewById<Spinner>(R.id.timecostEdit)
        val adapterTimeCost = ArrayAdapter(this, android.R.layout.simple_spinner_item, timecostItem)
        adapterTimeCost.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinerTimeCost.adapter = adapterTimeCost



        val bpId = intent.getLongExtra("id", 0L)
        if (bpId > 0L) {
            val foodMenu = realm.where<FoodMenu>().equalTo("id", bpId).findFirst()

            nameEdit.setText(foodMenu?.name)
            if (foodMenu != null) {
                difficultEdit.setSelection(foodMenu.numTextD)
                timecostEdit.setSelection(foodMenu.numTextT)
            }
            foodresourceEdit.setText(foodMenu?.resource)
            cookmethodEdit.setText(foodMenu?.method)

        } else { deleteBtn.visibility = View.INVISIBLE }


        difficultEdit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val text = parent?.selectedItem as String
                arrayTextD = position
                foodDifficult = text.toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        timecostEdit.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val text01 = parent?.selectedItem as String
                arrayTextT = position
                foodTimeCost = text01.toString()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        selectImageBtn.setOnClickListener {
            selectPhoto()
        }


        when (bpId) {
            0L -> {
                saveBtn.setOnClickListener {
                    realm.executeTransaction {
                        val maxId = realm.where<FoodMenu>().max("id")
                        val nextId = (maxId?.toLong() ?: 0L) + 1L
                        val foodMenu = realm.createObject<FoodMenu>(nextId)
                        foodMenu?.name = nameEdit.text.toString()
                        foodMenu?.image = foodImage
                        foodMenu?.difficult = foodDifficult
                        foodMenu?.timecost = foodTimeCost
                        foodMenu?.numTextD = arrayTextD
                        foodMenu?.numTextT = arrayTextT
                        foodMenu?.resource = foodresourceEdit.text.toString()
                        foodMenu?.method = cookmethodEdit.text.toString()
                    }
                    Toast.makeText(applicationContext, "保存しました", LENGTH_SHORT).show()
                }
            }
            else -> {
                saveBtn.setOnClickListener {

                    realm.executeTransaction {
                        val foodMenu = realm.where<FoodMenu>().equalTo("id", bpId).findFirst()
                        foodMenu?.name = nameEdit.text.toString()
                        foodMenu?.image = foodImage
                        foodMenu?.difficult = foodDifficult
                        foodMenu?.timecost = foodTimeCost
                        foodMenu?.numTextD = arrayTextD
                        foodMenu?.numTextT = arrayTextD
                        foodMenu?.resource = foodresourceEdit.text.toString()
                        foodMenu?.method = cookmethodEdit.text.toString()
                    }
                    Toast.makeText(applicationContext, "保存しました", LENGTH_SHORT).show()
                }
            }
        }
        deleteBtn.setOnClickListener {
            realm.executeTransaction{
                val foodMenu = realm.where<FoodMenu>()
                    .equalTo("id", bpId)
                    ?.findFirst()
                    ?.deleteFromRealm()
            }
            Toast.makeText(applicationContext, "削除しました", LENGTH_SHORT).show()
            finish()
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

