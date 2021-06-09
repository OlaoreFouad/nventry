package dev.olaore.nventry.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentProductBinding
import dev.olaore.nventry.databinding.FragmentProductsBinding

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProductBinding.inflate(inflater)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.singleProductToolbar.apply {
            inflateMenu(R.menu.product_menu)
            setNavigationIcon(R.drawable.ic_back_white)
            setNavigationOnClickListener {
                (requireActivity() as AppCompatActivity).onBackPressed()
            }
            setOnMenuItemClickListener { menuItem ->
                when(menuItem.itemId) {
                    R.id.nav_edit_product -> {
                        Log.d("ProductFragment", "Edit Product Clicked!")
                        true
                    }
                    R.id.nav_share_product -> {
                        Log.d("ProductFragment", "Share Product Clicked!")
                        true
                    }
                    else  -> false
                }

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

}

