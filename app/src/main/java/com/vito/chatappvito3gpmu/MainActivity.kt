package com.vito.chatappvito3gpmu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vito.chatappvito3gpmu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private val porukeList = mutableListOf<Poruka>()
    private lateinit var adapter: PorukaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val databaseUrl = "https://chatappvito3gpmu-default-rtdb.europe-west1.firebasedatabase.app/"
        database = Firebase.database(databaseUrl).reference

        adapter = PorukaAdapter(porukeList)
        binding.porukeRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.porukeRecyclerView.adapter = adapter

        binding.posaljiButton.setOnClickListener {
            val messageText: String = binding.porukaEditText.text.toString().trim()
            val senderText: String = binding.primateljEditText.text.toString().trim()

            if (messageText.isNotEmpty() && senderText.isNotEmpty()) {
                val message = Poruka(messageText, senderText)
                sendMessageToFirebase(message)
            }
        }
        fetchMessagesFromFirebase()
    }

    private fun sendMessageToFirebase(message: Poruka) {
        val messageKey: String? = database.child("messages").push().key

        messageKey?.let {
            database.child("messages").child(it).setValue(message)
                .addOnSuccessListener {
                    binding.porukaEditText.text.clear()
                    binding.primateljEditText.text.clear()
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
