package com.example.fundapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.UserItemBinding
import com.example.fundapp.extensions.getEmailMasked
import com.example.fundapp.model.User

class UserAdapter(
    private var users: MutableList<User>,
    private val listener: OnItemClickListenerUser
) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.apply {
                userName.text = user.name.split(" ")[0]
                emailID.text = user.email.getEmailMasked()
                totalDepositedValue.text = user.totalDeposited.toString()
                totalWithdrawValue.text = user.totalWithdrawAmount.toString()
                userCurrentBalance.text = "Balance:" + user.currentBalance.toString()

                user.photoUrl?.let { url ->
                    Glide.with(root)
                        .load(user.photoUrl)
                        .placeholder(R.drawable.baseline_person_24)
                        .into(circularImageView)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(users[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(users[position])
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateUsers(user: List<User>) {
        users.clear()
        users.addAll(user)
        notifyDataSetChanged()

    }


}

interface OnItemClickListenerUser {

    fun onItemClick(user: User)

}