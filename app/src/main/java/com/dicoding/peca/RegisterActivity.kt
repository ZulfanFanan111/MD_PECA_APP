package com.dicoding.peca

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.dicoding.peca.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var cToolbar: Toolbar
    private lateinit var txToolbar: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar?.hide()
        cToolbar("Sign Un")

        binding.tvLogin.setOnClickListener {
            intentKeLogin()
        }
    }
    private fun cToolbar(des: String){
        cToolbar = findViewById(R.id.toolbar)
        txToolbar = findViewById(R.id.toolbarTitle)
        setSupportActionBar(cToolbar)
        txToolbar.text = des
    }
    private fun intentKeLogin(){
        Intent(this@RegisterActivity, LoginActivity::class.java).also {
            startActivity(it)
        }
    }
}