package com.example.fundapp.fragments.approverequest

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundapp.R
import com.example.fundapp.adapter.ApproveRequestAdapter
import com.example.fundapp.adapter.ApproveRequestAdapterListener
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.databinding.FragmentApproveRequestBinding
import com.example.fundapp.extensions.visibility

/**
 * Fragment for approving or rejecting withdrawal requests.
 *
 * This fragment displays a list of withdrawal requests that need to be approved or rejected.
 * It uses an [ApproveRequestAdapter] to show the requests and provides functionality to
 * accept or reject them.
 */
class ApproveRequestFragment :
    BindingFragment<FragmentApproveRequestBinding>(FragmentApproveRequestBinding::inflate),
    ApproveRequestAdapterListener {

    private lateinit var adapter: ApproveRequestAdapter
    private val approveRequestViewModel: ApproveRequestViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the adapter and set it to the RecyclerView
        adapter = ApproveRequestAdapter(mutableListOf(), this)
        binding.apply {
            approveRequestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            approveRequestRecyclerView.adapter = adapter
        }

        // Configure the toolbar
        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.approvals)
            backArrow.setImageResource(R.drawable.back)
            cardImage.visibility(false)
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_approveRequestFragment_to_menuFragment)
            }
        }

        // Observe the loading state and withdrawal requests
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

        // Fetch all withdrawal requests
        approveRequestViewModel.getAllWithdrawRequests()
    }

    /**
     * Called when a request is accepted.
     *
     * Navigates to the [WithdrawProofFragment] and passes necessary details through a bundle.
     *
     * @param transactionId ID of the transaction
     * @param userId ID of the user
     * @param withdrawAmount Amount to be withdrawn
     * @param date Date of the request
     */
    override fun onAcceptClick(
        transactionId: String, userId: String, withdrawAmount: Int, date: String
    ) {
        val bundle = Bundle().apply {
            putString(getString(R.string.transactionid), transactionId)
            putString(getString(R.string.withdrawamount), withdrawAmount.toString())
            putString(getString(R.string.userid), userId)
            putString(getString(R.string.date), date)
        }
        findNavController().navigate(
            R.id.action_approveRequestFragment_to_withdrawProofFragment, bundle
        )
    }

    /**
     * Called when a request is rejected.
     *
     * Updates the ViewModel to reject the specified request.
     *
     * @param transactionId ID of the transaction to be rejected
     */
    override fun onRejectClick(transactionId: String) {
        approveRequestViewModel.rejectRequest(transactionId)
    }
}
