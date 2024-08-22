package com.example.fundapp.fragments.googlelogin.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.adapter.UserAdapter
import com.example.fundapp.databinding.FragmentHomeBinding
import com.example.fundapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var auth: FirebaseAuth
    private val dateNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val currentUserId = auth.currentUser?.uid
        userViewModel.getUser(currentUserId!!)
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                binding.textHomeUserName.text = "Hello, " + user.name
                binding.textHomeCurrentBalanceValue.text = "Rs: ${user.currentBalance}"
                binding.textHomeDepositedValue.text = "Rs: ${user.totalDeposited}"
                binding.textHomeWithdrawValue.text = "Rs: ${user.totalWithdrawAmount}"
                binding.textHomeGreetings.text = setMessage()
                context?.let {
                    Glide.with(it)
                        .load(user.photoUrl)
                        .placeholder(R.drawable.deposit)
                        .into(binding.componentToolbar.circularImageView)

                }
            }



        }


        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.home)
       backArrow.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_menuFragment)
        }
        }
    }

    fun setMessage(): String {
        val message = when (dateNow) {
            in 0..5 -> "Good Night"
            in 6..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..23 -> "Good Evening"
            else -> "Invalid time"
        }
        return message
    }
}