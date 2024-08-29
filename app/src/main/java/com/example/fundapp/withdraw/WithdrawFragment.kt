package com.example.fundapp.withdraw

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.fundapp.R
import com.example.fundapp.databinding.FragmentWithdrawBinding
import com.example.fundapp.model.TransactionUser
import com.example.fundapp.viewmodel.TransactionViewModel
import com.example.fundapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar
import java.util.UUID

class WithdrawFragment : Fragment() {

    private lateinit var binding: FragmentWithdrawBinding
    private lateinit var transactionViewModel: TransactionViewModel
    private lateinit var userViewModel: UserViewModel
    private lateinit var auth: FirebaseAuth
    private var date: String = ""
    private var currentUserBalance: Int = 0 // Store the observed balance

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentWithdrawBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        transactionViewModel = ViewModelProvider(this).get(TransactionViewModel::class.java)
        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.withdraw)
            backArrow.setImageResource(R.drawable.back)
            context?.let {
                Glide.with(it).load(auth.currentUser?.photoUrl)
                    .placeholder(R.drawable.baseline_person_24)
                    .into(binding.componentToolbar.circularImageView)
            }
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_withdrawFragment_to_menuFragment)
            }

            cardImage.visibility=View.GONE

        }

        userViewModel.userBalance.observe(viewLifecycleOwner) { balance ->

            if (balance != null) {
                currentUserBalance = balance.toInt()
            }
        }

        userViewModel.getUserCurrentBalance(auth.currentUser!!.uid)

        binding.apply {
            textViewSelectedDate.setOnClickListener {
                selectDate()
            }

            buttonWithdrawAmount.setOnClickListener {
                val withdrawAmountText = textFieldWithdraw.text.toString()
                val withdrawReason = textFieldWithdrawReason.text.toString()

                if (withdrawAmountText.isEmpty() || withdrawReason.isEmpty() || date.isEmpty()) {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val withdrawAmount = withdrawAmountText.toInt()
                    Log.d("WithdrawFragment", "Withdraw Amount: $withdrawAmount")

                    // Check if the withdrawal amount is greater than the current balance
                    if (withdrawAmount >= currentUserBalance) {
                        Toast.makeText(
                            context,
                            "Withdrawal amount is greater than your current balance",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Proceed  the withdrawal
                        val transaction = TransactionUser(
                            photoUrl = auth.currentUser!!.photoUrl.toString(),
                            name = auth.currentUser!!.displayName!!,
                            amount = withdrawAmount,
                            reason = withdrawReason,
                            dateWithdraw = date,
                            type = "withdraw",
                            transactionId = UUID.randomUUID().toString(),
                            userId = auth.currentUser!!.uid,
                            status = "pending"
                        )
                        transactionViewModel.withdrawAmount(transaction)
                        Toast.makeText(context, "Withdrawal successful", Toast.LENGTH_SHORT).show()
                        textFieldWithdraw.text?.clear()
                        textFieldWithdrawReason.text?.clear()
                        textViewSelectedDate.text = R.string._15_02_2020.toString()

                    }
                }
            }
        }
    }

    private fun selectDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
                date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.textViewSelectedDate.text = date
            }, year, month, day
        )

        datePickerDialog.show()
    }
}
