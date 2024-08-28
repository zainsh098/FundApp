package com.example.fundapp.fragments.googlelogin.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.FragmentMenuBinding
import com.example.fundapp.fragments.googlelogin.approverequest.ApproveRequestFragment
import com.example.fundapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class MenuFragment : Fragment() {


    private lateinit var binding: FragmentMenuBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var role: String

    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View {
        binding = FragmentMenuBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        role = arguments?.getString("role").toString()


        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)


//        if (role == "admin") {
//                binding.cardApproveRequest.visibility = View.VISIBLE
//            } else {
//                binding.cardApproveRequest.visibility = View.GONE
//            }



        binding.componentToolbar.apply {

            textToolbar.text = getString(R.string.menu)
            binding.componentToolbar.apply {
                context?.let {
                    Glide.with(it)
                        .load(auth.currentUser?.photoUrl)
                        .placeholder(R.drawable.baseline_person_24)
                        .into(binding.componentToolbar.circularImageView)
                }

            }

            backArrow.setImageResource(R.drawable.back)
            backArrow.setOnClickListener {

                findNavController().navigate(R.id.action_menuFragment_to_homeFragment)

            }
            binding.cardDeposit.setOnClickListener {
                findNavController().navigate(R.id.action_menuFragment_to_depositFragment)

            }
            binding.cardWithdrawFunds.setOnClickListener {

                findNavController().navigate(R.id.action_menuFragment_to_withdrawFragment)

            }

            binding.cardMyRequest.setOnClickListener {

                findNavController().navigate(R.id.action_menuFragment_to_myRequestFragment)

            }
        }

    }
}