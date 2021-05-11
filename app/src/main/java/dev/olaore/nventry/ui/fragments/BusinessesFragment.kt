package dev.olaore.nventry.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentBusinessesBinding

class BusinessesFragment : Fragment() {

    private lateinit var binding: FragmentBusinessesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBusinessesBinding.inflate(
            inflater, container, false
        )

        binding.addBusinessButton.setOnClickListener {
            it.findNavController().navigate(
                R.id.action_businesses_to_addBusinessFragment
            )
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        Log.d("AddBusinessFragment", "Got back here successfully!")
    }

}