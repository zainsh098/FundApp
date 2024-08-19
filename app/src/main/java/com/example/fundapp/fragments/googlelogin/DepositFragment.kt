package com.example.fundapp.fragments.googlelogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fundapp.R
import com.example.fundapp.databinding.FragmentDepositBinding

class DepositFragment : Fragment() {


    private  lateinit var binding:FragmentDepositBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentDepositBinding.inflate(layoutInflater,container,false)
        return inflater.inflate(R.layout.fragment_deposit, container, false)
    }


}