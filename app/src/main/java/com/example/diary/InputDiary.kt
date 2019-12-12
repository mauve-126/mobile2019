package com.example.diary


import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.input_diary.*
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.content.ContentResolver
import android.content.Context
import android.content.Intent

import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.net.Uri
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.fragment.app.FragmentActivity
import com.pedro.library.AutoPermissions
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.annotations.NotNull
import java.io.FileInputStream
import java.io.FileOutputStream
//import com.pedro.library.AutoPermissions

import java.text.SimpleDateFormat
import java.util.*


class InputDiary: MainActivity() {

    var imageFileuri:Uri? =  null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.input_diary)

        /*val secondintent:Intent=getIntent();

        val YM =secondintent.getIntArrayExtra("YM")
        val pickeddate =secondintent.getIntExtra("picked",0)
        val sdf = SimpleDateFormat("yyyy MM", Locale.KOREAN)
        val sdf1=SimpleDateFormat("zz", Locale.KOREAN)
        current_Month.text = sdf.format(YM)
        picked_date.text=sdf1.format(pickeddate)
*/

        val inputText  = findViewById<EditText>(R.id.inputText)
        val imageView = findViewById<ImageView>(R.id.imageView)
        val save:Button = findViewById(R.id.save)
        val imgsave:Button = findViewById(R.id.imagesave)
        val image:Button = findViewById(R.id.image)


        val secondintent:Intent=getIntent();

        val pickedYear =secondintent.getIntExtra("Y",0)
        val pick_Y:String=pickedYear.toString()
        current_YEAR.text=pick_Y

        val pickedMonth =secondintent.getIntExtra("M",0)+1
        val pick_M=pickedMonth.toString()

        current_Month.text=pick_M

        val pickeddate =secondintent.getIntExtra("picked",0)
        val pick:String=pickeddate.toString()
        picked_date.text=pick

        var fileName = "$pick_Y$pick_M$pick.txt"
        var imagefileName = "image$pick_Y$pick_M$pick.txt"

        checkedDay(pick_Y, pick_M, pick)
        checkedimage(pick_Y, pick_M, pick)



        image.setOnClickListener {
            openGallery()
            AutoPermissions.Companion.loadAllPermissions(this, 101)

        }
        save.setOnClickListener {
            saveDiary(fileName)
        }
        imagesave.setOnClickListener {
            saveImage(imagefileName, imageFileuri)
        }

    }



    fun openGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT // ACTION_GET_CONTENT - 데이터에서 하나의 콘텐트를 선택하여 반환

        startActivityForResult(intent, 101) // 등번호 101번
    }

    fun setFIleUrI(imagefileUri: Uri?){
        this.imageFileuri = imagefileUri
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101) {
            if (resultCode == Activity.RESULT_OK) {
                val fileUri = data!!.data


                val resolver = contentResolver // 리졸버 획득

                try {
                    val instream = resolver.openInputStream(fileUri!!) // 저장소의 URL에 연결하여 데이터를 가져옴
                    val imgBitmap = BitmapFactory.decodeStream(instream)
                    imageView.setImageBitmap(imgBitmap)
                    setFIleUrI(fileUri)

                    instream!!.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    }

    fun onDenied(requestCode: Int, permissions: Array<String>) {

    }

    fun onGranted(requestCode: Int, permissions: Array<String>) {
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.action_login -> {
                val Intent = Intent(this,com.example.diary.Login::class.java)
                startActivity(Intent)
                return true
            }
            /*
            R.id.action_viewAll -> {
                val Intent2 = Intent(this,com.example.diary.view_all::class.java)
                startActivity(Intent2)
                return true
            }*/
            else -> {
                return super.onOptionsItemSelected(item)
            }
        }
    }

    // 일기 파일 읽기
    private fun checkedDay(year: String, monthOfYear: String, dayOfMonth: String) {

        // 받은 날짜로 날짜 보여주는
        var fileName = "$year$monthOfYear$dayOfMonth.txt"

        // 읽어봐서 읽어지면 일기 가져오고
        // 없으면 catch 그냥 살아? 아주 위험한 생각같다..
        var fis: FileInputStream? = null
        try {
            fis = openFileInput(fileName)
            val fileData = ByteArray(fis!!.available())
            fis.read(fileData)
            fis.close()

            var str = String(fileData)
            // 읽어서 토스트 메시지로 보여줌
            Toast.makeText(applicationContext, "일기 써둔 날", Toast.LENGTH_SHORT).show()
            inputText.setText(str)
            save.setText("수정하기")
        } catch (e: Exception) { // UnsupportedEncodingException , FileNotFoundException , IOException
            // 없어서 오류가 나면 일기가 없는 것 -> 일기를 쓰게 한다.
            Toast.makeText(applicationContext, "일기 없는 날", Toast.LENGTH_SHORT).show()
            inputText.setText("")
            save.setText("새 일기 저장")
            e.printStackTrace()
        }

    }



    // 일기 저장하는 메소드
    @SuppressLint("WrongConstant")
    private fun saveDiary(readDay: String)
    {
        var fos: FileOutputStream? = null

        try {
            fos =
                openFileOutput(readDay, Context.MODE_NO_LOCALIZED_COLLATORS) //MODE_WORLD_WRITEABLE
            val content = inputText.getText().toString()

            // String.getBytes() = 스트링을 배열형으로 변환?
            fos!!.write(content.toByteArray())
            //fos.flush();
            fos!!.close()

            // getApplicationContext() = 현재 클래스.this ?
            Toast.makeText(applicationContext, "일기 저장됨", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) { // Exception - 에러 종류 제일 상위 // FileNotFoundException , IOException
            e.printStackTrace()
            Toast.makeText(applicationContext, "오류오류", Toast.LENGTH_SHORT).show()
        }
    }

    // 사진 파일 읽기
    private fun checkedimage(year: String, monthOfYear: String, dayOfMonth: String) {

        // 받은 날짜로 날짜 보여주는
        var imgfileName = "image$year$monthOfYear$dayOfMonth.txt"

        // 읽어봐서 읽어지면 일기 가져오고
        // 없으면 catch 그냥 살아? 아주 위험한 생각같다..
        var fis: FileInputStream? = null
        try {
            fis = openFileInput(imgfileName)
            val fileData = ByteArray(fis!!.available())
            fis.read(fileData)
            fis.close()

            var image = String(fileData)
            var imageUri:Uri? = Uri.parse(image)


            val imgresolver = contentResolver // 리졸버 획득
            val instream = imgresolver.openInputStream(imageUri!!) // 저장소의 URL에 연결하여 데이터를 가져옴
            val imgBitmap = BitmapFactory.decodeStream(instream)
            imageView.setImageBitmap(imgBitmap)

            // 읽어서 토스트 메시지로 보여줌
            Toast.makeText(applicationContext, "사진 있음", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) { // UnsupportedEncodingException , FileNotFoundException , IOException
            // 없어서 오류가 나면 일기가 없는 것 -> 일기를 쓰게 한다.
            Toast.makeText(applicationContext, "사진 없음", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

    }

//이미지 저장
    @SuppressLint("WrongConstant")
    private fun saveImage(readDay: String, imagefileuri: Uri?)
    {
        var fos: FileOutputStream? = null

        try {
            fos =
                openFileOutput(readDay, Context.MODE_NO_LOCALIZED_COLLATORS) //MODE_WORLD_WRITEABLE
            val content = imagefileuri.toString()

            // String.getBytes() = 스트링을 배열형으로 변환?
            fos!!.write(content.toByteArray())
            //fos.flush();
            fos!!.close()

            // getApplicationContext() = 현재 클래스.this ?
            Toast.makeText(applicationContext, "사진 저장됨", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) { // Exception - 에러 종류 제일 상위 // FileNotFoundException , IOException
            e.printStackTrace()
            Toast.makeText(applicationContext, "오류", Toast.LENGTH_SHORT).show()
        }
    }




}
