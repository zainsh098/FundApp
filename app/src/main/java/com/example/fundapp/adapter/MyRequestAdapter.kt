package com.example.fundapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.MyRequestItemBinding
import com.example.fundapp.extensions.visibility
import com.example.fundapp.model.TransactionUser

/**
 * Adapter class for handling the list of user's withdrawal and deposit requests.
 *
 * @property withdrawlRequest A mutable list of TransactionUser objects representing the requests.
 * @property listener An instance of OnClickItemShowBottomSheet to handle the bottom sheet display.
 */
class MyRequestAdapter(

    private var withdrawlRequest: MutableList<TransactionUser>,
    private val listener: OnClickItemShowBottomSheet

) : RecyclerView.Adapter<MyRequestAdapter.ViewHolder>() {

    /**
     * Creates a new ViewHolder instance to hold the view for each request item.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new View.
     * @return A ViewHolder object that holds the layout for each item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            MyRequestItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    /**
     * ViewHolder class to hold and bind the views for each request item.
     *
     * @property binding The binding object for MyRequestItem layout.
     */
    class ViewHolder(val binding: MyRequestItemBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Binds the data (withdrawal/deposit request details) to the ViewHolder.
     *
     * @param holder The ViewHolder that should be updated to represent the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val withdrawlRequest = withdrawlRequest[position]

        holder.binding.apply {
            textViewWithdrawAmount.text = "Rs: ${withdrawlRequest.amount}" // Display withdrawal amount
            textViewTransactionDate.text = withdrawlRequest.date // Display transaction date
            textViewWithdrawlStatus.text = withdrawlRequest.status // Display status of the request

            // Conditionally show/hide the reason based on request type (deposit/withdraw)
            if (withdrawlRequest.type == "deposit") {
                textViewWithdrawReason.visibility(false)
            } else if (withdrawlRequest.type == "withdraw") {
                textViewWithdrawReason.visibility(true)
                textViewWithdrawReason.text = withdrawlRequest.reason
            }

            // Display the request type
            textViewTransactionType.text = withdrawlRequest.type

            // Load user photo using Glide
            circularImageView.let {
                Glide.with(holder.itemView.context).load(withdrawlRequest.photoUrl)
                    .placeholder(R.drawable.baseline_person_24).into(it)
            }

            // Set up onClickListener to show proof of withdrawal or deposit when clicked
            myRequestCardItem.setOnClickListener {
                if (withdrawlRequest.type == "withdraw") {
                    listener.showPhoto(position, withdrawlRequest.proofOfWithdraw)
                } else {
                    listener.showPhoto(position, withdrawlRequest.proofOfDeposit)
                }
            }
        }
    }

    /**
     * Returns the total number of items in the adapter.
     *
     * @return Number of withdrawal/deposit requests in the list.
     */
    override fun getItemCount(): Int {
        return withdrawlRequest.size
    }

    /**
     * Updates the list of requests with new data and refreshes the adapter.
     *
     * @param newWithdrawlRequest The new list of requests to be displayed.
     */
    fun updateList(newWithdrawlRequest: List<TransactionUser>) {
        withdrawlRequest.addAll(newWithdrawlRequest)
        notifyDataSetChanged() // Refresh the entire list
    }

    /**
     * Interface to handle the display of photos (proof of transaction) in a bottom sheet.
     */
    interface OnClickItemShowBottomSheet {
        /**
         * Shows the photo in a bottom sheet when the request item is clicked.
         *
         * @param position The position of the clicked item in the adapter.
         * @param photoUrl The URL of the proof photo (withdrawal or deposit).
         */
        fun showPhoto(position: Int, photoUrl: String?)
    }
}
