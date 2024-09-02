package com.example.fundapp.fragments.googlelogin.withdraw

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fundapp.R
import com.example.fundapp.databinding.FragmentWithdrawBinding
import com.example.fundapp.fragments.googlelogin.bottomsheet.BottomSheetDFragment
import com.example.fundapp.fragments.googlelogin.bottomsheet.WithdrawalBottomSheetFragment
import com.example.fundapp.viewmodel.UserViewModel
import com.google.firebase.auth.FirebaseAuth

class WithdrawFragment : Fragment() {

    private lateinit var binding: FragmentWithdrawBinding
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var auth: FirebaseAuth
    private var currentUserBalance: Int = 0 // it will store the current balance of the user

    private val withdrawViewModel: WithdrawViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentWithdrawBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

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

        userViewModel.userBalance.observe(viewLifecycleOwner) { balance ->

            if (balance != null) {
                currentUserBalance = balance.toInt()
            }
        }


        withdrawViewModel.dateLiveData.observe(viewLifecycleOwner) { date ->
            binding.textViewSelectedDate.text = date

        }

        userViewModel.getUserCurrentBalance(auth.currentUser!!.uid)

        binding.apply {
            textViewSelectedDate.setOnClickListener {
                withdrawViewModel.selectDate(requireContext())
            }

            buttonWithdrawAmount.setOnClickListener {
                val withdrawAmountText = textFieldWithdraw.text.toString()
                val withdrawReason = textFieldWithdrawReason.text.toString()
                val date = textViewSelectedDate.text.toString()

                if (withdrawAmountText.isEmpty() || withdrawReason.isEmpty() || date.isEmpty()) {
                    Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                } else {
                    val withdrawAmount = withdrawAmountText.toInt()
                    Log.d("WithdrawFragment", "Withdraw Amount: $withdrawAmount")

                    if(withdrawAmount>currentUserBalance)

                        Toast.makeText(
                            context,
                            "Withdrawal amount is greater than your current balance",
                            Toast.LENGTH_SHORT
                        ).show()

                    withdrawViewModel.requestWithdrawal(
                        withdrawAmount,
                        withdrawReason,
                        date,
                        requireContext()
                    )
                    textFieldWithdraw.text?.clear()
                    textFieldWithdrawReason.text?.clear()
                    textViewSelectedDate.text = R.string._15_02_2020.toString()

                }


            }
        }
        withdrawViewModel.withdrawSuccess.observe(viewLifecycleOwner) { success ->


            if (success) {
                showBottomSheet()
            }
        }
    }


    private fun showBottomSheet() {
        val bottomSheet =WithdrawalBottomSheetFragment()
        bottomSheet.show(parentFragmentManager, WithdrawalBottomSheetFragment::class.java.name)
    }
}



