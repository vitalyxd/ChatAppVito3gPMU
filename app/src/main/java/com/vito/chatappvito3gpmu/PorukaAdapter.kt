package com.vito.chatappvito3gpmu

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vito.chatappvito3gpmu.databinding.PorukaItemBinding

class PorukaAdapter(private val poruke: List<Poruka>) : RecyclerView.Adapter<PorukaAdapter.PorukaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PorukaViewHolder {
        val binding = PorukaItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PorukaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PorukaViewHolder, position: Int) {
        holder.bind(poruke[position])
    }

    override fun getItemCount(): Int = poruke.size

    class PorukaViewHolder(private val binding: PorukaItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(poruka: Poruka) {
            binding.tvMessage.text = poruka.message
            binding.tvSender.text = poruka.sender
        }
    }
}
