package com.example.foodmenu

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_edit.*
import java.io.ByteArrayOutputStream

class EditActivity : AppCompatActivity() {
    private lateinit var realm: Realm



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        realm = Realm.getDefaultInstance()

        selectImageBtn.setOnClickListener {
            selectPhoto()
        }
    }
         /* 画像データをByteArrayに変換する

        fun createImageData() : ByteArray {
            val bitmap = (imageEdit.drawable as GlideBitmapDrawable).bitmap
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
            val imageByteArray = baos.toByteArray()
            return imageByteArray
        }*/

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
                    resultData?.data?.also { uri ->
                        val inputStream = contentResolver?.openInputStream(uri)
                        val image = BitmapFactory.decodeStream(inputStream)
                        val imageView = findViewById<ImageView>(R.id.imageEdit)
                        imageView.setImageBitmap(image)
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
