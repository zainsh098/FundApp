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
    private val context: Context, private var requestList: MutableList<TransactionUser>
) : RecyclerView.Adapter<ApproveRequestAdapter.ViewHolder>() {
    class ViewHolder(val binding: RequestApproveItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ApproveRequestAdapter.ViewHolder {
        val binding =
            RequestApproveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)


    }

    override fun onBindViewHolder(holder: ApproveRequestAdapter.ViewHolder, position: Int) {

        val approveRequestList = requestList[position]

        holder.binding.textViewTransactionID.text = "Request #: " + approveRequestList.transactionId
        holder.binding.textViewTransactionDate.text = "Date :" + approveRequestList.dateWithdraw
        holder.binding.textViewWithdrawAmount.text = "Rs: " + approveRequestList.amount.toString()
        holder.binding.textViewUserName.text = approveRequestList.name
        holder.binding.textViewWithdrawReason.text = approveRequestList.reason


        holder.binding.circularImageView.let {
            Glide.with(holder.itemView.context).load(approveRequestList.photoUrl)
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