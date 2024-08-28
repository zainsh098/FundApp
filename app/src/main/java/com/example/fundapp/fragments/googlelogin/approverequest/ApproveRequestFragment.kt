package com.example.fundapp.fragments.googlelogin.approverequest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fundapp.databinding.FragmentApproveRequestBinding

class ApproveRequestFragment : Fragment() {

    private lateinit var binding: FragmentApproveRequestBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentApproveRequestBinding.inflate(inflater, container, false)
        return binding.root
    }





}