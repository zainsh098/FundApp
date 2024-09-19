package com.example.fundapp.fragments.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.fundapp.R
import com.example.fundapp.databinding.FragmentWithdrawalBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * A BottomSheetDialogFragment for handling withdrawal actions.
 *
 * This fragment presents a bottom sheet dialog used for withdrawal-related operations.
 * It includes a close button that navigates back to the menu fragment and clears the
 * back stack to remove the current withdrawal fragment from the navigation stack.
 */
class WithdrawalBottomSheetFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentWithdrawalBottomSheetBinding

    /**
     * Inflates the layout for this bottom sheet fragment.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The root view of the inflated layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWithdrawalBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after [onCreateView]. Sets up the view and listeners.
     *
     * @param view The root view of the fragment's layout.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)

        binding.txtCloseButton.setOnClickListener {
            // Navigate to the menu fragment and clear the back stack
            findNavController().navigate(R.id.action_withdrawFragment_to_menuFragment)
            dismiss() // Close the bottom sheet dialog
            findNavController().popBackStack(R.id.withdrawFragment, true) // Remove withdrawFragment from back stack
        }
    }
}
