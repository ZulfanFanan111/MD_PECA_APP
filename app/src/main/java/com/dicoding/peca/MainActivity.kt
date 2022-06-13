package com.dicoding.peca

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dicoding.peca.databinding.ActivityMainBinding
import android.widget.Toast
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val homeFrag = HomeFragment()
    private val storeFrag = StoreFragment()
    private val historyFrag = HistoryFragment()
    private val profileFrag = ProfileFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionGrant()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRE_PERMISSION,
                REQUEST_CODE_PERMISSION
            )
        }

        binding.btScann.setOnClickListener {
            Intent(this@MainActivity, CameraActivity::class.java).also {
                startActivity(it)
            }
        }
        binding.bottomNavView.background = null
        binding.bottomNavView.menu.getItem(2).isEnabled = false

        makeCurrentFrag(homeFrag)
        binding.bottomNavView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.itHome -> {
                    makeCurrentFrag(homeFrag)
                    return@setOnItemSelectedListener true
                }
                R.id.itStore -> {
                    makeCurrentFrag(storeFrag)
                    return@setOnItemSelectedListener true
                }
                R.id.itHistory -> {
                    makeCurrentFrag(historyFrag)
                    return@setOnItemSelectedListener true
                }
                R.id.itProfile -> {
                    makeCurrentFrag(profileFrag)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
    private fun makeCurrentFrag(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frag_nav, fragment)
            commit()
        }
    }
    private fun allPermissionGrant() = REQUIRE_PERMISSION.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (!allPermissionGrant()) {
                Toast.makeText(
                    this, "Tidak diberikan izin", Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
    companion object{
        const val CAMERA_X_RESULT = 200
        private val REQUIRE_PERMISSION = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSION = 10
    }
}