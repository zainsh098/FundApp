package com.example.fundapp.fragments.approverequest

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
import com.example.fundapp.adapter.ApproveRequestAdapter
import com.example.fundapp.adapter.ApproveRequestAdapterListener
import com.example.fundapp.databinding.FragmentApproveRequestBinding
import com.example.fundapp.extensions.visibility

class ApproveRequestFragment : Fragment(), ApproveRequestAdapterListener {

    private lateinit var binding: FragmentApproveRequestBinding
    private lateinit var adapter: ApproveRequestAdapter
    private val approveRequestViewModel: ApproveRequestViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentApproveRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ApproveRequestAdapter(mutableListOf(), this)
        binding.approveRequestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.approveRequestRecyclerView.adapter = adapter

        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.approvals)
            backArrow.setImageResource(R.drawable.back)
            cardImage.visibility(false)
            backArrow.setOnClickListener {

                findNavController().navigate(R.id.action_approveRequestFragment_to_menuFragment)

            }
        }
        approveRequestViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility(isLoading)

        }
        approveRequestViewModel.withdrawalRequests.observe(viewLifecycleOwner) { withdrawalRequests ->

            if (withdrawalRequests.isNullOrEmpty()) {
                binding.txtNoData.visibility = View.VISIBLE

            } else {
                binding.txtNoData.visibility = View.GONE
                adapter.updateList(withdrawalRequests)


            }
            Log.d("ApproveRequestFragment", "Withdrawal Requests: $withdrawalRequests")
        }

        approveRequestViewModel.getAllWithdrawRequests()
    }

    override fun onAcceptClick(
        transactionId: String, userId: String, withdrawAmount: Int, date: String
    ) {
        val bundle = Bundle().apply {
            putString("transactionId", transactionId)
            putString("withdrawAmount", withdrawAmount.toString())
            putString("userID", userId)
            putString("date", date)
        }
        findNavController().navigate(
            R.id.action_approveRequestFragment_to_withdrawProofFragment, bundle
        )
    }

    override fun onRejectClick(transactionId: String) {
        approveRequestViewModel.rejectRequest(transactionId)
    }
}
