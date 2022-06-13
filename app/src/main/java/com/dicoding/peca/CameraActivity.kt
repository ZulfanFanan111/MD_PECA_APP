package com.dicoding.peca

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import android.view.Surface
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import com.dicoding.peca.databinding.ActivityCameraBinding
import java.io.File
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import com.dicoding.peca.Constant

class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private var imgCapture: ImageCapture?= null
    private lateinit var hasilDir: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener {
            Intent(this@CameraActivity, MainActivity::class.java).also {
                startActivity(it)
            }
        }
        hasilDir= getOutputDir()
        binding.btnCapture.setOnClickListener { takeFoto() }
        binding.btnBack.setOnClickListener { closeCamera() }

        if(allPermissionGrant()){
            openCamera()
        } else {
            ActivityCompat.requestPermissions(this, Constant.REQ_PERMISSION, Constant.REQ_CODE_PERMISSION)
        }

    }
    private fun getOutputDir(): File{
        val fotoDir = this.externalMediaDirs?.firstOrNull()?.let { itFile ->
            File(itFile, "PECA").apply {
                mkdirs()
            }
        }
        return if (fotoDir!= null && fotoDir.exists())
            fotoDir else filesDir
    }

    private fun closeCamera() {
        Intent(this@CameraActivity, MainActivity::class.java).also {
            startActivity(it)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constant.REQ_CODE_PERMISSION){
            if (allPermissionGrant()){
                openCamera()
            } else {
                Toast.makeText(this,
                    "izin tidak diberikan",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionGrant()=
        Constant.REQ_PERMISSION.all{
            ContextCompat.checkSelfPermission(
                baseContext, it
            ) == PackageManager.PERMISSION_GRANTED
        }


    public override fun onResume() {
        super.onResume()
        hideUI()
        openCamera()
    }

    private fun takeFoto(){
        val imgCapture = imgCapture?: return
//        val nameFile = getImgString(15)
        val fileFoto = File(
            hasilDir,
            SimpleDateFormat(Constant.FILE_NAMA_FORMAT, Locale.getDefault())
                .format(System.currentTimeMillis())+ ".jpg"
        )
        val hasilOption = ImageCapture.OutputFileOptions.Builder(fileFoto).build()
        imgCapture.takePicture(
            hasilOption,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val saveUri= Uri.fromFile(fileFoto)
                    val des = "Foto disimpan"
                    Toast.makeText(this@CameraActivity, "$des", Toast.LENGTH_SHORT).show()
                    val inten = Intent(this@CameraActivity, HistoryFragment::class.java)
                    inten.putExtra(Constant.TAGUri, saveUri.toString())
                    startActivity(inten)
                    finish()
                }
                override fun onError(exception: ImageCaptureException) {
                    Log.e(Constant.TAG, "Error ${exception.message}", exception)
                }
            }
        )
    }

    private fun openCamera(){
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also { Prev ->
                    Prev.setSurfaceProvider(
                        binding.cameraFinder.surfaceProvider
                    )
                }
            imgCapture= ImageCapture.Builder()
                .build()
            val camSelect = CameraSelector.DEFAULT_BACK_CAMERA
            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, camSelect, preview, imgCapture
                )
            } catch (e: Exception) {
                Log.e(Constant.TAG, "buka kamera gagal", e)
            }
        }, ContextCompat.getMainExecutor(this))
    }
    private fun hideUI(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}
