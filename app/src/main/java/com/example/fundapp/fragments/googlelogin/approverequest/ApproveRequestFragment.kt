package com.example.fundapp.fragments.googlelogin.approverequest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundapp.adapter.ApproveRequestAdapter
import com.example.fundapp.adapter.ApproveRequestAdapterListener
import com.example.fundapp.databinding.FragmentApproveRequestBinding
import com.example.fundapp.viewmodel.TransactionViewModel

class ApproveRequestFragment : Fragment(), ApproveRequestAdapterListener {

    private lateinit var binding: FragmentApproveRequestBinding
    private lateinit var adapter: ApproveRequestAdapter
    private val transactionViewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentApproveRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ApproveRequestAdapter(mutableListOf(), this)
        binding.approveRequestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.approveRequestRecyclerView.adapter = adapter

        transactionViewModel.withdrawalRequests.observe(viewLifecycleOwner) { withdrawalRequests ->
            Log.d("ApproveRequestFragment", "Withdrawal Requests: $withdrawalRequests")
            adapter.updateList(withdrawalRequests)
        }

        transactionViewModel.getAllWithdrawRequests()
    }

    override fun onAcceptClick(transactionId: String) {
        transactionViewModel.acceptRequest(transactionId)

    }
    override fun onRejectClick(transactionId: String) {
        transactionViewModel.rejectRequest(transactionId)
    }

}
