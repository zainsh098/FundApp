package com.example.fundapp.withdraw

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fundapp.R
import com.example.fundapp.databinding.FragmentWithdrawBinding
import java.util.Calendar


class WithdrawFragment : Fragment() {


    private lateinit var binding: FragmentWithdrawBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWithdrawBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            componentToolbar.textToolbar.text = getString(R.string.withdraw)
            componentToolbar.backArrow.setImageResource(R.drawable.arrow_back)



            componentToolbar.backArrow.setOnClickListener {

                findNavController().navigate(R.id.action_withdrawFragment_to_menuFragment)

            }
            buttonSelectDate.setOnClickListener {

                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(
                    requireContext(),
                    { _, selectedYear, selectedMonth, selectedDay ->
                        val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                        textViewSelectedDate.text = date
                    }, year, month, day
                )

                datePickerDialog.show()
            }
        }

    }
}

