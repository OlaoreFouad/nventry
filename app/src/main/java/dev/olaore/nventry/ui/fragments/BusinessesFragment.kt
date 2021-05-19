package dev.olaore.nventry.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentBusinessesBinding
import dev.olaore.nventry.models.domain.Business
import dev.olaore.nventry.network.Status
import dev.olaore.nventry.ui.BusinessActivity
import dev.olaore.nventry.ui.adapters.BusinessesAdapter
import dev.olaore.nventry.ui.listeners.BusinessInteraction
import dev.olaore.nventry.ui.viewmodels.BusinessesViewModel
import dev.olaore.nventry.utils.Utils
import dev.olaore.nventry.utils.obtainViewModel
import dev.olaore.nventry.utils.showSnackbar

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
                BusinessesFragmentDirections.actionBusinessesToAddBusinessFragment()
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

        viewModel.onBusinessDeleted.observe(viewLifecycleOwner, Observer {

            binding.isLoading = it.status == Status.LOADING

            when (it.status) {

                Status.ERROR -> {
                    showSnackbar(binding.businessesList, "Error occurred: " + it.message)
                }
                Status.SUCCESS -> {
                    showSnackbar(binding.businessesList, "Business deleted successfully")
                    viewModel.refresh()
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
        val intent = Intent(requireContext(), BusinessActivity::class.java)
        intent.putExtra(Utils.BUSINESS_ID, businessId)
        startActivity(intent)
    }

    override fun onDeleteBusiness(businessId: String) {
        viewModel.deleteBusiness(businessId)
    }

    override fun onEditBusiness(business: Business) {
        findNavController().navigate(
            BusinessesFragmentDirections.actionBusinessesToEditBusinessFragment(business)
        )
    }

}