package dev.olaore.nventry.ui.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import dev.olaore.nventry.databinding.FragmentSignupBinding
import dev.olaore.nventry.models.database.DatabaseUser
import dev.olaore.nventry.network.Status
import dev.olaore.nventry.ui.viewmodels.SignupViewModel
import dev.olaore.nventry.utils.*
import java.util.*

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

        viewModel.createdAccount.observe(viewLifecycleOwner, Observer {
            binding.isLoading = it.status == Status.LOADING // update loading state
            when (it.status) {
                Status.SUCCESS -> {
                    val user: DatabaseUser? = it.data // get user data
                    showSnackbar(binding.isLoadingProgress, "User created successfully!")
                    viewModel.saveUserDetailsToDatabase(user!!)
                    Prefs.saveAuthenticatedUser(requireContext(), user.userId) // save user details to preferences
                    openHome() // open home screen
                }
                Status.ERROR -> {
                    // show alert error if an error event is emitted.
                    showSnackbar(binding.isLoadingProgress, "Error occurred while creating account: "
                            + it.message)
                }
            }

        })

        // set on-click listener for signup button
        binding.signupButton.setOnClickListener {
            // close keyboard
            closeKeyboard()
            // call createaccount logic in Signup ViewModel
            viewModel.createAccount()
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

    private fun openHome() {
        val homeIntent = Intent(requireActivity(), MainActivity::class.java)
        startActivity(homeIntent)
        requireActivity().finish()
    }

}