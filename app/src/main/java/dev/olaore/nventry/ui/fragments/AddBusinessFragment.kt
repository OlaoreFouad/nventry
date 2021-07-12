package dev.olaore.nventry.ui.fragments

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentAddBusinessBinding
import dev.olaore.nventry.databinding.FragmentBusinessesBinding
import dev.olaore.nventry.models.network.NetworkBusiness
import dev.olaore.nventry.network.Network
import dev.olaore.nventry.network.Status
import dev.olaore.nventry.network.Storage
import dev.olaore.nventry.ui.viewmodels.UpsertBusinessViewModel
import dev.olaore.nventry.ui.views.UploadImageContainer
import dev.olaore.nventry.utils.Utils
import dev.olaore.nventry.utils.obtainViewModel
import java.io.File

class AddBusinessFragment : Fragment(), UploadImageContainer.Listener {

    private lateinit var binding: FragmentAddBusinessBinding
    private lateinit var viewModel: UpsertBusinessViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddBusinessBinding.inflate(
            inflater, container, false
        )

        binding.uploadImageContainer.addListener(this)
        binding.isLoading = false

        viewModel = obtainViewModel(UpsertBusinessViewModel::class.java)

        // subscribe to fileUpload event (only create business when
        // image has been uploaded successfully)
        viewModel.fileUploadComplete.observe(viewLifecycleOwner, Observer {
            // update UI loading state
            binding.isLoading = it.status == Status.LOADING
            when (it.status) {
                Status.ERROR -> {
                    // show error message if upload failed
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    // if successful, get image upload url
                    val downloadUrl = it.data?.uploadUrl
                    // create business
                    viewModel.createBusiness(
                        binding.businessNameEditText.editText!!.text.toString(),
                        binding.businessDescriptionEditText.editText!!.text.toString(),
                        downloadUrl!!
                    )
                }
            }
        })

        // subscribe to business creation event
        viewModel.businessCreated.observe(viewLifecycleOwner, Observer {
            binding.isLoading = it.status == Status.LOADING // update UI loading state
            when (it.status) {
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show() // show error if creation failed
                }
                Status.SUCCESS -> {
                    Log.d("AddBusinessFragment", "Business Created")
                    requireActivity().onBackPressed() // go back to main list once business created
                }
            }
        })

        // set on-click listener on add business button
        binding.addBusinessButton.setOnClickListener {
            // call addBusiness function
            addBusiness()
        }

        return binding.root

    }

    private fun addBusiness() {

        // get business names and descriptions
        val businessName = binding.businessNameEditText.editText?.text.toString()
        val businessDescription = binding.businessDescriptionEditText.editText?.text.toString()

        // ensure business names and description are not empty
        if (businessName.isEmpty() || businessDescription.isEmpty()) {
            Toast.makeText(requireContext(), "Kindly fill all fields", Toast.LENGTH_LONG).show()
            return
        }

        // ensure user has selected one image for business image/logo
        if (binding.uploadImageContainer.hasUploadedImage) {
            val uri = binding.uploadImageContainer.getCurrentImageUri()
            val fileId = Network.getRandomId()

            // upload image to firebase storage
            viewModel.uploadImage(businessName, fileId, uri)
        } else {
            // inform user of the necessity to select an image
            Toast.makeText(requireContext(), "Please upload a business logo/image", Toast.LENGTH_LONG).show()
        }

    }

    override fun onRequestImageUpload() {
        val selectFileIntent = Intent(Intent.ACTION_GET_CONTENT)
        selectFileIntent.type = "image/*"
        startActivityForResult(selectFileIntent, Utils.REQUEST_FILE_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Utils.REQUEST_FILE_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                val imageUri = data.data
                imageUri?.let {
                    binding.uploadImageContainer.setImage(imageUri)
                }
            }
        }
    }

}