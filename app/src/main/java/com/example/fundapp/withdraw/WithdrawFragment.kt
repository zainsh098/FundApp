package com.example.fundapp.withdraw

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.FragmentWithdrawBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar


class WithdrawFragment : Fragment() {


    private lateinit var binding: FragmentWithdrawBinding
    private lateinit var auth: FirebaseAuth
    private var date: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWithdrawBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()


        binding.componentToolbar.apply {

            textToolbar.text = getString(R.string.menu)
            binding.componentToolbar.apply {
                context?.let {
                    Glide.with(it)
                        .load(auth.currentUser?.photoUrl)
                        .placeholder(R.drawable.baseline_person_24)
                        .into(binding.componentToolbar.circularImageView)
                }
                textToolbar.text = getString(R.string.withdraw)
                backArrow.setImageResource(R.drawable.back)
                backArrow.setOnClickListener {

                    findNavController().navigate(R.id.action_withdrawFragment_to_menuFragment)

                }


            }
        }
        binding.apply {
            buttonSelectDate.setOnClickListener {
                selectDate()
            }
        }

    }

    private fun selectDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.textViewSelectedDate.text = date
            }, year, month, day
        )

        datePickerDialog.show()
    }

}

