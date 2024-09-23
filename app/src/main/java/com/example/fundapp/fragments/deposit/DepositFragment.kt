package com.example.fundapp.fragments.deposit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.fundapp.R
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.databinding.FragmentDepositBinding
import com.example.fundapp.fragments.bottomsheet.BottomSheetDFragment

/**
 * A Fragment for handling deposit operations.
 *
 * This fragment allows users to make a deposit by selecting a date, entering an amount,
 * and attaching a file. It also provides feedback via Toast messages and displays a bottom
 * sheet dialog upon successful deposit.
 */
class DepositFragment : BindingFragment<FragmentDepositBinding>(FragmentDepositBinding::inflate) {

    private val depositViewModel: DepositViewModel by viewModels()

    /**
     * Called immediately after `onCreateView`. Sets up the toolbar, UI elements, and observers.
     *
     * @param view The root view of the fragment's layout.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupUI()

    }

    /**
     * Sets up the toolbar with appropriate text and click listeners.
     */
    private fun setupToolbar() {
        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.deposit)
            backArrow.setImageResource(R.drawable.back)
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_depositFragment_to_menuFragment)
                findNavController().popBackStack(R.id.depositFragment, true)
            }
            cardImage.visibility = View.GONE
        }
    }

    /**
     * Configures the UI elements, including button click listeners and file picker.
     */
    private fun setupUI() {
        binding.apply {
            cardViewAttachment.setOnClickListener { pickFile() }
            textViewSelectedDate.setOnClickListener { depositViewModel.selectDate(requireContext()) }

            buttonDeposit.setOnClickListener {
                val depositAmountText = textFieldDeposit.text.toString()
                val dateDepositAmount = textViewSelectedDate.text.toString()
                val selectedFileUri = depositViewModel.fileUriLiveData.value

                if (depositAmountText.isEmpty() || dateDepositAmount.isEmpty() || selectedFileUri == null) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.please_fill_the_missing_fields),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    depositViewModel.submitDeposit(
                        depositAmountText.toInt(),
                        dateDepositAmount,
                        selectedFileUri,
                    )
                }
            }
            observeViewModel()
        }
    }

    /**
     * Observes changes in the ViewModel and updates the UI accordingly.
     */
    private fun observeViewModel() {
        depositViewModel.dateLiveData.observe(viewLifecycleOwner) { date ->
            binding.textViewSelectedDate.text = date
        }

        depositViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->


            binding.apply {
                blurredBackground.visibility = if (isLoading) View.VISIBLE else View.GONE
                progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }

        depositViewModel.depositSuccess.observe(viewLifecycleOwner) { success ->
            if (success) showBottomSheet()
        }

        depositViewModel.successMessage.observe(viewLifecycleOwner) { message ->
            message?.let { showToast(it) }
        }

        depositViewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let { showToast(it) }
        }
    }

    /**
     * Shows a bottom sheet dialog when the deposit is successful.
     */
    private fun showBottomSheet() {
        val bottomSheet = BottomSheetDFragment()
        bottomSheet.show(parentFragmentManager, BottomSheetDFragment::class.java.name)
    }

    /**
     * Displays a Toast message with the provided text.
     *
     * @param message The message to be displayed in the Toast.
     */
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Starts an activity to pick a file from the device.
     */
    private fun pickFile() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply { type = "image/*" }
        startActivityForResult(intent, 100)
    }

    /**
     * Handles the result of the file picker activity.
     *
     * @param requestCode The request code passed in `startActivityForResult()`.
     * @param resultCode The result code returned by the file picker activity.
     * @param data The intent containing the result data.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            val selectedFileUri = data?.data
            depositViewModel.fileUriLiveData.value = selectedFileUri
            selectedFileUri?.let {
                binding.textViewAttachment.text = getString(R.string.file_attached)
                binding.imageViewAttachment.setImageResource(R.drawable.attachfile)
            }
        }
    }
}
