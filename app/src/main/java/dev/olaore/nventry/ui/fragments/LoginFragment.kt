package dev.olaore.nventry.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import dev.olaore.nventry.MainActivity
import dev.olaore.nventry.R
import dev.olaore.nventry.databinding.FragmentLoginBinding
import dev.olaore.nventry.models.database.DatabaseUser
import dev.olaore.nventry.network.Status
import dev.olaore.nventry.ui.viewmodels.LoginViewModel
import dev.olaore.nventry.utils.*

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

        val userId = Prefs.getUserId(requireContext())
        if (!userId.isNullOrEmpty()) {
            viewModel.getDatabaseUser(userId)
        }

        viewModel.loggedInUser.observe(viewLifecycleOwner, Observer {

            binding.isLoading = it.status == Status.LOADING

            when (it.status) {

                Status.SUCCESS -> {

                    val user: DatabaseUser = it.data!!
                    showSnackbar(binding.isLoadingProgress, "Login successful!")
                    Prefs.saveAuthenticatedUser(requireContext(), user.userId)
                    viewModel.saveUserDetailsToDatabase(user)
                    openHome()

                }
                Status.ERROR -> {
                    showSnackbar(binding.isLoadingProgress, "Error occurred while logging you in: ${ it.message }")
                }

            }

        })

        binding.loginButton.setOnClickListener {
            closeKeyboard()
            viewModel.login()
        }
    }

    private fun closeKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = requireActivity().currentFocus

        if (view == null) {
            view = View(requireContext())
        }

        imm.hideSoftInputFromWindow(view.windowToken, 0)

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

    private fun openHome() {
        val homeIntent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(homeIntent)
        requireActivity().finish()
    }

}

