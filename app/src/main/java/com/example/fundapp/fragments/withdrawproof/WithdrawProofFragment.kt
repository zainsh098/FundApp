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
import com.example.fundapp.viewmodel.TransactionViewModel
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class WithdrawProofFragment :
    BindingFragment<FragmentWithdawProofBinding>(FragmentWithdawProofBinding::inflate) {

    private val transactionViewModel: TransactionViewModel by viewModels()
    private val storage = FirebaseStorage.getInstance()
    private var selectedFileUri: Uri? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val transactionID = arguments?.getString("transactionId")
        val withdrawAmount = arguments?.getString("withdrawAmount")
        val userID = arguments?.getString("userID")
        val date = arguments?.getString("date")

        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.withdraw_approval)
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_withdrawProofFragment_to_approveRequestFragment)
            }
        }

        binding.textFieldWithdraw.setText(withdrawAmount)
        binding.textViewSelectedDate.text = date
        binding.textFieldWithdrawReason.setText(transactionID)

        binding.cardViewAttachment.setOnClickListener {
            pickFile()
        }

        binding.buttonWithdrawApproved.setOnClickListener {
            if (selectedFileUri != null) {
                uploadFileToFirestore(selectedFileUri!!) { fileUrl ->
                    if (transactionID != null) {
                        binding.progressBar.visibility(true)
                        transactionViewModel.updateTransactionProof(transactionID, fileUrl)
                        showToast("Transaction updated successfully.")
                        binding.progressBar.visibility(false)

                        if (userID != null) {
                            transactionViewModel.acceptRequest(
                                transactionID,
                                userID,
                                withdrawAmount.toString().toInt()
                            )
                        }

                        findNavController().navigate(R.id.action_withdrawProofFragment_to_menuFragment)
                    }
                }
            } else {
                showToast("Please attach a proof file.")
            }
        }
    }

    private fun uploadFileToFirestore(fileUri: Uri, onComplete: (String) -> Unit) {
        val storageRef = storage.reference.child("withdraw/${UUID.randomUUID()}")
        val uploadTask = storageRef.putFile(fileUri)

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                onComplete(uri.toString())
            }
        }.addOnFailureListener {
            showToast("Failed to upload file")
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
            selectedFileUri = data?.data
            if (selectedFileUri != null) {
                binding.cardViewAttachment.findViewById<TextView>(R.id.textViewAttachment).text =
                    getString(R.string.file_attached)
                binding.cardViewAttachment.findViewById<ImageView>(R.id.imageViewAttachment)
                    .setImageResource(R.drawable.attachfile)
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


}
