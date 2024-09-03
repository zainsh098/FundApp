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
        RecyclerView.ViewHolder(binding.root)

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
        val transaction = transactions[position]

        if (transaction.type == "withdraw") {
            holder.binding.transcationAmount.setTextColor(Color.RED)
        } else {
            holder.binding.transcationAmount.setTextColor(Color.GREEN)
            holder.binding.imageViewDownArrow.setImageResource(R.drawable.uparrow_green)
        }

        holder.binding.transcationAmount.text = "Rs: ${transaction.amount}"
        holder.binding.transcationType.text = transaction.type
        holder.binding.transcationID.text = "ID # ${transaction.transactionId}"
    }

    override fun getItemCount(): Int = transactions.size

    fun updateList(newTransactions: List<TransactionUser>) {
        transactions = newTransactions
        notifyDataSetChanged()
    }
}
