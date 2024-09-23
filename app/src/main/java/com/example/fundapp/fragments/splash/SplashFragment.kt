package com.example.fundapp.fragments.splash

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.fundapp.R
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : BindingFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(2000L)
            findNavController().navigate(R.id.action_splashFragment_to_organizationNameFragment)
        }
    }
}
