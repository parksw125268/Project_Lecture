package com.kotlin.a05_pictureframe

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import java.util.*

class MainActivity : AppCompatActivity() {
    private val addPhotoButon : Button by lazy {
        findViewById<Button>(R.id.addPhotoButon)
    }
    private val startPhotoFrameModeButton : Button by lazy {
        findViewById<Button>(R.id.startPhotoFrameModeButton)
    }
    private val listImageView : List<ImageView> by lazy{
        mutableListOf<ImageView>().apply{
            add(findViewById(R.id.imageView11))
            add(findViewById(R.id.imageView12))
            add(findViewById(R.id.imageView13))
            add(findViewById(R.id.imageView21))
            add(findViewById(R.id.imageView22))
            add(findViewById(R.id.imageView23))
        }

    }
    val imageUriList : MutableList<Uri> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAddPhotoButton()//버튼을 초기화 해준다. 한눈에 보기좋게 fun 으로 뺀다.
        initStartPhotoFrameModeButton()

    }

    private fun initAddPhotoButton(){
        addPhotoButon.setOnClickListener {
            if (imageUriList.size >= 6){
                Toast.makeText(this,"이미 사진이 꽉 찼습니다.",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            when{
                //권한이 등록되어 있는가?
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED ->{ //권한이 잘 부여되어 있음.
                    //기능
                    navigatePhotos()
                }
                // 권한이 거절되었을 때 교육용 팝업을 띄울 필요가 있는지 체크
                // boolean
                shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) ->{
                    // 교육용 팝업 확인 후 권한팝업을 띄우는 기능
                    showPermissionContextPopup()
                }
                else ->{
                    requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1000)
                }
            }

        }
    }
    private fun initStartPhotoFrameModeButton(){
        startPhotoFrameModeButton.setOnClickListener {
            val intent = Intent(this,PhotoFrameActivity::class.java)
            imageUriList.forEachIndexed { index, uri ->
                intent.putExtra("photo${index}",uri.toString())
            }
            intent.putExtra("photoListSize",imageUriList.size)
            startActivity(intent)
        }

    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            1000 ->{
                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //true : 권한이 부여됨
                    //기능 구현
                    navigatePhotos()
                }else{
                    Toast.makeText(this,"권한을 거부하셨습니다. ",Toast.LENGTH_SHORT).show()
                }
            }
            else ->{}

        }
    }
    private fun navigatePhotos(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent,2000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK){ //취소한경우
            return
        }
        when (requestCode){
            2000 -> {
                val selectedImageUri = data?.data
                if(selectedImageUri == null){
                    Toast.makeText(this,"사진을 가져오지 못했습니다",Toast.LENGTH_SHORT).show()
                }else{
                    imageUriList.add(selectedImageUri)
                    listImageView[imageUriList.size-1].setImageURI(selectedImageUri) // ImageView에 집어넣기
                    Toast.makeText(this,"${imageUriList.size}",Toast.LENGTH_SHORT).show()
                }

            }else -> {
                Toast.makeText(this,"사진을 가져오지 못했습니다.",Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showPermissionContextPopup(){
        AlertDialog.Builder(this)
            .setTitle("권한이 필요합니다.")
            .setMessage("전자액자에 앱에서 사지을 불러오기 위해 권한이 필요합니다. ")
            .setPositiveButton("동의하기") { _, _ ->
                requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1000)
            }
                .setNegativeButton("취소하기") { _, _ -> }
                .create()
                .show()
    }
}