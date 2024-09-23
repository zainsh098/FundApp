package com.example.fundapp.fragments.requestdetailsbottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.FragmentRequestDetailSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class RequestDetailSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentRequestDetailSheetBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestDetailSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val urlImage = arguments?.getString("photoUrl")

        Glide.with(requireContext())
            .load(urlImage)
            .placeholder(R.drawable.proof)
            .into(binding.proofImage)

    }

}