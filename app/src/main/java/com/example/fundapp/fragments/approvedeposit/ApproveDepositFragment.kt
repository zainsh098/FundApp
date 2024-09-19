package com.example.fundapp.fragments.approvedeposit

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundapp.R
import com.example.fundapp.adapter.DepositApproveAdapter
import com.example.fundapp.adapter.DepositApproveAdapter.OnClickItemListenerDeposit
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.databinding.FragmentDepositApproveRequestBinding
import com.example.fundapp.extensions.visibility

/**
 * Fragment for handling deposit approval requests.
 *
 * This fragment displays a list of deposit requests that need to be approved or rejected. It uses
 * a [DepositApproveAdapter] to show the list and handles user interactions for accepting or rejecting
 * requests.
 */
class ApproveDepositFragment :
    BindingFragment<FragmentDepositApproveRequestBinding>(FragmentDepositApproveRequestBinding::inflate),
    OnClickItemListenerDeposit {

    private lateinit var adapter: DepositApproveAdapter
    private val approveDepositViewModel: ApproveDepositViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter and set it to the RecyclerView
        adapter = DepositApproveAdapter(arrayListOf(), this)
        binding.approveRequestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.approveRequestRecyclerView.adapter = adapter

        // Setup toolbar
        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.approvals)
            backArrow.setImageResource(R.drawable.back)
            cardImage.visibility(false)
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_approveDepositFragment_to_menuFragment)
            }
        }

        // Observe ViewModel data
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
            Log.d("ApproveRequestFragment", "Deposit Requests: $depositRequests")
        }

        // Fetch deposit requests from ViewModel
        approveDepositViewModel.getAllDepositRequests()
    }

    /**
     * Handles the acceptance of a deposit request.
     *
     * @param transactionId The ID of the deposit transaction.
     * @param userId The ID of the user making the deposit.
     * @param depositAmount The amount to be deposited.
     */
    override fun acceptClick(transactionId: String, userId: String, depositAmount: Int) {
        approveDepositViewModel.acceptDepositRequest(transactionId, userId, depositAmount)
    }

    /**
     * Handles the rejection of a deposit request.
     *
     * @param transactionId The ID of the deposit transaction to be rejected.
     */
    override fun rejectClick(transactionId: String) {
        approveDepositViewModel.rejectRequest(transactionId)
    }
}
