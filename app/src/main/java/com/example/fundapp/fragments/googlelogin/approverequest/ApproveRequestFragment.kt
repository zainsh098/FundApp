package com.example.fundapp.fragments.googlelogin.approverequest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundapp.adapter.ApproveRequestAdapter
import com.example.fundapp.databinding.FragmentApproveRequestBinding
import com.example.fundapp.viewmodel.TransactionViewModel
import com.google.firebase.auth.FirebaseAuth

class ApproveRequestFragment : Fragment() {

    private lateinit var binding: FragmentApproveRequestBinding
    private lateinit var adapter: ApproveRequestAdapter
    private lateinit var transactionViewModel: TransactionViewModel
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApproveRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ApproveRequestAdapter(requireContext(), mutableListOf())
        binding.approveRequestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.approveRequestRecyclerView.adapter = adapter

        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        transactionViewModel.withdrawalRequests.observe(viewLifecycleOwner) { withdrawalRequests ->
            Log.d("ApproveRequestFragment", "Withdrawal Requests: $withdrawalRequests")
            val nonNullHistory = withdrawalRequests.filterNotNull()
            adapter.updateList(nonNullHistory)
        }

        transactionViewModel.getApprovalWithdrawalRequests(auth.currentUser!!.uid)
    }
}
