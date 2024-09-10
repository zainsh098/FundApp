package com.example.fundapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.ApproveRequestDepositItemBinding
import com.example.fundapp.model.TransactionUser

class DepositApproveAdapter(
    private val depositRequest: ArrayList<TransactionUser>,
    val listener: OnClickItemListenerDeposit

) : RecyclerView.Adapter<DepositApproveAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding =
            ApproveRequestDepositItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    class ViewHolder(val binding: ApproveRequestDepositItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = depositRequest[position]
        holder.binding.textViewUserName.text = request.name
        holder.binding.textViewDepositAmount.text = "Rs" + request.amount
        holder.binding.textViewTransactionDate.text = "Rs" + request.date
        holder.binding

        holder.binding.circularImageView.let {
            holder.binding.circularImageView.let {
                Glide.with(holder.itemView.context).load(request.photoUrl)
                    .placeholder(R.drawable.baseline_person_24).into(it)
            }

        }

        holder.binding.buttonApproveRequest.setOnClickListener {
            listener.acceptClick(request.transactionId, request.userId, request.amount)
            depositRequest.removeAt(holder.adapterPosition)
            notifyItemRemoved(holder.adapterPosition)
        }

    }

    override fun getItemCount(): Int {
        return depositRequest.size

    }

    fun updateList(newDeposit: List<TransactionUser>) {
        depositRequest.clear()
        depositRequest.addAll(newDeposit)
        notifyDataSetChanged()

    }

    interface OnClickItemListenerDeposit {

        fun acceptClick(transactionId: String, userId: String, depositAmount: Int)
        fun rejectClick(transactionId: String)
    }
}