package com.example.fundapp.fragments.googlelogin.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.fundapp.R
import com.example.fundapp.adapter.UserAdapter
import com.example.fundapp.databinding.FragmentHomeBinding
import com.example.fundapp.viewmodel.UserViewModel

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        userAdapter = UserAdapter(requireContext(), mutableListOf())
//        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
//
//        binding.recyclerView.adapter = userAdapter

        userViewModel.users.observe(viewLifecycleOwner) { users ->
            userAdapter.updateList(users)
        }
        userViewModel.getAllUsers()
        binding.componentToolbar.backArrow.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_menuFragment)
        }
        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.home)
        }
    }
}