package com.example.fundapp.fragments.googlelogin.transactiondetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundapp.R
import com.example.fundapp.adapter.TransactionAdapter
import com.example.fundapp.databinding.FragmentTransactionDetailsBinding
import com.example.fundapp.extensions.visibility
import com.example.fundapp.viewmodel.TransactionViewModel
import com.example.fundapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class TransactionDetailsFragment : Fragment() {

    private lateinit var binding: FragmentTransactionDetailsBinding
    private lateinit var transactionAdapter: TransactionAdapter
    private val transactionViewModel: TransactionViewModel by viewModels()
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
        val userId = arguments?.getString("userId")

        Log.d("USER ID  : ", "User ID ${userId}")
        transactionAdapter = TransactionAdapter()

        binding.componentToolbar.apply {

            textToolbar.text = getString(R.string.transaction_details)
            backArrow.setImageResource(R.drawable.back)
            cardImage.visibility(false)
            backArrow.setOnClickListener {



                findNavController().navigate(R.id.action_transactionDetailsFragment_to_homeFragment)
            }


        }
//
//        userViewModel.userIds.observe(viewLifecycleOwner) { ids ->
//            ids.forEach { id ->
//                Log.d("TransactionDetailsFragment", "User ID: $id")
//            }
//        }
//        userViewModel.getAllUserIds()

        binding.apply {
            recyclerViewTransactionDetails.layoutManager = LinearLayoutManager(requireContext())
            recyclerViewTransactionDetails.adapter = transactionAdapter
        }

        transactionViewModel.isLoading.observe(viewLifecycleOwner){
            isLoading->
            binding.progressBar.visibility(isLoading)

        }


        transactionViewModel.transactionHistory.observe(viewLifecycleOwner) { history ->
            Log.d("TransactionDetailsFragment", "Transaction History: $history")
            val nonNullHistory = history.filterNotNull()
            transactionAdapter.updateList(nonNullHistory)
        }
        if (userId != null) {
            transactionViewModel.getTransactionHistory(userId)
        }


    }
}
