package dev.olaore.nventry.ui.fragments

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentLoginBinding
import dev.olaore.nventry.utils.CustomClickableSpan
import dev.olaore.nventry.utils.createSpannableText

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dontHaveAnAccountText.text = createSpannableText(
            binding.dontHaveAnAccountText.text.toString(), "Sign up"
        )
    }

}

