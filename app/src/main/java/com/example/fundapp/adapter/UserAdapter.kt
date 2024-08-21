package com.example.fundapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.UserItemBinding
import com.example.fundapp.model.User

class UserAdapter(private val context: Context, private val users: MutableList<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.binding.cardUserName.text = user.name
//        holder.binding.cardCurrentBalance.text = "Rs: ${user.currentBalance}"
        holder.binding.textViewTotalDeposit.text = "Rs: ${user.totalDeposited}"
        holder.binding.textViewtotalWithdraw.text = "Rs: ${user.totalWithdrawAmount}"

        user.photoUrl?.let { url ->
            Glide.with(context)
                .load(url)
                .placeholder(R.drawable.deposit)
//                .into(holder.binding.circularImageView)
        }
    }

    override fun getItemCount(): Int {
        return users.size
    }

    fun updateList(newList: List<User>) {
        users.clear()
        users.addAll(newList)
        notifyDataSetChanged()
    }
}