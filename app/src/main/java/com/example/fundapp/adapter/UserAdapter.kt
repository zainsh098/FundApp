package com.example.fundapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.UserItemBinding
import com.example.fundapp.extensions.getEmailMasked
import com.example.fundapp.model.User

/**
 * Adapter class for displaying a list of users in a RecyclerView.
 *
 * @param users The list of users to be displayed.
 * @param listener The listener for item click events.
 */
class UserAdapter(
    private val listener: OnItemClickListenerUser
) : RecyclerView.Adapter<UserAdapter.MyViewHolder>() {
    private var users: MutableList<User> = mutableListOf()

    /**
     * Creates a new ViewHolder to hold the view for each user item.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new View.
     * @return A MyViewHolder object that holds the layout for each user item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    /**
     * ViewHolder class to bind the user data to the respective UI elements.
     *
     * @property binding The binding object for UserItem layout.
     */
    class MyViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root) {
        /**
         * Binds the user data to the UI elements (name, email, balances).
         *
         * @param user The user object that contains the details.
         */
        fun bind(user: User) {
            binding.apply {
                userName.text = user.name.split(" ")[0]  // Display the first name only
                emailID.text = user.email.getEmailMasked()  // Mask the email for privacy
                totalDepositedValue.text =
                    user.totalDeposited.toString()  // Display total deposited amount
                totalWithdrawValue.text =
                    user.totalWithdrawAmount.toString()  // Display total withdrawn amount
                userCurrentBalance.text =
                    "Balance: ${user.currentBalance}"  // Display current balance

                // Load the user's photo using Glide
                user.photoUrl?.let { url ->
                    Glide.with(root)
                        .load(url)
                        .placeholder(R.drawable.baseline_person_24)  // Placeholder image
                        .into(circularImageView)
                }
            }
        }
    }

    /**
     * Binds the data (user details) to the ViewHolder.
     *
     * @param holder The ViewHolder that should be updated to represent the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(users[position])
        holder.itemView.setOnClickListener {
            listener.onItemClick(users[position])  // Notify listener when an item is clicked
        }
    }

    /**
     * Returns the total number of items in the adapter.
     *
     * @return Number of users in the list.
     */
    override fun getItemCount(): Int {
        return users.size
    }

    /**
     * Updates the list of users with new data and refreshes the adapter.
     *
     * @param user The new list of users to be displayed.
     */
    fun updateUsers(user: List<User>) {
        users.clear()
        users.addAll(user)
        notifyDataSetChanged()  // Refresh the entire list
    }
}

/**
 * Listener interface for handling click events on user items.
 */
interface OnItemClickListenerUser {
    /**
     * Called when a user item is clicked.
     *
     * @param user The user that was clicked.
     */
    fun onItemClick(user: User)
}
