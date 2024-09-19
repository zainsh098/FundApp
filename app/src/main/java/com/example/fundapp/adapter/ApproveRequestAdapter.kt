package com.example.fundapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.RequestApproveItemBinding
import com.example.fundapp.fragments.approverequest.ApproveRequestFragment
import com.example.fundapp.model.TransactionUser

/**
 * Adapter class for handling a list of withdrawal/transaction requests that
 * can be approved or rejected by an admin or authorized user.
 *
 * @property requestList List of TransactionUser objects representing the requests.
 * @property listener Listener for handling approval and rejection actions.
 */
class ApproveRequestAdapter(
    private var requestList: MutableList<TransactionUser>,
    private val listener: ApproveRequestFragment
) : RecyclerView.Adapter<ApproveRequestAdapter.ViewHolder>() {

    /**
     * ViewHolder class to hold the view for each request item.
     *
     * @property binding The binding object for RequestApproveItem layout.
     */
    class ViewHolder(val binding: RequestApproveItemBinding) : RecyclerView.ViewHolder(binding.root)

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new View.
     * @return ViewHolder instance containing the inflated layout.
     */
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val binding =
            RequestApproveItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    /**
     * Binds the data (request details) to the ViewHolder.
     *
     * @param holder The ViewHolder that should be updated to represent the contents of the item.
     * @param position The position of the item in the adapter's data set.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val approveRequest = requestList[position]
        holder.binding.textViewTransactionDate.text = approveRequest.date  // Display transaction date
        holder.binding.textViewWithdrawAmount.text = "Rs ${approveRequest.amount}" // Display amount
        holder.binding.textViewUserName.text = approveRequest.name.split(" ")[0] // Display user first name
        holder.binding.textViewWithdrawReason.text = approveRequest.reason // Display withdrawal reason

        // Load user photo using Glide
        holder.binding.circularImageView.let {
            Glide.with(holder.itemView.context).load(approveRequest.photoUrl)
                .placeholder(R.drawable.baseline_person_24).into(it)
        }

        // Approve button click listener
        holder.binding.buttonApproveRequest.setOnClickListener {
            listener.onAcceptClick(
                approveRequest.transactionId,
                approveRequest.userId,
                approveRequest.amount,
                approveRequest.date
            )
            requestList.removeAt(holder.adapterPosition) // Remove request from the list
            notifyItemRemoved(holder.adapterPosition) // Notify the adapter of the removal
        }

        // Reject button click listener
        holder.binding.buttonRejectRequest.setOnClickListener {
            listener.onRejectClick(approveRequest.transactionId)
            requestList.removeAt(holder.adapterPosition) // Remove request from the list
            notifyItemRemoved(holder.adapterPosition) // Notify the adapter of the removal
        }
    }

    /**
     * Returns the total number of items in the adapter.
     *
     * @return Number of requests in the list.
     */
    override fun getItemCount(): Int {
        return requestList.size
    }

    /**
     * Updates the request list with new data and refreshes the adapter.
     *
     * @param newWithdrawalRequest List of new requests to be displayed.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newWithdrawalRequest: List<TransactionUser>) {
        requestList.clear()
        requestList.addAll(newWithdrawalRequest)
        notifyDataSetChanged() // Refresh the entire list
    }
}

/**
 * Interface to handle approve/reject click events.
 */
interface ApproveRequestAdapterListener {
    /**
     * Called when the approve button is clicked.
     *
     * @param transactionId The transaction ID of the request.
     * @param userId The user ID associated with the request.
     * @param withdrawAmount The amount to be withdrawn.
     * @param date The date of the transaction.
     */
    fun onAcceptClick(transactionId: String, userId: String, withdrawAmount: Int, date: String)

    /**
     * Called when the reject button is clicked.
     *
     * @param transactionId The transaction ID of the request.
     */
    fun onRejectClick(transactionId: String)
}
