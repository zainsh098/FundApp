package com.example.fundapp.fragments.policies

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.fundapp.R
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.databinding.FragmentPolicyBinding
import com.example.fundapp.extensions.visibility

class PolicyFragment : BindingFragment<FragmentPolicyBinding>(FragmentPolicyBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.componentToolbar.apply {

            textToolbar.text = getString(R.string.policies)
            backArrow.setImageResource(R.drawable.back)
            cardImage.visibility(false)
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_policyFragment_to_menuFragment)
            }
        }
    }
}