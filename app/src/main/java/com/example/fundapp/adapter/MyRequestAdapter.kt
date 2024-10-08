package com.example.fundapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.MyRequestItemBinding
import com.example.fundapp.model.TransactionUser

class MyRequestAdapter(

    private var withdrawlRequest: MutableList<TransactionUser>,
    private val listener: OnClickItemShowBottomSheet

) : RecyclerView.Adapter<MyRequestAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MyRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)

    }

    class ViewHolder(val binding: MyRequestItemBinding) : RecyclerView.ViewHolder(binding.root)

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val withdrawlRequest = withdrawlRequest[position]

        holder.binding.textViewWithdrawAmount.text = "Rs: " + withdrawlRequest.amount.toString()
        holder.binding.textViewTransactionDate.text = withdrawlRequest.date
        holder.binding.textViewWithdrawlStatus.text = withdrawlRequest.status
        holder.binding.textViewWithdrawReason.text = withdrawlRequest.reason
        holder.binding.textViewTransactionType.text = withdrawlRequest.type
        holder.binding.circularImageView.let {
            Glide.with(holder.itemView.context).load(withdrawlRequest.photoUrl)
                .placeholder(R.drawable.baseline_person_24).into(it)
        }
        holder.binding.myRequestCardItem.setOnClickListener {
            if (withdrawlRequest.type == "withdraw") {
                listener.showPhoto(position, withdrawlRequest.proofOfWithdraw)

            } else {

                listener.showPhoto(position, withdrawlRequest.proofOfDeposit)

            }
        }
    }

    override fun getItemCount(): Int {
        return withdrawlRequest.size
    }


    fun updateList(newWithdrawlRequest: List<TransactionUser>) {
        withdrawlRequest.addAll(newWithdrawlRequest)
        notifyDataSetChanged()
    }

    interface OnClickItemShowBottomSheet {
        fun showPhoto(position: Int, photoUrl: String?)
    }
}