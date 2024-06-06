package com.vito.chatappvito3gpmu

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.vito.chatappvito3gpmu.databinding.RegisterMainBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: RegisterMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RegisterMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.buttonRegistracija.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                registerUser(email, password)
            } else {
                Toast.makeText(this, "Molimo unesite email i lozinku", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registracija uspješna
                    Toast.makeText(this, "Registracija uspješna", Toast.LENGTH_SHORT).show()
                    // Možete dodati logiku za preusmjeravanje korisnika na drugu aktivnost ili ekran
                } else {
                    // Registracija neuspješna
                    Toast.makeText(this, "Registracija neuspješna: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
