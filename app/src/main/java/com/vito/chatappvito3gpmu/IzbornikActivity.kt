package com.vito.chatappvito3gpmu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.vito.chatappvito3gpmu.databinding.IzbornikMainBinding

class IzbornikActivity : AppCompatActivity() {
    private lateinit var binding: IzbornikMainBinding
    private lateinit var auth: FirebaseAuth

    private val EDIT_PROFILE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IzbornikMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.textViewUserEmail.text = currentUser.email
        }

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
            startActivityForResult(intent, EDIT_PROFILE_REQUEST)
        }

        binding.buttonLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.buttonPrikazRazgovora.setOnClickListener {
            val intent = Intent(this, PrikazRazgovoraActivity::class.java)
            startActivity(intent)
        }


    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == Activity.RESULT_OK) {
            val imageUrl = data?.getStringExtra("imageUrl")
            if (imageUrl != null) {
                // Ovdje ažurirajte sliku u izborniku, na primjer:
                // Glide.with(this).load(imageUrl).into(binding.imageViewProfile)
            }
        }
    }
}
