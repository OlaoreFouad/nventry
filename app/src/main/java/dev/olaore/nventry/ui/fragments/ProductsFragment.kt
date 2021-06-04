package dev.olaore.nventry.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentProductsBinding
import dev.olaore.nventry.models.domain.Business
import dev.olaore.nventry.models.network.Product
import dev.olaore.nventry.network.Status
import dev.olaore.nventry.ui.adapters.BusinessesAdapter
import dev.olaore.nventry.ui.adapters.ProductsAdapter
import dev.olaore.nventry.ui.viewmodels.BusinessViewModel
import dev.olaore.nventry.ui.viewmodels.ProductsViewModel
import dev.olaore.nventry.utils.obtainViewModel
import dev.olaore.nventry.utils.showSnackbar

class ProductsFragment : Fragment() {

    private lateinit var binding: FragmentProductsBinding
    private lateinit var businessViewModel: BusinessViewModel
    private lateinit var viewModel: ProductsViewModel

    private lateinit var businessId: String
    private lateinit var productsAdapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(
            inflater, container, false
        )
        viewModel = obtainViewModel(ProductsViewModel::class.java)
        binding.apply {
            isEmpty = false
            isEmpty = false
            isLoading = false
        }

        requireActivity().let {
            businessViewModel = ViewModelProvider(it).get(BusinessViewModel::class.java)
            this.businessId = businessViewModel.businessId
        }
        binding.addProductButton.setOnClickListener {
            it.findNavController().navigate(
                R.id.action_products_to_upsertProductFragment
            )
        }

        viewModel.products.observe(viewLifecycleOwner, Observer {
            binding.isLoading = it.status == Status.LOADING
            when (it.status) {
                Status.SUCCESS -> {
                    binding.isEmpty = it.data?.isEmpty()
                    if (binding.isEmpty == false) {
                        val products: List<Product> = it.data!!
                        productsAdapter = ProductsAdapter(requireContext(), products) { productId ->
                            Log.d("ProductsFragment", "Product ID: $productId")
                        }
                        binding.productsList.apply {
                            layoutManager = GridLayoutManager(requireContext(), 2)
                            adapter = productsAdapter
                            setHasFixedSize(true)
                        }
                    }
                }
                Status.ERROR -> {
                    showSnackbar(binding.root, "Error occurred: ${ it.message }")
                }
            }
        })

        viewModel.getAllProducts(this.businessId)

        return binding.root
    }

}