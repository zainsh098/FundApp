package com.example.fundapp.fragments.home


import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.example.fundapp.viewmodel.UserViewModel

class HomeFragment : BindingFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate),
    OnItemClickListenerUser {


    private lateinit var userAdapter: UserAdapter
    private val homeViewModel: HomeViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        userAdapter = UserAdapter(mutableListOf(), this)
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUsers.adapter = userAdapter

        val userRole = userViewModel.currentUser.value?.role
        Log.d("HomeFragment", "User Role   Home Fragment : $userRole")

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
                textHomeUserName.text = "Hello, " + user?.name
//                textHomeOrganizationBalanceValue.text = "Rs: ${user?.organizationBalance}"
                textHomeDepositedValue.text = "Rs: ${user?.totalDeposited}"
                textHomeWithdrawValue.text = "Rs: ${user?.totalWithdrawAmount}"
            }
            context?.let {
                Glide.with(it)
                    .load(user?.photoUrl)
                    .placeholder(R.drawable.baseline_person_24)
                    .into(binding.componentToolbar.circularImageView)
            }

        }

        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.home)
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_menuFragment)
            }

            circularImageView.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_transactionDetailsFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().moveTaskToBack(true)
                }
            })
    }

    override fun onItemClick(user: User) {
        val bundle = Bundle().apply {
            putString("userId", user.userId)
        }
        findNavController().navigate(R.id.action_homeFragment_to_transactionDetailsFragment, bundle)
    }


}


