package com.example.fundapp.fragments.withdrawproof

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fundapp.R
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.databinding.FragmentWithdawProofBinding
import com.example.fundapp.extensions.visibility
import com.example.fundapp.viewmodel.WithdrawProofViewModel

class WithdrawProofFragment :
    BindingFragment<FragmentWithdawProofBinding>(FragmentWithdawProofBinding::inflate) {

    private val withdrawProofViewModel: WithdrawProofViewModel by viewModels()
    private var selectedFileUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val transactionID = arguments?.getString("transactionId")
        val withdrawAmount = arguments?.getString("withdrawAmount")
        val userID = arguments?.getString("userID")
        val date = arguments?.getString("date")

        binding.componentToolbar.apply {
            cardImage.visibility(false)
            textToolbar.text = getString(R.string.withdraw_approval)
            backArrow.setImageResource(R.drawable.back)
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_withdrawProofFragment_to_menuFragment)
            }
        }
        binding.apply {
            textFieldWithdraw.setText(withdrawAmount)
            textViewSelectedDate.text = date
            textFieldWithdrawReason.setText(transactionID)
        }



        binding.cardViewAttachment.setOnClickListener {
            pickFile()
        }

        binding.buttonWithdrawApproved.setOnClickListener {
            if (transactionID != null && withdrawAmount != null && userID != null) {
                withdrawProofViewModel.submitWithdraw(
                    selectedFileUri,
                    transactionID,
                    userID,
                    withdrawAmount.toInt()
                )
            } else {
                showToast("Missing information")
            }
        }

        observeViewModel()
    }

    private fun pickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            selectedFileUri = data?.data
            if (selectedFileUri != null) {
                binding.cardViewAttachment.findViewById<TextView>(R.id.textViewAttachment).text =
                    getString(R.string.file_attached)
                binding.cardViewAttachment.findViewById<ImageView>(R.id.imageViewAttachment)
                    .setImageResource(R.drawable.attachfile)
            }
        }
    }

    private fun observeViewModel() {
        withdrawProofViewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility(isLoading)
        }

        withdrawProofViewModel.successMessage.observe(viewLifecycleOwner) { message ->
            showToast(message)
            findNavController().navigate(R.id.action_withdrawProofFragment_to_menuFragment)
        }

        withdrawProofViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            showToast(message)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}
