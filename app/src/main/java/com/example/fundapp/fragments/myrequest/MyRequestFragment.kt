package com.example.fundapp.fragments.myrequest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundapp.R
import com.example.fundapp.adapter.MyRequestAdapter
import com.example.fundapp.databinding.FragmentMyRequestBinding
import com.example.fundapp.extensions.visibility
import java.text.SimpleDateFormat
import java.util.Locale

class MyRequestFragment : Fragment() {
    private lateinit var binding: FragmentMyRequestBinding
    private val myRequestViewModel: MyRequestViewModel by viewModels()
    private lateinit var myRequestAdapter: MyRequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

        binding.componentToolbar.apply {

            textToolbar.text = getString(R.string.my_request)
            backArrow.setImageResource(R.drawable.back)
            cardImage.visibility(false)

            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_myRequestFragment_to_menuFragment)

            }

        }
        myRequestViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility(isLoading)

        }

        myRequestViewModel.getTransactionHistory1.observe(viewLifecycleOwner) { history ->
            val myRequestHistory =
                history.filterNotNull().filter { it.status == "pending" || it.status == "accepted" }
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val sortedHistory = myRequestHistory.sortedByDescending {
                dateFormat.parse(it.date)
            }
            myRequestAdapter.updateList(sortedHistory)
        }

    }
}
