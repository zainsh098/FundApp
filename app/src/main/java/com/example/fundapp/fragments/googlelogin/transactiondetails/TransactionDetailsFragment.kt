package com.example.fundapp.fragments.googlelogin.transactiondetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundapp.adapter.TransactionAdapter
import com.example.fundapp.databinding.FragmentTransactionDetailsBinding
import com.example.fundapp.viewmodel.TransactionViewModel
import com.example.fundapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth


class TransactionDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTransactionDetailsBinding
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var userViewModel: UserViewModel
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

        transactionAdapter = TransactionAdapter(mutableListOf())
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)


        // Observe user IDs
        userViewModel.userIds.observe(viewLifecycleOwner) { ids ->
            ids.forEach { id ->
                // Log each user ID
                Log.d("TransactionDetailsFragment", "User ID: $id")
            }


        }
        userViewModel.getAllUserIds()
        binding.apply {
            recyclerViewTransactionDetails.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewTransactionDetails.adapter = transactionAdapter
        }


        transactionViewModel.transactionHistory.observe(viewLifecycleOwner) { history ->



            val nonNullHistory = history.filterNotNull()
            transactionAdapter.updateList(nonNullHistory)
        }
        transactionViewModel.getTransactionHistory(currentUserId!!)

    }
}