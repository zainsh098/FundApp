package com.example.fundapp.fragments.googlelogin.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val auth = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.componentToolbar.backArrow.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_menuFragment)
        }

        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.home)
        }

        binding.apply {
            cardUserName.text = auth?.displayName?.split(" ")?.get(1)
        }

        // Use Glide to load the profile picture
        val photoUrl = auth?.photoUrl
        if (photoUrl != null) {
            Glide.with(this)
                .load(photoUrl)
                .placeholder(R.drawable.deposit)
                .into(binding.circularImageView)
        }
    }
}
