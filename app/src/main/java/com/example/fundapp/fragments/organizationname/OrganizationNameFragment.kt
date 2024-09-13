package com.example.fundapp.fragments.organizationname

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fundapp.R
import com.example.fundapp.base.BindingFragment
import com.example.fundapp.databinding.FragmentOrganizationNameBinding

class OrganizationNameFragment : BindingFragment<FragmentOrganizationNameBinding>(FragmentOrganizationNameBinding::inflate) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_organization_name, container, false)
    }

}