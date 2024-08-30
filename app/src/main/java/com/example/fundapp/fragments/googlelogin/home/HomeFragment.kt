package com.example.fundapp.fragments.googlelogin.home


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.adapter.UserAdapter
import com.example.fundapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var userAdapter: UserAdapter
    private val homeViewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userAdapter = UserAdapter(mutableListOf())
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUsers.adapter = userAdapter


        homeViewModel.message.observe(viewLifecycleOwner) { message ->
            binding.textHomeGreetings.text = message
        }

        homeViewModel.allUsers.observe(viewLifecycleOwner) { users ->
            userAdapter.updateUsers(users)
        }

        homeViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.apply {
                    textHomeUserName.text = "Hello, " + user.name
                    textHomeCurrentBalanceValue.text = "Rs: ${user.currentBalance}"
                    textHomeDepositedValue.text = "Rs: ${user.totalDeposited}"
                    textHomeWithdrawValue.text = "Rs: ${user.totalWithdrawAmount}"
                }
                context?.let {
                    Glide.with(it)
                        .load(user.photoUrl)
                        .placeholder(R.drawable.baseline_person_24)
                        .into(binding.componentToolbar.circularImageView)
                }
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


    }

}

