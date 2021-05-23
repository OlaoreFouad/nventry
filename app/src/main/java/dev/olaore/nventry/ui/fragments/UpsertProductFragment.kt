package dev.olaore.nventry.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentUpsertProductBinding
import dev.olaore.nventry.ui.views.UploadImagesContainer
import dev.olaore.nventry.utils.Utils

class UpsertProductFragment : Fragment(), UploadImagesContainer.Listener {

    private lateinit var binding: FragmentUpsertProductBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpsertProductBinding.inflate(
            inflater, container, false
        )
        binding.isLoading = false

        binding.uploadImagesContainer.addListener(this)

        return binding.root
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