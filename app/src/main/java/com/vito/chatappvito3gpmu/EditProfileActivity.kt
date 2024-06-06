package com.vito.chatappvito3gpmu

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.vito.chatappvito3gpmu.databinding.EditprofileMainBinding
import java.util.*

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: EditprofileMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage

    private val PICK_IMAGE_REQUEST = 71
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditprofileMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        fetchUserData()
        fetchUserPassword()

        binding.ButtonIzmjeniSliku.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
        }

        binding.ButtonIzmjeni.setOnClickListener {
            updateProfile()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data
            binding.imageViewProfile.setImageURI(imageUri)
        }
    }

    private fun uploadImage(callback: (String) -> Unit) {
        if (imageUri != null) {
            val ref = storage.reference.child("images/${UUID.randomUUID()}")
            ref.putFile(imageUri!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener { uri ->
                        callback(uri.toString())
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to upload image: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            callback("")
        }
    }

    private fun fetchUserData() {
        val user = auth.currentUser
        if (user != null) {
            binding.editTextEmail.setText(user.email)
        }
    }

    private fun updateProfile() {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextLozinka.text.toString()
        val user = auth.currentUser

        if (user != null && email.isNotEmpty() && password.isNotEmpty()) {
            val credential = EmailAuthProvider.getCredential(user.email!!, password)

            user.reauthenticate(credential)
                .addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        user.updateEmail(email)
                            .addOnCompleteListener { emailUpdateTask ->
                                if (emailUpdateTask.isSuccessful) {
                                    Toast.makeText(this, "Email updated", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Failed to update email: ${emailUpdateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }

                        user.updatePassword(password)
                            .addOnCompleteListener { passwordUpdateTask ->
                                if (passwordUpdateTask.isSuccessful) {
                                    Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Failed to update password: ${passwordUpdateTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                    } else {
                        Toast.makeText(this, "Reauthentication failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        uploadImage { imageUrl ->
            if (imageUrl.isNotEmpty()) {
                saveImageUrlToDatabase(imageUrl)
            }
        }
    }

    private fun fetchUserPassword() {
        val user = auth.currentUser
        if (user != null) {
            // Dohvatite lozinku korisnika ako ju želite prikazati u EditTextu
        }
    }

    private fun saveImageUrlToDatabase(imageUrl: String) {
        val user = auth.currentUser
        val database = FirebaseDatabase.getInstance()
        val userRef = database.reference.child("users").child(user!!.uid)

        val userProfileUpdates = mapOf("profileImageUrl" to imageUrl)
        userRef.updateChildren(userProfileUpdates)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile image updated successfully", Toast.LENGTH_SHORT).show()
                val resultIntent = Intent().apply {
                    putExtra("imageUrl", imageUrl)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Failed to update profile image: ${e.message}",
                    Toast.LENGTH_SHORT
                )
            }
    }
}