package dev.olaore.nventry.ui.fragments

import android.os.Bundle
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
import dev.olaore.nventry.databinding.FragmentSignupBinding
import dev.olaore.nventry.ui.viewmodels.SignupViewModel
import dev.olaore.nventry.utils.CustomClickableSpan
import dev.olaore.nventry.utils.createSpannableText
import dev.olaore.nventry.utils.obtainViewModel

class SignupFragment : Fragment() {

    private lateinit var binding: FragmentSignupBinding
    private lateinit var viewModel: SignupViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(
            inflater, container, false
        )

        viewModel = obtainViewModel(SignupViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createLoginSpannable()

        binding.signupButton.setOnClickListener {
            viewModel.createAccount()
        }

    }

    private fun createLoginSpannable() {
        val loginTextIndex = resources.getString(R.string.already_have_an_account_login).indexOf("L")
        val loginTextString = SpannableString(resources.getString(R.string.already_have_an_account_login))

        val clickableSpan = CustomClickableSpan {
            navigateToLoginScreen()
        }

        loginTextString.setSpan(
            clickableSpan, loginTextIndex, loginTextIndex + 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        binding.alreadyHaveAnAccountText.apply {
            text = loginTextString
            isClickable = true
            movementMethod = LinkMovementMethod.getInstance()
        }

    }

    private fun navigateToLoginScreen() {
        findNavController().navigate(
            SignupFragmentDirections.actionSignupFragmentToLoginFragment()
        )
    }

}