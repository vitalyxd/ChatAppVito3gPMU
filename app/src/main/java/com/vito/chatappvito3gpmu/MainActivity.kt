import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ChildEventListener
import com.vito.chatappvito3gpmu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private val porukeList = mutableListOf<Poruka>()
    private lateinit var adapter: PorukaAdapter
    private val register = Register()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val databaseUrl = "https://chatappvito3gpmu-default-rtdb.europe-west1.firebasedatabase.app/"
        database = Firebase.database(databaseUrl).reference

        adapter = PorukaAdapter(porukeList)
        binding.RV.layoutManager = LinearLayoutManager(this)
        binding.RV.adapter = adapter

        binding.sendButton.setOnClickListener {
            val messageText: String = binding.messageEditText.text.toString().trim()
            val senderText: String = binding.senderEditText.text.toString().trim()

            if (messageText.isNotEmpty() && senderText.isNotEmpty()) {
                val message = Poruka(messageText, senderText)
                sendMessageToFirebase(message)
            }
        }

        binding.registerButton.setOnClickListener {
            val email = binding.editTextEmail.text.toString().trim()
            val password = binding.editTextPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                register.registerUser(email, password) { success, errorMessage ->
                    if (success) {
                        Toast.makeText(this, "Registracija uspješna", Toast.LENGTH_SHORT).show()
                        // todo nakon registracije
                    } else {
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Molimo unesite e-mail i lozinku", Toast.LENGTH_SHORT).show()
            }
        }

        fetchMessagesFromFirebase()
    }

    private fun sendMessageToFirebase(message: Poruka) {
        val messageKey: String? = database.child("messages").push().key

        messageKey?.let {
            database.child("messages").child(it).setValue(message)
                .addOnSuccessListener {
                    binding.messageEditText.text.clear()
                    binding.senderEditText.text.clear()
                }
                .addOnFailureListener {
                    // TODO: Prikazati grešku korisniku
                }
        }
    }

    private fun fetchMessagesFromFirebase() {
        database.child("messages").addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val poruka = snapshot.getValue(Poruka::class.java)
                poruka?.let {
                    porukeList.add(it)
                    adapter.notifyItemInserted(porukeList.size - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {
                // TODO: Prikazati grešku korisniku
            }
        })
    }
}
