package com.example.fundapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.ApproveRequestDepositItemBinding
import com.example.fundapp.model.TransactionUser

/**
 * Adapter class for handling a list of deposit requests that can be approved or rejected.
 *
 * @property depositRequest List of TransactionUser objects representing the deposit requests.
 * @property listener Listener to handle approve/reject actions.
 */
class DepositApproveAdapter(
    private val depositRequest: ArrayList<TransactionUser>,
    private val listener: OnClickItemListenerDeposit
) : RecyclerView.Adapter<DepositApproveAdapter.ViewHolder>() {

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new View.
     * @return ViewHolder instance containing the inflated layout.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ApproveRequestDepositItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    /**
     * ViewHolder class to hold the view for each deposit request item.
     *
     * @property binding The binding object for ApproveRequestDepositItem layout.
     */
    class ViewHolder(val binding: ApproveRequestDepositItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the data (deposit request details) to the ViewHolder.
         *
         * @param transactionUser The TransactionUser object containing request data.
         */
        fun bind(transactionUser: TransactionUser) {
            binding.apply {
                textViewUserName.text = transactionUser.name.split(" ")[0] // Display first name
                textViewDepositAmount.text = "Rs ${transactionUser.amount}" // Display deposit amount
                textViewTransactionDate.text = transactionUser.date // Display transaction date

                // Load user photo using Glide
                circularImageView.let {
                    Glide.with(root.context).load(transactionUser.photoUrl)
                        .placeholder(R.drawable.baseline_person_24).into(it)
                }
            }
        }
    }

    /**
     * Binds the data for the current item to the ViewHolder.
     *
     * @param holder The ViewHolder that should be updated.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = depositRequest[position]
        holder.bind(request) // Bind the current deposit request data

        // Approve button click listener
        holder.binding.buttonApproveRequest.setOnClickListener {
            listener.acceptClick(request.transactionId, request.userId, request.amount)
            depositRequest.removeAt(holder.adapterPosition) // Remove the request from the list
            notifyItemRemoved(holder.adapterPosition) // Notify adapter about item removal
        }

        // Reject button click listener
        holder.binding.buttonRejectRequest.setOnClickListener {
            listener.rejectClick(request.transactionId)
            depositRequest.removeAt(holder.adapterPosition) // Remove the request from the list
            notifyItemRemoved(holder.adapterPosition) // Notify adapter about item removal
        }
    }

    /**
     * Returns the total number of items in the adapter.
     *
     * @return Number of deposit requests in the list.
     */
    override fun getItemCount(): Int {
        return depositRequest.size
    }

    /**
     * Updates the list of deposit requests with new data and refreshes the adapter.
     *
     * @param newDeposit The new list of deposit requests to be displayed.
     */
    fun updateList(newDeposit: List<TransactionUser>) {
        depositRequest.clear()
        depositRequest.addAll(newDeposit)
        notifyDataSetChanged() // Refresh the entire list
    }

    /**
     * Interface to handle approve/reject click events for a deposit request.
     */
    interface OnClickItemListenerDeposit {
        /**
         * Called when the approve button is clicked.
         *
         * @param transactionId The transaction ID of the request.
         * @param userId The user ID associated with the request.
         * @param depositAmount The amount to be deposited.
         */
        fun acceptClick(transactionId: String, userId: String, depositAmount: Int)

        /**
         * Called when the reject button is clicked.
         *
         * @param transactionId The transaction ID of the request.
         */
        fun rejectClick(transactionId: String)
    }
}
