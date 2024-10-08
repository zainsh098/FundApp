package com.example.fundapp.fragments.withdraw

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fundapp.R
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.databinding.FragmentWithdrawBinding
import com.example.fundapp.fragments.bottomsheet.WithdrawalBottomSheetFragment
import com.example.fundapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class WithdrawFragment :
    BindingFragment<FragmentWithdrawBinding>(FragmentWithdrawBinding::inflate) {

    private lateinit var auth: FirebaseAuth
    private var currentUserBalance: Int = 0
    private val withdrawViewModel: WithdrawViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.withdraw)
            backArrow.setImageResource(R.drawable.back)
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_withdrawFragment_to_menuFragment)
            }
            cardImage.visibility = View.GONE
        }

        userViewModel.getUserCurrentBalance(auth.currentUser!!.uid)
        userViewModel.userBalance.observe(viewLifecycleOwner) { balance ->
            balance?.let {
                currentUserBalance = it.toInt()
            }
        }
        withdrawViewModel.dateLiveData.observe(viewLifecycleOwner) { date ->
            binding.textViewSelectedDate.text = date
        }


        binding.apply {
            textViewSelectedDate.setOnClickListener {
                withdrawViewModel.selectDate(requireContext())
            }


            buttonWithdrawAmount.setOnClickListener {

                val withdrawAmountText = textFieldWithdraw.text.toString()
                val withdrawReason = textFieldWithdrawReason.text.toString()
                val date = textViewSelectedDate.text.toString()

                if (withdrawAmountText.isEmpty() || withdrawReason.isEmpty() || date.isEmpty()) {
                    showToast("Please fill in all fields")
                } else {

                    withdrawViewModel.getTransactionHistory(auth.currentUser!!.uid)
                    withdrawViewModel.transactionHistory.observe(viewLifecycleOwner) { history ->

                        val pending = history.filter {

                            it!!.status == "pending" && it.type == "withdraw"
                        }
                        Log.d("Status ", pending.toString())
                        if (pending.isNotEmpty()) {
                            showToast("Already have pending request ")
                        }


                        else
                        {
                            val withdrawAmount = withdrawAmountText.toInt()
                            withdrawViewModel.requestWithdrawal(
                                currentUserBalance,
                                withdrawAmount,
                                withdrawReason,
                                date
                            )

                        }

                    }


                }

            }
            observeViewModel()
        }
    }




    private fun observeViewModel() {
        withdrawViewModel.withdrawSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                showBottomSheet()
            }
        }
        withdrawViewModel.successMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                showToast(it)
            }
        }
        withdrawViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                showToast(it)
            }
        }
    }

    private fun showBottomSheet() {
        val bottomSheet = WithdrawalBottomSheetFragment()
        bottomSheet.show(parentFragmentManager, WithdrawalBottomSheetFragment::class.java.name)
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}

