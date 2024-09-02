package com.example.fundapp.fragments.googlelogin.myrequest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundapp.R
import com.example.fundapp.adapter.MyRequestAdapter
import com.example.fundapp.databinding.FragmentMyRequestBinding
import com.example.fundapp.viewmodel.TransactionViewModel
import com.google.firebase.auth.FirebaseAuth

import java.text.SimpleDateFormat
import java.util.Locale

class MyRequestFragment : Fragment() {
    private lateinit var binding: FragmentMyRequestBinding
    private  val transactionViewModel: TransactionViewModel by viewModels()
    private lateinit var myRequestAdapter: MyRequestAdapter
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyRequestBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myRequestAdapter = MyRequestAdapter(mutableListOf())

        binding.apply {
            myRequestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            myRequestRecyclerView.adapter = myRequestAdapter
        }


        transactionViewModel.transactionHistory.observe(viewLifecycleOwner) { history ->
            val myRequestHistory = history.filterNotNull().filter { it.status == "pending" }


            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val sortedHistory = myRequestHistory.sortedByDescending {
                dateFormat.parse(it.dateWithdraw)
            }

            myRequestAdapter.updateList(sortedHistory)
        }

        transactionViewModel.getTransactionHistory(auth.currentUser!!.uid)
    }
}
