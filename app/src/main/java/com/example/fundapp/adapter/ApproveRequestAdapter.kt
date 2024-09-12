package com.example.fundapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.RequestApproveItemBinding
import com.example.fundapp.fragments.approverequest.ApproveRequestFragment
import com.example.fundapp.model.TransactionUser

class ApproveRequestAdapter(
    private var requestList: MutableList<TransactionUser>,
    private val listener: ApproveRequestFragment


) : RecyclerView.Adapter<ApproveRequestAdapter.ViewHolder>() {

    class ViewHolder(val binding: RequestApproveItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding =
            RequestApproveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val approveRequest = requestList[position]
        holder.binding.textViewTransactionDate.text = "Date :" + approveRequest.date
        holder.binding.textViewWithdrawAmount.text = "Rs: " + approveRequest.amount.toString()
        holder.binding.textViewUserName.text = approveRequest.name.split(" ")[0]
        holder.binding.textViewWithdrawReason.text = approveRequest.reason

        holder.binding.circularImageView.let {
            Glide.with(holder.itemView.context).load(approveRequest.photoUrl)
                .placeholder(R.drawable.baseline_person_24).into(it)
        }
        holder.binding.buttonApproveRequest.setOnClickListener {
            listener.onAcceptClick(
                approveRequest.transactionId,
                approveRequest.userId,
                approveRequest.amount,
                approveRequest.date
            )
            requestList.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
        }

        holder.binding.buttonRejectRequest.setOnClickListener {
            listener.onRejectClick(approveRequest.transactionId)
            requestList.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
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

interface ApproveRequestAdapterListener {
    fun onAcceptClick(transactionId: String, userId: String, withdrawAmount: Int, date: String)
    fun onRejectClick(transactionId: String)
}