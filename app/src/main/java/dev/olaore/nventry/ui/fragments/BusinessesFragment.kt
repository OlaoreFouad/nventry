package dev.olaore.nventry.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentBusinessesBinding
import dev.olaore.nventry.models.domain.Business
import dev.olaore.nventry.network.Status
import dev.olaore.nventry.ui.adapters.BusinessesAdapter
import dev.olaore.nventry.ui.listeners.BusinessInteraction
import dev.olaore.nventry.ui.viewmodels.BusinessesViewModel
import dev.olaore.nventry.utils.obtainViewModel

class BusinessesFragment : Fragment(), BusinessInteraction {

    private lateinit var binding: FragmentBusinessesBinding
    private lateinit var viewModel: BusinessesViewModel
    private lateinit var businessesAdapter: BusinessesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBusinessesBinding.inflate(
            inflater, container, false
        )

        binding.apply {
            isEmpty = false
            isEmpty = false
            isLoading = false
        }

        binding.addBusinessButton.setOnClickListener {
            it.findNavController().navigate(
                R.id.action_businesses_to_addBusinessFragment
            )
        }

        viewModel = obtainViewModel(BusinessesViewModel::class.java)

        viewModel.businesses.observe(viewLifecycleOwner, Observer {

            binding.isLoading = it.status == Status.LOADING

            when (it.status) {

                Status.ERROR -> {
                    Log.d("BusinessesFragment", "Error occurred: " + it.message)
                }
                Status.SUCCESS -> {
                    binding.isEmpty = it.data?.isEmpty()
                    if (binding.isEmpty == false) {
                        val businesses: List<Business> = it.data!!
                        businessesAdapter = BusinessesAdapter(requireContext(), businesses, this)
                        binding.businessesList.apply {
                            layoutManager = LinearLayoutManager(requireContext())
                            adapter = businessesAdapter
                            setHasFixedSize(true)
                        }
                    }
                }

            }

        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
    }

    override fun onBusinessClicked(businessId: String) {
        Log.d("BusinessesFragment", "Business Clicked: $businessId")
    }

    override fun onDeleteBusiness(businessId: String) {
        Log.d("BusinessesFragment", "On Delete Business: $businessId")
    }

    override fun onEditBusiness(businessId: String) {
        Log.d("BusinessesFragment", "On Edit Business: $businessId")
    }

}