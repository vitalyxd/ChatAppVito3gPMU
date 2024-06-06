package com.vito.chatappvito3gpmu

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.vito.chatappvito3gpmu.databinding.EditprofileMainBinding

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: EditprofileMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditprofileMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
