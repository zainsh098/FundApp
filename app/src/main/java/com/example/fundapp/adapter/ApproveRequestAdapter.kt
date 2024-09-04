package com.example.fundapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.RequestApproveItemBinding
import com.example.fundapp.model.TransactionUser
class ApproveRequestAdapter(
    private val context: Context,
    private var requestList: MutableList<TransactionUser>
) : RecyclerView.Adapter<ApproveRequestAdapter.ViewHolder>() {

    class ViewHolder(val binding: RequestApproveItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ApproveRequestAdapter.ViewHolder {
        val binding = RequestApproveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val approveRequest = requestList[position]
        holder.binding.textViewTransactionDate.text = "Date :" + approveRequest.dateWithdraw
        holder.binding.textViewWithdrawAmount.text = "Rs: " + approveRequest.amount.toString()
        holder.binding.textViewUserName.text = approveRequest.name
        holder.binding.textViewWithdrawReason.text = approveRequest.reason

        holder.binding.circularImageView.let {
            Glide.with(holder.itemView.context).load(approveRequest.photoUrl)
                .placeholder(R.drawable.baseline_person_24).into(it)
        }
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    fun updateList(newWithdrawalRequest: List<TransactionUser>) {
        requestList.clear()
        requestList.addAll(newWithdrawalRequest)
        notifyDataSetChanged()
    }
}
