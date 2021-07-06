package dev.olaore.nventry.ui.fragments

import android.content.DialogInterface
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentProductBinding
import dev.olaore.nventry.databinding.SingleIndicatorBinding
import dev.olaore.nventry.models.network.Product
import dev.olaore.nventry.network.Status
import dev.olaore.nventry.ui.adapters.ProductImagesAdapter
import dev.olaore.nventry.ui.adapters.SelectImagesAdapter
import dev.olaore.nventry.ui.viewmodels.BusinessViewModel
import dev.olaore.nventry.ui.viewmodels.ProductViewModel
import dev.olaore.nventry.utils.*

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

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.quantity = 0
        binding.isUpdatingQuantity = false

        binding.copyDescription.setOnClickListener { copyToClipboard("Description", this.product.description) }
        binding.copySharingPromo.setOnClickListener { copyToClipboard("Sharing Promo", this.product.sharingText) }
        binding.copyWebLink.setOnClickListener { copyToClipboard("Web Link", this.product.webLink) }

        val args: ProductFragmentArgs by navArgs()
        viewModel.productId = args.productId
        viewModel.product.observe(viewLifecycleOwner, Observer { product ->
            binding.isLoading = product.status == Status.LOADING
            when (product.status) {
                Status.ERROR -> {
                    showSnackbar(binding.root, "Error occurred: ${product.message}")
                }
                Status.SUCCESS -> {
                    this.product = product.data!!
                    binding.product = product.data
                    binding.quantity = this.product.quantity
                    setupViewPager()
                }
            }
        })
        viewModel.productUpdated.observe(viewLifecycleOwner, Observer {
            binding.isUpdatingQuantity = it.status == Status.LOADING
            when (it.status) {
                Status.ERROR -> {
                    showSnackbar(binding.root, "Error occurred: ${it.message}")
                }
                Status.SUCCESS -> {
                    viewModel.getProduct()
                    showSnackbar(binding.root, "Quantity updated successfully!")
                }
            }
        })
        viewModel.productDeleted.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.ERROR -> {
                    showSnackbar(binding.root, "Error occurred: ${it.message}")
                }
                Status.SUCCESS -> {
                    showSnackbar(binding.root, "Product deleted successfully!")
                    requireActivity().onBackPressed()
                }
            }
        })

        binding.addProductQuantity.setOnClickListener { this.increaseQuantity() }
        binding.removeProductQuantity.setOnClickListener { this.decreaseQuantity() }
        binding.savePriceQuantity.setOnClickListener { this.updateQuantity() }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        imageBindings.clear()
    }

    private fun checkPermissions() {
        var result: Int
        val requiredPermissions = mutableListOf<String>()

        imagePermissions.forEach { permission ->
            result = ContextCompat.checkSelfPermission(requireContext(), permission)
            if (result != PackageManager.PERMISSION_GRANTED) {
                requiredPermissions.add(permission)
            }
        }

        if (requiredPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                requireActivity(), requiredPermissions.toTypedArray(), 100
            )
        } else {
            showDialog()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            when {
                grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED -> {
                    showDialog()
                }
            }
        }

    }

    private fun share(index: Int) {
        shareProduct(requireContext(), product.sharingText, this.mProductImagesAdapter.views[index], "Share ${ product.name }")
    }

    fun copyToClipboard(label: String, text: String) {
        requireContext().copyToClipboard(text) {
            showSnackbar(binding.root, "$label copied to clipboard!")
        }
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
                when (menuItem.itemId) {
                    R.id.nav_edit_product -> {
                        findNavController().navigate(
                            ProductFragmentDirections.actionProductFragmentToUpsertProductFragment(
                                product
                            )
                        )
                        true
                    }
                    R.id.nav_share_product -> {
                        checkPermissions()
                        true
                    }
                    R.id.nav_delete_product -> {
                        initializeDelete()
                        true
                    }
                    else -> false
                }

            }
        }

    }

    private fun setupViewPager() {
        mProductImagesAdapter =
            ProductImagesAdapter(requireActivity().applicationContext, binding.product!!.imageUrls)

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

        if (this.imageBindings.size != this.product.imageUrls.size) {
            binding.imageIndicatorsContainer.removeAllViews()
            this.product.imageUrls.forEach { _ ->
                val lBinding = SingleIndicatorBinding.inflate(layoutInflater)
                lBinding.current = false
                val textView = lBinding.root as TextView
                this.imageBindings.add(lBinding)
                val indicatorSize =
                    requireContext().resources.getDimension(R.dimen.indicator_size_lg).toInt()
                val indicatorMarginSize =
                    requireContext().resources.getDimension(R.dimen.indicator_margin_size).toInt()
                val params = LinearLayout.LayoutParams(indicatorSize, indicatorSize)
                params.setMargins(indicatorMarginSize, 0, indicatorMarginSize, 0);
                textView.layoutParams = params
                binding.imageIndicatorsContainer.addView(textView)
            }
        }
        setCurrentItem(0)
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

    private fun increaseQuantity() {
        binding.quantity = binding.quantity!!.plus(1)
    }

    private fun decreaseQuantity() {
        if (binding.quantity != 0) {
            binding.quantity = binding.quantity!!.minus(1)
        }
    }

    private fun updateQuantity() {
        viewModel.updateQuantity(binding.quantity!!)
    }

    private fun initializeDelete() {

        val alertDialog: AlertDialog = AlertDialog.Builder(requireContext())
            .setTitle("Delete ${product.name}")
            .setMessage("Are you sure you want to delete this product?")
            .setNegativeButton("No") { dInterface, _ ->
                dInterface.dismiss()
            }
            .setPositiveButton("Yes") { dInterface, _ ->
                dInterface.dismiss()
                viewModel.deleteProduct(this.product.id)
            }
            .create()

        alertDialog.show()
    }

    private fun getBitmapAtPagerIndex(index: Int): Bitmap {

        val imageView = this.mProductImagesAdapter.views[index]
        return (imageView.drawable as BitmapDrawable).bitmap

    }

    private fun showDialog() {

        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_select_image, null)
        val selectImagesList: RecyclerView = view.findViewById(R.id.selectImagesList)
        val selectImagesAdapter = SelectImagesAdapter(requireContext(), product.imageUrls)

        selectImagesList.apply {
            adapter = selectImagesAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        }

        val selectImagesDialogBuilder = AlertDialog.Builder(requireContext())
        selectImagesDialogBuilder.setTitle("Select Image To Share")
            .setView(view)
            .setPositiveButton("Select Image"){ dialog, _ ->
                val index = selectImagesAdapter.selectedImageIndex
                share(index)
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }

        selectImagesDialogBuilder.show()


    }
}

