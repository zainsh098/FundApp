package com.example.fundapp.fragments.googlelogin.home


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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

    private lateinit var userAdapter: UserAdapter
    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var auth: FirebaseAuth


    private val homeViewModel : HomeViewModel by viewModels()
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

        transactionViewModel = ViewModelProvider(this)[TransactionViewModel::class.java]


        homeViewModel.allUsers.observe(viewLifecycleOwner) { users ->
            userAdapter.updateUsers(users)
        }
        homeViewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {

                UserSession.role = user.role
                binding.apply {
                    textHomeUserName.text = "Hello, " + user.name
                    textHomeCurrentBalanceValue.text = "Rs: ${user.currentBalance}"
                    textHomeDepositedValue.text = "Rs: ${user.totalDeposited}"
                    textHomeWithdrawValue.text = "Rs: ${user.totalWithdrawAmount}"
                }


//                binding.textHomeGreetings.text = setMessage() todo shift to viewmodel
                context?.let {
                    Glide.with(it)
                        .load(user.photoUrl)
                        .placeholder(R.drawable.baseline_person_24)
                        .into(binding.componentToolbar.circularImageView)
                }
            }
        }

        // Start listening to transaction updates
//        transactionViewModel.startTransactionListener(currentUserId)
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


}

object UserSession {
    var role: String? = null
}