package com.example.fundapp.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.adapter.OnItemClickListenerUser
import com.example.fundapp.adapter.UserAdapter
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.databinding.FragmentHomeBinding
import com.example.fundapp.model.User

/**
 * Fragment displaying the home screen with user information and organization balance.
 *
 * Handles UI interactions and communicates with [HomeViewModel] to fetch and display data.
 */
class HomeFragment : BindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    OnItemClickListenerUser {

    private lateinit var userAdapter: UserAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize adapter and set up RecyclerView
        userAdapter = UserAdapter(mutableListOf(), this)
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUsers.adapter = userAdapter

        // Observe and update UI based on LiveData from ViewModel
        homeViewModel.message.observe(viewLifecycleOwner) { message ->
            binding.textHomeGreetings.text = message
        }

        homeViewModel.allUsers.observe(viewLifecycleOwner) { users ->
            userAdapter.updateUsers(users)
        }

        homeViewModel.orgBalance.observe(viewLifecycleOwner) {
            binding.textHomeOrganizationBalanceValue.text = "Rs: $it"
        }

        homeViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            binding.apply {
                textHomeUserName.text = getString(R.string.hello) + user?.name
                textHomeDepositedValue.text = getString(R.string.rs) + user?.totalDeposited
                textHomeWithdrawValue.text = getString(R.string.rs) + user?.totalWithdrawAmount
            }
            context?.let {
                Glide.with(it)
                    .load(user?.photoUrl)
                    .placeholder(R.drawable.baseline_person_24)
                    .into(binding.componentToolbar.circularImageView)
            }
        }
        // Setup toolbar
        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.home)
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_menuFragment)
            }
        }

        // Handle back button press to move task to the background
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().moveTaskToBack(true)
                }
            }
        )
    }

    /**
     * Handles item click events from the user list.
     *
     * @param user The user that was clicked.
     */
    override fun onItemClick(user: User) {
        val bundle = Bundle().apply {
            putString("userId", user.userId)
        }
        findNavController().navigate(R.id.action_homeFragment_to_transactionDetailsFragment, bundle)
    }
}

