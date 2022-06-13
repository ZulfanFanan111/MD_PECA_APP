package com.dicoding.peca

import android.content.ClipDescription
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.dicoding.peca.databinding.ActivityLoginBinding
import com.dicoding.peca.databinding.ActivitySplashScreenBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var cToolbar: Toolbar
    private lateinit var txToolbar: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actionBar?.hide()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        cToolbar("Sign In")

        binding.btLogin.setOnClickListener {
            intentKeMain()
        }
        binding.tvDaftar.setOnClickListener {
            intentKeRegister()
        }
    }
    private fun cToolbar(des: String){
        cToolbar = findViewById(R.id.toolbar)
        txToolbar = findViewById(R.id.toolbarTitle)
        setSupportActionBar(cToolbar)
        txToolbar.text = des
    }
    private fun intentKeMain(){
        Intent(this@LoginActivity, MainActivity::class.java).also {
            startActivity(it)
        }
    }
    private fun intentKeRegister(){
        Intent(this@LoginActivity, RegisterActivity::class.java).also {
            startActivity(it)
        }
    }
}