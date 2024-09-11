package com.example.fundapp.fragments.approvedeposit

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundapp.R
import com.example.fundapp.adapter.ApproveRequestAdapter
import com.example.fundapp.adapter.DepositApproveAdapter
import com.example.fundapp.adapter.DepositApproveAdapter.OnClickItemListenerDeposit
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.databinding.FragmentDepositApproveRequestBinding
import com.example.fundapp.extensions.visibility

class ApproveDepositFragment :
    BindingFragment<FragmentDepositApproveRequestBinding>(FragmentDepositApproveRequestBinding::inflate),
    OnClickItemListenerDeposit {

    private lateinit var adapter: DepositApproveAdapter
    private val approveDepositViewModel: ApproveDepositViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = DepositApproveAdapter(arrayListOf(), this)
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
        approveDepositViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility(isLoading)

        }
        approveDepositViewModel.depositRequests.observe(viewLifecycleOwner) { depositRequests ->

            if (depositRequests.isNullOrEmpty()) {
                binding.txtNoData.visibility = View.VISIBLE

            } else {
                binding.txtNoData.visibility = View.GONE
                adapter.updateList(depositRequests)
            }
            Log.d("ApproveRequestFragment", "Withdrawal Requests: $depositRequests")
        }

        approveDepositViewModel.getAllDepositRequests()
    }

    override fun acceptClick(transactionId: String, userId: String, depositAmount: Int) {
        approveDepositViewModel.acceptDepositRequest(transactionId, userId, depositAmount)

    }
    override fun rejectClick(transactionId: String) {
        approveDepositViewModel.rejectRequest(transactionId)
    }
}
