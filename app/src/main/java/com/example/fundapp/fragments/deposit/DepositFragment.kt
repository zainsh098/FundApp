package com.example.fundapp.fragments.deposit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fundapp.R
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.databinding.FragmentDepositBinding
import com.example.fundapp.fragments.bottomsheet.BottomSheetDFragment

class DepositFragment : BindingFragment<FragmentDepositBinding>(FragmentDepositBinding::inflate) {

    private val depositViewModel: DepositViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.deposit)
            backArrow.setImageResource(R.drawable.back)
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_depositFragment_to_menuFragment)
                findNavController().popBackStack(R.id.depositFragment, true)
            }
            cardImage.visibility = View.GONE
        }
        binding.apply {
            cardViewAttachment.setOnClickListener {
                pickFile()
            }
            textViewSelectedDate.setOnClickListener {
                depositViewModel.selectDate(requireContext())
            }

            binding.buttonDeposit.setOnClickListener {
                val depositAmountText = textFieldDeposit.text.toString()
                val dateDepositAmount = textViewSelectedDate.text.toString()
                val selectedFileUri = depositViewModel.fileUriLiveData.value
                if (depositAmountText.isEmpty() ||
                    dateDepositAmount.isEmpty() || selectedFileUri == null
                ) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.please_fill_the_missing_fields),
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    depositViewModel.submitDeposit(
                        depositAmountText.toString().toInt(),
                        dateDepositAmount,
                        selectedFileUri,
                        requireContext()
                    )
                }

            }
        }
        depositViewModel.dateLiveData.observe(viewLifecycleOwner) { date ->
            binding.textViewSelectedDate.text = date
        }
        depositViewModel.isLoading.observe(viewLifecycleOwner) { view ->
            binding.progressBar.visibility = if (view) View.VISIBLE else View.GONE

        }
        depositViewModel.depositSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                showBottomSheet()
            }
        }
    }

    private fun showBottomSheet() {
        val bottomSheet = BottomSheetDFragment()
        bottomSheet.show(parentFragmentManager, BottomSheetDFragment::class.java.name)
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
                    getString(R.string.file_attached)
                binding.cardViewAttachment.findViewById<ImageView>(R.id.imageViewAttachment)
                    .setImageResource(R.drawable.attachfile)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}
