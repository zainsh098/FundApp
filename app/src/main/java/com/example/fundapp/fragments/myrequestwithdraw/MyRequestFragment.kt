package com.example.fundapp.fragments.myrequestwithdraw

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundapp.R
import com.example.fundapp.adapter.MyRequestAdapter
import com.example.fundapp.adapter.MyRequestAdapter.OnClickItemShowBottomSheet
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.constants.TransactionConstant
import com.example.fundapp.databinding.FragmentMyRequestBinding
import com.example.fundapp.extensions.visibility
import com.example.fundapp.fragments.requestdetailsbottom.RequestDetailSheetFragment
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Fragment for displaying the user's withdrawal and deposit requests.
 *
 * Shows a list of requests and allows viewing details in a bottom sheet.
 */
class MyRequestFragment :
    BindingFragment<FragmentMyRequestBinding>(FragmentMyRequestBinding::inflate),
    OnClickItemShowBottomSheet {

    private val myRequestViewModel: MyRequestViewModel by viewModels()
    private  var myRequestAdapter: MyRequestAdapter=MyRequestAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView with the adapter
        binding.apply {
            myRequestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            myRequestRecyclerView.adapter = myRequestAdapter
        }

        // Set up toolbar with title and back button
        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.my_request)
            backArrow.setImageResource(R.drawable.back)
            cardImage.visibility(false)
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_myRequestFragment_to_menuFragment)
            }
        }

        // Observe loading state to show/hide progress bar
        myRequestViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility(isLoading)
        }

        // Observe request history and update the RecyclerView
        myRequestViewModel.getTransactionHistory1.observe(viewLifecycleOwner) { history ->
            val myRequestHistory = history.filterNotNull()
                .filter {
                    it.status == TransactionConstant.KEY_PENDING ||
                            it.status == TransactionConstant.KEY_ACCEPTED ||
                            it.type == TransactionConstant.KEY_WITHDRAW ||
                            it.type == TransactionConstant.KEY_DEPOSIT
                }

            val dateFormat = SimpleDateFormat(getString(R.string.dd_mm_yyyy), Locale.getDefault())
            val sortedHistory = myRequestHistory.sortedByDescending {
                dateFormat.parse(it.date)
            }

            if (sortedHistory.isEmpty()) {
                binding.txtNoData.visibility(true)
            } else {
                binding.txtNoData.visibility(false)
                myRequestAdapter.updateList(sortedHistory)
            }
        }
    }

    /**
     * Show a bottom sheet with the details of the photo.
     */
    override fun showPhoto(position: Int, photoUrl: String?) {
        val bundle = Bundle().apply {
            putString("photoUrl", photoUrl)
        }
        val bottomSheet = RequestDetailSheetFragment().apply {
            arguments = bundle
        }
        bottomSheet.show(parentFragmentManager, RequestDetailSheetFragment::class.java.name)
    }
}
