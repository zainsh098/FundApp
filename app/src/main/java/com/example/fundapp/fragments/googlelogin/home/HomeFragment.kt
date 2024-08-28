package com.example.fundapp.fragments.googlelogin.home


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.adapter.TransactionAdapter
import com.example.fundapp.adapter.UserAdapter
import com.example.fundapp.databinding.FragmentHomeBinding
import com.example.fundapp.viewmodel.TransactionViewModel
import com.example.fundapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var auth: FirebaseAuth
    private val dateNow = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

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

        auth = FirebaseAuth.getInstance()
        userAdapter = UserAdapter(requireContext(), mutableListOf())
        transactionAdapter = TransactionAdapter(requireContext(), mutableListOf())
        binding.recyclerViewUsers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUsers.adapter = userAdapter

        userViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]

        val currentUserId = auth.currentUser?.uid

        userViewModel.getUser(currentUserId!!)
        userViewModel.getAllUsers()

        userViewModel.users.observe(viewLifecycleOwner) { users ->
            userAdapter.updateUsers(users)
        }
        userViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {

                UserSession.role = user.role

                binding.textHomeUserName.text = "Hello, " + user.name
                binding.textHomeCurrentBalanceValue.text = "Rs: ${user.currentBalance}"
                binding.textHomeDepositedValue.text = "Rs: ${user.totalDeposited}"
                binding.textHomeWithdrawValue.text = "Rs: ${user.totalWithdrawAmount}"
                binding.textHomeGreetings.text = setMessage()
                context?.let {
                    Glide.with(it)
                        .load(user.photoUrl)
                        .placeholder(R.drawable.baseline_person_24)
                        .into(binding.componentToolbar.circularImageView)
                }
            }
        }

        // Start listening to transaction updates
        transactionViewModel.startTransactionListener(currentUserId)
        transactionViewModel.transactionHistory.observe(viewLifecycleOwner) { transactions ->
            // Update your UI or adapter with new transactions here
            transactionAdapter.updateList(transactions.filterNotNull())
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

    override fun onDestroyView() {
        super.onDestroyView()
        // Stop listening to transaction updates to avoid memory leaks
        transactionViewModel.stopTransactionListener()
    }

    private fun setMessage(): String {
        return when (dateNow) {
            in 0..5 -> "Good Night"
            in 6..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..23 -> "Good Evening"
            else -> "Invalid time"
        }
    }
}

object UserSession {
    var role: String? = null
}