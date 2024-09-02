package com.example.fundapp.fragments.googlelogin.deposit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.FragmentDepositBinding

class DepositFragment : Fragment() {

    private lateinit var binding: FragmentDepositBinding
    private val depositViewModel: DepositViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDepositBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.deposit)
            backArrow.setImageResource(R.drawable.back)
            context?.let {
                Glide.with(it)
                    .load(depositViewModel.auth.currentUser?.photoUrl)
                    .placeholder(R.drawable.baseline_person_24)
                    .into(binding.componentToolbar.circularImageView)
            }
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_depositFragment_to_menuFragment)
            }
        }
        binding.apply {
            cardViewAttachment.setOnClickListener {
                pickFile()
            }
            textViewSelectedDate.setOnClickListener {
                depositViewModel.selectDate(requireContext())
            }

            binding.buttonDeposit.setOnClickListener {
                binding.progressBar.visibility = View.VISIBLE

                val depositAmountText = textFieldDeposit.text.toString()
                val dateDepositAmount = textViewSelectedDate.text.toString()
                val selectedFileUri = depositViewModel.fileUriLiveData.value
                if (depositAmountText.isEmpty() ||
                    dateDepositAmount.isEmpty() || selectedFileUri == null
                ) {
                    Toast.makeText(
                        requireContext(),
                        "Please Fill the missing Fields",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.progressBar.visibility = View.GONE

                } else {
                    depositViewModel.submitDeposit(
                        depositAmountText.toString().toInt(),
                        dateDepositAmount,
                        selectedFileUri,
                        requireContext()
                    )
                    binding.progressBar.visibility = View.GONE
                }
            }
        }

        depositViewModel.dateLiveData.observe(viewLifecycleOwner) { date ->
            binding.textViewSelectedDate.text = date
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
            depositViewModel.fileUriLiveData.value = selectedFileUri
            selectedFileUri?.let {
                binding.cardViewAttachment.findViewById<TextView>(R.id.textViewAttachment).text =
                    "File Attached"
                binding.cardViewAttachment.findViewById<ImageView>(R.id.imageViewAttachment)
                    .setImageResource(R.drawable.attachfile)
            }
        }
    }


}
