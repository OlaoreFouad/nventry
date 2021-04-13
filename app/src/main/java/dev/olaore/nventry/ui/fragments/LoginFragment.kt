package dev.olaore.nventry.ui.fragments

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentLoginBinding
import dev.olaore.nventry.ui.viewmodels.LoginViewModel
import dev.olaore.nventry.utils.CustomClickableSpan
import dev.olaore.nventry.utils.createSpannableText
import dev.olaore.nventry.utils.obtainViewModel

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        viewModel = obtainViewModel(LoginViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createSignupSpannable()

        binding.loginButton.setOnClickListener {
            viewModel.login()
        }
    }

    private fun createSignupSpannable() {
        val signupTextIndex = resources.getString(R.string.don_t_have_an_account_sign_up).indexOf("S")
        val signupTextString = SpannableString(resources.getString(R.string.don_t_have_an_account_sign_up))

        val clickableSpan = CustomClickableSpan {
            navigateToSignupScreen()
        }

        signupTextString.setSpan(
            clickableSpan, signupTextIndex, signupTextIndex + 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.dontHaveAnAccountText.apply {
            text = signupTextString
            isClickable = true
            movementMethod = LinkMovementMethod.getInstance()
        }

    }

    private fun navigateToSignupScreen() {
        findNavController().navigate(
            LoginFragmentDirections.actionLoginFragmentToSignupFragment()
        )
    }

}

