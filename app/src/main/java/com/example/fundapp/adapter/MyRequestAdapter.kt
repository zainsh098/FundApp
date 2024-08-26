package com.example.fundapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fundapp.databinding.MyRequestItemBinding
import com.example.fundapp.model.TransactionUser

class MyRequestAdapter(
    private val context: Context,
    private var withdrawlRequest: MutableList<TransactionUser>
) : RecyclerView.Adapter<MyRequestAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRequestAdapter.ViewHolder {
        val binding =
            MyRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }

    class ViewHolder(val binding: MyRequestItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: MyRequestAdapter.ViewHolder, position: Int) {
        val withdrawlRequest = withdrawlRequest[position]

        holder.binding.textViewWithdrawAmount.text ="Rs: "+ withdrawlRequest.amount.toString()
        holder.binding.textViewTransactionID.text = "ID#:"+withdrawlRequest.transactionId
        holder.binding.textViewTransactionDate.text = withdrawlRequest.dateWithdraw
        holder.binding.textViewWithdrawlStatus.text = withdrawlRequest.status

    }

    override fun getItemCount(): Int {
        return withdrawlRequest.size
    }

    fun updateList(newWithdrawlRequest: List<TransactionUser>) {

        withdrawlRequest.addAll(newWithdrawlRequest)
        notifyDataSetChanged()
    }


}