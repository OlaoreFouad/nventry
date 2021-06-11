package dev.olaore.nventry.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentProductBinding
import dev.olaore.nventry.databinding.FragmentProductsBinding
import dev.olaore.nventry.databinding.SingleIndicatorBinding
import dev.olaore.nventry.models.network.Product
import dev.olaore.nventry.network.Status
import dev.olaore.nventry.ui.adapters.OnboardingAdapter
import dev.olaore.nventry.ui.adapters.ProductImagesAdapter
import dev.olaore.nventry.ui.viewmodels.BusinessViewModel
import dev.olaore.nventry.ui.viewmodels.ProductViewModel
import dev.olaore.nventry.utils.obtainViewModel
import dev.olaore.nventry.utils.showSnackbar

class ProductFragment : Fragment() {

    private lateinit var binding: FragmentProductBinding
    private lateinit var businessViewModel: BusinessViewModel
    private lateinit var viewModel: ProductViewModel

    private lateinit var product: Product
    private lateinit var businessId: String

    private lateinit var mProductImagesAdapter: ProductImagesAdapter
    private lateinit var mOnPageChangeListener: ViewPager.OnPageChangeListener
    private var imageBindings = mutableListOf<SingleIndicatorBinding>();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProductBinding.inflate(inflater)

        setHasOptionsMenu(true)
        requireActivity().let {
            businessViewModel = ViewModelProvider(it).get(BusinessViewModel::class.java)
            this.businessId = businessViewModel.businessId
        }
        viewModel = obtainViewModel(ProductViewModel::class.java)
        val args: ProductFragmentArgs by navArgs()
        viewModel.productId = args.productId
        viewModel.product.observe(viewLifecycleOwner, Observer { product ->
            binding.isLoading = product.status == Status.LOADING
            when (product.status) {
                Status.ERROR -> {
                    showSnackbar(binding.root, "Error occurred: ${ product.message }")
                }
                Status.SUCCESS -> {
                    this.product = product.data!!
                    binding.product = product.data
                    setupViewPager()
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProduct()

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

    private fun setupViewPager() {
        mProductImagesAdapter = ProductImagesAdapter(requireActivity().applicationContext, binding.product!!.imageUrls)

        mOnPageChangeListener = object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                setCurrentItem(position)
            }
        }

        binding.apply {
            productImagesViewPager.addOnPageChangeListener(mOnPageChangeListener)
            productImagesViewPager.adapter = mProductImagesAdapter
        }

        this.product.imageUrls.forEach { _ ->
            val lBinding = SingleIndicatorBinding.inflate(layoutInflater)
            lBinding.current = false
            val textView = lBinding.root as TextView
            this.imageBindings.add(lBinding)
            val indicatorSize = requireContext().resources.getDimension(R.dimen.indicator_size_lg).toInt()
            val indicatorMarginSize = requireContext().resources.getDimension(R.dimen.indicator_margin_size).toInt()
            val params = LinearLayout.LayoutParams(indicatorSize, indicatorSize)
            params.setMargins(indicatorMarginSize,0, indicatorMarginSize,0);
            textView.layoutParams = params
            binding.imageIndicatorsContainer.addView(textView)
        }
        setCurrentItem(0)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setCurrentItem(currentIndex: Int) {
        invalidateActiveStates()
        imageBindings[currentIndex].current = true
    }

    private fun invalidateActiveStates() {
        imageBindings.forEach {
            it.current = false
        }
    }

}

