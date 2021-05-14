package dev.olaore.nventry.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentEditBusinessBinding
import dev.olaore.nventry.models.domain.Business
import dev.olaore.nventry.network.Network
import dev.olaore.nventry.network.Status
import dev.olaore.nventry.ui.viewmodels.UpsertBusinessViewModel
import dev.olaore.nventry.ui.views.UploadImageContainer
import dev.olaore.nventry.utils.Utils
import dev.olaore.nventry.utils.obtainViewModel

class EditBusinessFragment : Fragment(), UploadImageContainer.Listener {

    private lateinit var binding: FragmentEditBusinessBinding
    private lateinit var currentBusiness: Business
    private lateinit var viewModel: UpsertBusinessViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEditBusinessBinding.inflate(
            inflater, container, false
        )

        binding.uploadImageContainer.addListener(this)

        viewModel = obtainViewModel(UpsertBusinessViewModel::class.java)

        viewModel.fileUploadComplete.observe(viewLifecycleOwner, Observer {
            binding.isLoading = it.status == Status.LOADING
            when (it.status) {

                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    val downloadUrl = it.data?.uploadUrl
                    saveCurrentBusiness(downloadUrl!!)
                }

            }
        })

        viewModel.businessUpdated.observe(viewLifecycleOwner, Observer {

            binding.isLoading = it.status == Status.LOADING

            when (it.status) {
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                Status.SUCCESS -> {
                    Log.d("EditBusinessFragment", "Business Updated")
                    requireActivity().onBackPressed()
                }
            }

        })

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args: EditBusinessFragmentArgs by navArgs()
        this.currentBusiness = args.business

        this.populateForm()

        binding.editBusinessButton.setOnClickListener {
            performEdit()
        }

    }

    private fun saveCurrentBusiness(_logoUrl: String) {
        this.currentBusiness.apply {
            name = binding.businessNameEditText.editText?.text.toString()
            description = binding.businessDescriptionEditText.editText?.text.toString()
            logoUrl = _logoUrl
        }
        viewModel.updateBusiness(this.currentBusiness)
    }

    private fun performEdit() {
        val businessName = binding.businessNameEditText.editText?.text.toString()
        val businessDescription = binding.businessDescriptionEditText.editText?.text.toString()

        if (businessName.isEmpty() || businessDescription.isEmpty()) {
            Toast.makeText(requireContext(), "Kindly fill all fields", Toast.LENGTH_LONG).show()
            return
        }

        if (binding.uploadImageContainer.hasUploadedImage) {
            val uri = binding.uploadImageContainer.getCurrentImageUri()
            val fileId = Network.getRandomId()

            viewModel.uploadImage(businessName, fileId, uri)
        } else {
            saveCurrentBusiness(this.currentBusiness.logoUrl)
        }
    }

    private fun populateForm() {

        binding.apply {
            businessNameEditText.editText?.setText(currentBusiness.name)
            businessDescriptionEditText.editText?.setText(currentBusiness.description)
            uploadImageContainer.setImage(currentBusiness.logoUrl, true)
        }

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
                    binding.uploadImageContainer.setImage(imageUri)
                }
            }
        }
    }

}