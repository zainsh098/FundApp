package com.example.fundapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fundapp.databinding.TransactionDetailsItemBinding
import com.example.fundapp.model.TransactionUser

class TransactionAdapter(
    private var transactions: MutableList<TransactionUser>
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.binding.transcationType.text = transaction.type
        holder.binding.transcationID.text = "ID # " + transaction.transactionId
        holder.binding.transcationAmount.text = "Rs: " + transaction.amount.toString()
    }

    override fun getItemCount(): Int = transactions.size

    fun updateList(newTransactions: List<TransactionUser>) {
        transactions.clear()
        transactions.addAll(newTransactions)
        notifyDataSetChanged()
    }
}
