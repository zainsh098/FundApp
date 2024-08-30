package com.example.fundapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.UserItemBinding
import com.example.fundapp.model.User

class UserAdapter(private var users: MutableList<User>) :
    RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAdapter.MyViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    class MyViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: UserAdapter.MyViewHolder, position: Int) {

        users[position].let {

            holder.binding.userName.text = it.name
            holder.binding.emailID.text = getEmailMasked(it.email)
            holder.binding.totalDepositedValue.text = it.totalDeposited.toString()
            holder.binding.totalWithdrawValue.text = it.totalWithdrawAmount.toString()

            it.photoUrl?.let { url ->
                Glide.with(holder.itemView.context)
                    .load(it.photoUrl)
                    .placeholder(R.drawable.baseline_person_24)
                    .into(holder.binding.circularImageView)
            }
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


    fun getEmailMasked(email: String): String {
        val emailParts = email.split("@")
        val localPart = emailParts[0]
        val domain = emailParts[1]

        val maskedLocalPart =
            localPart.substring(0, 2) + "***" + localPart.substring(localPart.length - 2)

        return "$maskedLocalPart@$domain"
    }


}
