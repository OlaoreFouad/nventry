package dev.olaore.nventry.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentProductsBinding

class ProductsFragment : Fragment() {

    private lateinit var binding: FragmentProductsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProductsBinding.inflate(
            inflater, container, false
        )

        binding.addProductButton.setOnClickListener {
            it.findNavController().navigate(
                R.id.action_products_to_upsertProductFragment
            )
        }

        return binding.root
    }

}