package com.example.fundapp.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fundapp.R
import com.example.fundapp.constants.TransactionConstant
import com.example.fundapp.databinding.TransactionDetailsItemBinding
import com.example.fundapp.model.TransactionUser

/**
 * Adapter class for handling the list of transaction details (withdrawals and deposits).
 */
class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    private var transactions: List<TransactionUser> = emptyList()

    /**
     * ViewHolder class to bind the transaction data to the respective UI elements.
     *
     * @property binding The binding object for TransactionDetailsItem layout.
     */
    class ViewHolder(val binding: TransactionDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds the transaction data to the UI elements (amount, type, transaction ID).
         *
         * @param transactionUser The transaction object that contains the details.
         */
        @SuppressLint("SetTextI18n")
        fun bind(transactionUser: TransactionUser) {
            binding.apply {
                // Set the text color based on the transaction type
                if (transactionUser.type == TransactionConstant.KEY_WITHDRAW) {
                    transcationAmount.setTextColor(Color.RED)
                } else {
                    transcationAmount.setTextColor(Color.GREEN)
                    binding.imageViewDownArrow.setImageResource(R.drawable.uparrow_green)
                }

                // Bind the transaction amount, type, and transaction ID
                transcationAmount.text = "Rs:${transactionUser.amount}"
                transcationType.text = transactionUser.type
                transcationID.text = "ID:${transactionUser.transactionId}"
            }
        }
    }

    /**
     * Creates a new ViewHolder instance to hold the view for each transaction item.
     *
     * @param parent The parent ViewGroup into which the new view will be added.
     * @param viewType The view type of the new View.
     * @return A ViewHolder object that holds the layout for each transaction item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = TransactionDetailsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    /**
     * Binds the data (transaction details) to the ViewHolder.
     *
     * @param holder The ViewHolder that should be updated to represent the item at the given position.
     * @param position The position of the item within the adapter's data set.
     */
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    /**
     * Returns the total number of items in the adapter.
     *
     * @return Number of transactions in the list.
     */
    override fun getItemCount(): Int = transactions.size

    /**
     * Updates the list of transactions with new data and refreshes the adapter.
     *
     * @param newTransactions The new list of transactions to be displayed.
     */
    fun updateList(newTransactions: List<TransactionUser>) {
        transactions = newTransactions
        notifyDataSetChanged() // Refresh the entire list
    }
}
