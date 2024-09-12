package com.example.fundapp.fragments.myrequestwithdraw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fundapp.R
import com.example.fundapp.adapter.MyRequestAdapter
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.databinding.FragmentMyRequestBinding
import com.example.fundapp.extensions.visibility
import com.example.fundapp.fragments.bottomsheet.BottomSheetDFragment
import com.example.fundapp.fragments.requestdetailsbottom.RequestDetailSheetFragment
import java.text.SimpleDateFormat
import java.util.Locale

class MyRequestFragment :
    BindingFragment<FragmentMyRequestBinding>(FragmentMyRequestBinding::inflate),
    MyRequestAdapter.OnClickItemShowBottomSheet {
    private val myRequestViewModel: MyRequestViewModel by viewModels()
    private lateinit var myRequestAdapter: MyRequestAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyRequestBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myRequestAdapter = MyRequestAdapter(mutableListOf(), this)

        binding.apply {
            myRequestRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            myRequestRecyclerView.adapter = myRequestAdapter

        }
        binding.componentToolbar.apply {
            textToolbar.text = getString(R.string.my_request)
            backArrow.setImageResource(R.drawable.back)
            cardImage.visibility(false)
            backArrow.setOnClickListener {
                findNavController().navigate(R.id.action_myRequestFragment_to_menuFragment)

            }
        }
        myRequestViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility(isLoading)

        }

        myRequestViewModel.getTransactionHistory1.observe(viewLifecycleOwner) { history ->
            if (history.isNullOrEmpty()) {
                binding.txtNoData.visibility(true)

            } else {
                binding.txtNoData.visibility(false)
                val myRequestHistory =
                    history.filterNotNull()
                        .filter { it.status == "pending" || it.status == "accepted" || it.type == "withdraw" || it.type == "deposit" }
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val sortedHistory = myRequestHistory.sortedByDescending {
                    it.status
                    dateFormat.parse(it.date)

                }
                myRequestAdapter.updateList(sortedHistory)
            }
        }
    }

    override fun showPhoto(position: Int, photoUrl: String?) {
        val bundle = Bundle()
        bundle.putString("photoUrl", photoUrl)
        val bottomSheet = RequestDetailSheetFragment()
        bottomSheet.arguments = bundle
        bottomSheet.show(parentFragmentManager, BottomSheetDFragment::class.java.name)

    }
}
