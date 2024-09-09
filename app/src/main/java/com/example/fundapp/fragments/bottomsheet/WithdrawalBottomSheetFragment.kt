package com.example.fundapp.fragments.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fundapp.R
import com.example.fundapp.databinding.FragmentWithdrawalBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class WithdrawalBottomSheetFragment : BottomSheetDialogFragment() {


    private lateinit var binding: FragmentWithdrawalBottomSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentWithdrawalBottomSheetBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.txtCloseButton.setOnClickListener {
            findNavController().navigate(R.id.action_withdrawFragment_to_menuFragment)
            dismiss()
            findNavController().popBackStack(R.id.withdrawFragment, true)

        }

    }

}