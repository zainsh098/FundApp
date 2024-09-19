package com.example.fundapp.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.databinding.FragmentMenuBinding
import com.example.fundapp.extensions.visibility
import com.example.fundapp.userrole.SessionManager

/**
 * Fragment representing the menu screen with navigation options.
 *
 * Shows different options based on the user's role (admin or regular user).
 */
class MenuFragment : BindingFragment<FragmentMenuBinding>(FragmentMenuBinding::inflate) {

    private val menuViewModel: MenuViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adjust visibility of menu items based on user role
        if (SessionManager.getRole() == "admin") {
            binding.cardApproveRequest.visibility(true)
            binding.cardApproveRequestDeposit.visibility(true)
        } else {
            binding.cardApproveRequestDeposit.visibility(false)
            binding.cardApproveRequest.visibility = View.GONE
        }

        // Setup toolbar with user photo and navigation actions
        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.menu)
            context?.let {
                Glide.with(it)
                    .load(menuViewModel.photoUrl.value)
                    .placeholder(R.drawable.baseline_person_24)
                    .into(binding.componentToolbar.circularImageView)
            }
            backArrow.setImageResource(R.drawable.back)
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_menuFragment_to_homeFragment)
            }
        }

        // Setup click listeners for navigation
        binding.apply {
            cardDeposit.setOnClickListener {
                findNavController().navigate(R.id.action_menuFragment_to_depositFragment)
            }
            cardWithdrawFunds.setOnClickListener {
                findNavController().navigate(R.id.action_menuFragment_to_withdrawFragment)
            }
            cardMyRequest.setOnClickListener {
                findNavController().navigate(R.id.action_menuFragment_to_myRequestFragment)
            }
            cardApproveRequest.setOnClickListener {
                findNavController().navigate(R.id.action_menuFragment_to_approveRequestFragment)
            }
            cardApproveRequestDeposit.setOnClickListener {
                findNavController().navigate(R.id.action_menuFragment_to_approveDepositFragment)
            }
            cardPolicy.setOnClickListener {
                findNavController().navigate(R.id.action_menuFragment_to_policyFragment)
            }
        }
    }
}
