package dev.olaore.nventry.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentPartnersBinding
import dev.olaore.nventry.ui.adapters.PartnersAdapter
import dev.olaore.nventry.utils.getStubPartners

class PartnersFragment : Fragment() {

    private lateinit var binding: FragmentPartnersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentPartnersBinding.inflate(inflater)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val partnersAdapter = PartnersAdapter(requireContext(), getStubPartners())

        binding.partnersList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = partnersAdapter
        }

    }

}