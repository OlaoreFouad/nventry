package dev.olaore.nventry.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dev.olaore.nventry.databinding.FragmentProfileBinding
import dev.olaore.nventry.ui.viewmodels.ProfileViewModel
import dev.olaore.nventry.utils.Prefs
import dev.olaore.nventry.utils.obtainViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater)

        viewModel = obtainViewModel(ProfileViewModel::class.java)
        viewModel.username.observe(viewLifecycleOwner, Observer {
            binding.profileUsername.text = it
        })

        viewModel.getUsername(Prefs.getUserId(requireContext())!!)

        return binding.root
    }

}