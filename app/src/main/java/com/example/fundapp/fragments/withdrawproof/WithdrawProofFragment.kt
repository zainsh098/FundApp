package com.example.fundapp.fragments.withdrawproof

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fundapp.R
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.databinding.FragmentWithdawProofBinding
import com.example.fundapp.viewmodel.TransactionViewModel


class WithdrawProofFragment :
    BindingFragment<FragmentWithdawProofBinding>(FragmentWithdawProofBinding::inflate) {

    private val transactionViewModel: TransactionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val transactionID = arguments?.getString("transactionId")
        val withdrawAmount = arguments?.getString("withdrawAmount")
        val date = arguments?.getString("date")

        // Setting up the toolbar
        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.withdraw_approval)
            backArrow.setImageResource(R.drawable.back)
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_withdrawProofFragment_to_approveRequestFragment)
            }
        }

        // Populating the fields with the passed data
        binding.apply {
            textFieldWithdraw.setText(withdrawAmount)
            textFieldWithdraw.isEnabled = false

            textViewSelectedDate.text = date

            textFieldWithdrawReason.setText(transactionID)
            textFieldWithdrawReason.isEnabled = false



        }


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
            val selectedFileUri = data?.data
            transactionViewModel.fileUriLiveData.value = selectedFileUri
            selectedFileUri?.let {
                binding.cardViewAttachment.findViewById<TextView>(R.id.textViewAttachment).text =
                    getString(R.string.file_attached)
                binding.cardViewAttachment.findViewById<ImageView>(R.id.imageViewAttachment)
                    .setImageResource(R.drawable.attachfile)
            }
        }
    }
}
