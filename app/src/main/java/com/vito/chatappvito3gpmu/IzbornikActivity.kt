package com.vito.chatappvito3gpmu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vito.chatappvito3gpmu.databinding.IzbornikMainBinding

class IzbornikActivity : AppCompatActivity() {
    private lateinit var binding: IzbornikMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IzbornikMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonPrikazChata.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.buttonLoginRegister.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.buttonProfileEdit.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
    }
}
