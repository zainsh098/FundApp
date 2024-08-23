package com.example.fundapp.fragments.googlelogin.deposit

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.FragmentDepositBinding
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.viewmodel.TransactionViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.Calendar
import java.util.UUID

class DepositFragment : Fragment() {

    private lateinit var binding: FragmentDepositBinding
    private lateinit var transactionViewModel: TransactionViewModel
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private lateinit var auth: FirebaseAuth
    private var selectedFileUri: Uri? = null
    private var date: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDepositBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)

        binding.componentToolbar.textToolbar.text = getString(R.string.deposit)
        binding.componentToolbar.backArrow.setImageResource(R.drawable.back)

        binding.componentToolbar.apply {
            context?.let {
                Glide.with(it)
                    .load(auth.currentUser?.photoUrl)
                    .placeholder(R.drawable.baseline_person_24)
                    .into(binding.componentToolbar.circularImageView)
            }
            textToolbar.text = getString(R.string.withdraw)
            backArrow.setImageResource(R.drawable.back)
            backArrow.setOnClickListener {

                findNavController().navigate(R.id.action_withdrawFragment_to_menuFragment)

            }


        }

        binding.apply {
            componentToolbar.backArrow.setOnClickListener {

                findNavController().navigate(R.id.action_depositFragment_to_menuFragment)


            }

        }

        binding.cardViewAttachment.setOnClickListener {
            pickFile()
        }

        binding.buttonLogin.setOnClickListener {
            submitDeposit()
        }
        binding.textViewSelectedDate.setOnClickListener {

            selectDate()

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
            selectedFileUri?.let { uri ->
                binding.imageViewSelectedFile.setImageURI(uri)
                binding.imageViewSelectedFile.visibility = View.VISIBLE
            }
        }
    }

    private fun submitDeposit() {
        val depositAmount = binding.textFieldDeposit.text.toString().toInt()

        if (selectedFileUri != null) {
            uploadFileToFirestore(selectedFileUri!!) { fileUrl ->

                val transaction = TransactionUser(
                    transactionId = UUID.randomUUID().toString(),
                    userId = auth.currentUser!!.uid,
                    type = "deposit",
                    amount = depositAmount,
                    date = "",
                    proofOfDeposit = fileUrl,
                    comments = null,
                    reason = null,
                    status = ""
                )

                transactionViewModel.submitDeposit(transaction)
            }
        } else {
            Toast.makeText(requireContext(), "Please select a file", Toast.LENGTH_SHORT).show()
        }
    }

    private fun uploadFileToFirestore(fileUri: Uri, onComplete: (String) -> Unit) {
        val storageRef = storage.reference.child("deposits/${UUID.randomUUID()}")
        val uploadTask = storageRef.putFile(fileUri)

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                onComplete(uri.toString())
            }
        }.addOnFailureListener {
            Toast.makeText(
                requireContext(),
                "Some Exception Caused.File Not Uploaded",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    private fun selectDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.textViewSelectedDate.text = date
            }, year, month, day
        )

        datePickerDialog.show()
    }


}
