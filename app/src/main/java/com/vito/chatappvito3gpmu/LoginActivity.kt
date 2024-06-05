package com.vito.chatappvito3gpmu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vito.chatappvito3gpmu.databinding.LoginMainBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
