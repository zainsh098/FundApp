package com.example.fundapp.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fundapp.R
import com.example.fundapp.databinding.TransactionDetailsItemBinding
import com.example.fundapp.model.TransactionUser

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private var transactions: List<TransactionUser> = emptyList()

    class ViewHolder(val binding: TransactionDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(transactionUser: TransactionUser) {
            binding.apply {

                if (transactionUser.type == "withdraw") {
                    transcationAmount.setTextColor(Color.RED)
                } else {
                    transcationAmount.setTextColor(Color.GREEN)
                    binding.imageViewDownArrow.setImageResource(R.drawable.uparrow_green)
                }
                transcationAmount.text = "Rs:${transactionUser.amount}"
                transcationType.text = transactionUser.type
                transcationID.text = "ID:${transactionUser.transactionId}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TransactionDetailsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(transactions[position])
    }

    override fun getItemCount(): Int = transactions.size

    fun updateList(newTransactions: List<TransactionUser>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
}
