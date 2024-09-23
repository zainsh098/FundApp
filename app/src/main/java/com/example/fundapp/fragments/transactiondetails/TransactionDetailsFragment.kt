package com.example.fundapp.fragments.transactiondetails

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundapp.R
import com.example.fundapp.adapter.TransactionAdapter
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.constants.TransactionConstant
import com.example.fundapp.databinding.FragmentTransactionDetailsBinding
import com.example.fundapp.extensions.visibility
import com.example.fundapp.viewmodel.TransactionViewModel

class TransactionDetailsFragment :
    BindingFragment<FragmentTransactionDetailsBinding>(FragmentTransactionDetailsBinding::inflate) {

    private var transactionAdapter: TransactionAdapter = TransactionAdapter()
    private val transactionViewModel: TransactionViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = arguments?.getString("userId")

        Log.d("USER ID  : ", "User ID ${userId}")
//        transactionAdapter = TransactionAdapter()

        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.transaction_details)
            backArrow.setImageResource(R.drawable.back)
            cardImage.visibility(false)
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_transactionDetailsFragment_to_homeFragment)
            }
        }

        binding.apply {
            recyclerViewTransactionDetails.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewTransactionDetails.adapter = transactionAdapter
        }

        transactionViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        transactionViewModel.transactionHistory.observe(viewLifecycleOwner) { history ->
            Log.d("Trancation ", history.toString())
            val filterHistory = history.filterNotNull()
                .filter {
                    (it.type == TransactionConstant.KEY_WITHDRAW || it.type == TransactionConstant.KEY_DEPOSIT) &&
                            it.status == TransactionConstant.KEY_ACCEPTED
                }
                .sortedByDescending {
                    it.date
                }
            if (filterHistory.isEmpty()) {
                binding.txtNoData.visibility(true)
            } else {

                binding.txtNoData.visibility(false)
                transactionAdapter.updateList(filterHistory)
            }
        }

        if (userId != null) {
            transactionViewModel.getTransactionHistory(userId)
        }
    }
}
