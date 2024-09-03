package com.example.fundapp.fragments.googlelogin.transactiondetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundapp.adapter.TransactionAdapter
import com.example.fundapp.databinding.FragmentTransactionDetailsBinding
import com.example.fundapp.viewmodel.TransactionViewModel
import com.example.fundapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class TransactionDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTransactionDetailsBinding
    private lateinit var transactionAdapter: TransactionAdapter
    private val transactionViewModel: TransactionViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTransactionDetailsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val currentUserId = auth.currentUser?.uid

        binding.recyclerViewTransactionDetails.layoutManager = LinearLayoutManager(requireContext())


        transactionViewModel.transactionHistory.observe(viewLifecycleOwner) { history ->
            Log.d("TransactionDetailsFragment", "Transaction History: $history")

            val nonNullHistory = history.filterNotNull()
            if (::transactionAdapter.isInitialized) {
                transactionAdapter.updateList(nonNullHistory)
            } else {
                transactionAdapter = TransactionAdapter(nonNullHistory.toMutableList())
                binding.recyclerViewTransactionDetails.adapter = transactionAdapter
            }
        }
        if (currentUserId != null) {
            transactionViewModel.getTransactionHistory(currentUserId)
        }
    }
}
