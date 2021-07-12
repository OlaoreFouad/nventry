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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import dev.olaore.nventry.databinding.FragmentUpsertProductBinding
import dev.olaore.nventry.models.network.Product
import dev.olaore.nventry.network.Network
import dev.olaore.nventry.network.Status
import dev.olaore.nventry.ui.viewmodels.BusinessViewModel
import dev.olaore.nventry.ui.viewmodels.UpsertProductViewModel
import dev.olaore.nventry.ui.views.UploadImagesContainer
import dev.olaore.nventry.utils.Utils
import dev.olaore.nventry.utils.asURIs
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
    private lateinit var productToBeEdited: Product

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpsertProductBinding.inflate(
            inflater, container, false
        )

        viewModel = obtainViewModel(UpsertProductViewModel::class.java)

        binding.isLoading = false
        binding.editMode = false

        binding.uploadImagesContainer.addListener(this)

        val args: UpsertProductFragmentArgs by navArgs()
        args.product?.let { product ->
            binding.editMode = true
            viewModel.setProduct(product)
            this.productToBeEdited = product
            binding.uploadImagesContainer.setImages(this.productToBeEdited.imageUrls)
        }

        viewModel = obtainViewModel(UpsertProductViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        requireActivity().let {
            businessViewModel = ViewModelProvider(it).get(BusinessViewModel::class.java)
            this.businessId = businessViewModel.businessId
        }

        viewModel.productCreated.observe(viewLifecycleOwner, Observer {
            binding.isLoading = it.status == Status.LOADING // update loading state on UI
            when (it.status) {
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show() // show failure error
                }
                Status.SUCCESS -> {
                    showSnackbar(binding.root, "Product created successfully!") // show success notification
                    requireActivity().onBackPressed() // return to previous page
                }
            }
        })
        viewModel.productUpdated.observe(viewLifecycleOwner, Observer {
            binding.isLoading = it.status == Status.LOADING
            when (it.status) {
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    showSnackbar(binding.root, "Product updated successfully!")
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

                    this.handleProductUpsert()
                }
            }
        })

        // set on-click listener for add/edit product button
        binding.upsertProductButton.setOnClickListener {
            // start by uploading the images selected by the user
            uploadProductImages()
        }

        return binding.root
    }

    private fun handleProductUpsert() {
        if (this.productImages.size == this.uploadedImages.size) {
            this.upsertProduct()
        } else if (this.currentProductIndex != this.productImages.size) {
            this.uploadNextImage(productName, this.productImages[currentProductIndex])
        }
    }

    private fun uploadProductImages() {

        // collect all data
        val name = binding.productName.editText?.text.toString()
        val desc = binding.productDescription.editText?.text.toString()
        val sharingText = binding.productSharingText.editText?.text.toString()
        val price = binding.productPrice.editText?.text.toString()
        val quantity = binding.productQuantity.editText?.text.toString()
        val category = ""

        // ensure no data is left empty before forwarding to next block
        if (name.isEmpty() || desc.isEmpty() || sharingText.isEmpty() || price.isEmpty() || quantity.isEmpty()) {
            showSnackbar(binding.root, "Please fill all fields") // show error if some fields are empty
            return
        }

        this.productName = name
        this.productImages = binding.uploadImagesContainer.getImages()
        // ensure user uploads at least one image
        if (productImages.isEmpty()) {
            showSnackbar(binding.root, "Please upload at least one image")
            return
        }

        // check if product is being edited or added
        if (binding.editMode!!) {
            this.uploadEditedImages(productName) // upload edited images (new ones only)
        } else {
            this.uploadNextImage(productName, this.productImages[0]) // upload new images since product is a new one.
        }

    }

    private fun uploadEditedImages(name: String) {

        val originalProductImages = this.productToBeEdited.imageUrls.asURIs()
        this.productImages = binding.uploadImagesContainer.getImages()

        var imagesChanged = false

        this.currentProductIndex = this.productImages.size - 1
        this.productImages.forEach { newProductImage ->
            val imageIndex = originalProductImages.indexOf(newProductImage)
            if (imageIndex == -1) {
                imagesChanged = true
                this.uploadNextImage(name, newProductImage)
            } else {
                this.uploadedImages.add(newProductImage.toString())
            }
        }

        if (!imagesChanged) {
            this.upsertProduct()
        }

    }

    private fun uploadNextImage(name: String, uri: Uri) {
        viewModel.uploadImage(name, Network.getRandomId(), uri)
    }

    private fun upsertProduct() {
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

        if (binding.editMode!!) {
            newProduct.id = this.productToBeEdited.id
            viewModel.updateProduct(newProduct)
        } else {
            viewModel.createProduct(newProduct)
        }

    }

    // request image upload
    override fun onRequestImageUpload() {
        // set intent to get a file from file system
        val selectFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        // set file type to image
        selectFileIntent.type = "image/*"
        // launch intent to get file (image)
        startActivityForResult(selectFileIntent, Utils.REQUEST_FILE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // receive data in callback
        if (requestCode == Utils.REQUEST_FILE_CODE && resultCode == Activity.RESULT_OK) {
            // ensure data returned is not NULL
            if (data != null) {
                // get the file
                val imageUri = data.data
                imageUri?.let {
                    // add the image to UI once Uniform Resource Identifier (URI) is not NULL
                    binding.uploadImagesContainer.addImage(imageUri)
                }
            }
        }
    }

}