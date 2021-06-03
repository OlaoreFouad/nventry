package dev.olaore.nventry.ui.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentUpsertProductBinding
import dev.olaore.nventry.models.network.Product
import dev.olaore.nventry.network.Network
import dev.olaore.nventry.network.Status
import dev.olaore.nventry.ui.viewmodels.BusinessViewModel
import dev.olaore.nventry.ui.viewmodels.UpsertProductViewModel
import dev.olaore.nventry.ui.views.UploadImagesContainer
import dev.olaore.nventry.utils.Utils
import dev.olaore.nventry.utils.obtainViewModel
import dev.olaore.nventry.utils.showSnackbar

class UpsertProductFragment : Fragment(), UploadImagesContainer.Listener {

    private lateinit var binding: FragmentUpsertProductBinding
    private var currentProductIndex = 0
    private var uploadedImages = mutableListOf<String>()
    private lateinit var productImages: List<Uri>
    private lateinit var viewModel: UpsertProductViewModel
    private lateinit var productName: String

    private lateinit var businessId: String

    private lateinit var businessViewModel: BusinessViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpsertProductBinding.inflate(
            inflater, container, false
        )
        binding.isLoading = false
        viewModel = obtainViewModel(UpsertProductViewModel::class.java)
        binding.uploadImagesContainer.addListener(this)

        requireActivity().let {
            businessViewModel = ViewModelProvider(it).get(BusinessViewModel::class.java)
            this.businessId = businessViewModel.businessId
        }

        viewModel.productCreated.observe(viewLifecycleOwner, Observer {
            binding.isLoading = it.status == Status.LOADING
            when (it.status) {
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    showSnackbar(binding.root, "Product created successfully!")
                    requireActivity().onBackPressed()
                }
            }
        })

        viewModel.fileUploadComplete.observe(viewLifecycleOwner, Observer {
            binding.isLoading = it.status == Status.LOADING
            when (it.status) {
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {

                    val downloadUrl = it.data?.uploadUrl!!
                    this.uploadedImages.add(downloadUrl)
                    currentProductIndex++
                    if (this.productImages.size == this.uploadedImages.size) {
                        this.createProduct()
                    } else {
                        this.uploadNextImage(productName, this.productImages[currentProductIndex])
                    }
                }
            }
        })

        binding.addProductButton.setOnClickListener {
            uploadProductImages()
        }

        return binding.root
    }

    private fun uploadProductImages() {

        val name = binding.productName.editText?.text.toString()
        val desc = binding.productDescription.editText?.text.toString()
        val sharingText = binding.productSharingText.editText?.text.toString()
        val price = binding.productPrice.editText?.text.toString()
        val quantity = binding.productQuantity.editText?.text.toString()
        val category = ""

        if (name.isEmpty() || desc.isEmpty() || sharingText.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
            showSnackbar(binding.root, "Please fill all fields")
            return
        }

        this.productName = name
        this.productImages = binding.uploadImagesContainer.getImages()
        if (productImages.isEmpty()) {
            showSnackbar(binding.root, "Please upload at least one image")
            return
        }

        this.uploadNextImage(productName, this.productImages[0])

    }

    private fun uploadNextImage(name: String, uri: Uri) {
        viewModel.uploadImage(name, Network.getRandomId(), uri)
    }

    private fun createProduct() {
        val newProduct = Product(
            "",
            this.businessId,
            binding.productName.editText?.text.toString(),
            binding.productPrice.editText?.text.toString().toFloat(),
            binding.productDescription.editText?.text.toString(),
            binding.productSharingText.editText?.text.toString(),
            "",
            binding.productQuantity.editText?.text.toString().toInt(),
            this.uploadedImages,
            System.currentTimeMillis()
        )

        viewModel.createProduct(newProduct)

    }

    override fun onRequestImageUpload() {
        val selectFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        selectFileIntent.type = "image/*"
        startActivityForResult(selectFileIntent, Utils.REQUEST_FILE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Utils.REQUEST_FILE_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                val imageUri = data.data
                imageUri?.let {
                    binding.uploadImagesContainer.addImage(imageUri)
                }
            }
        }
    }

}