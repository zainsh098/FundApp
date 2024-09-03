package com.example.fundapp.fragments.googlelogin.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.FragmentMenuBinding
import com.example.fundapp.extensions.visibility
import com.example.fundapp.userrole.SessionManager
import com.example.fundapp.viewmodel.UserViewModel

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    private val menuViewModel: MenuViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentMenuBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel.currentUser.observe(viewLifecycleOwner) { user ->

            val isAdmin = SessionManager.getRole() == "admin"
            binding.cardApproveRequest.visibility(isAdmin)

        }


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
        }
    }
}
