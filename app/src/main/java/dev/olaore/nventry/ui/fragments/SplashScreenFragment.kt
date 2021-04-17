package dev.olaore.nventry.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import dev.olaore.nventry.R
import dev.olaore.nventry.utils.Prefs

class SplashScreenFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val authenticated = Prefs.isUserAuthenticated(requireContext())
        val action = if (authenticated) {
            SplashScreenFragmentDirections.actionSplashScreenFragmentToLoginFragment()
        } else {
            SplashScreenFragmentDirections.actionSplashScreenFragmentToOnboardingFragment()
        }
        Handler().postDelayed({
            findNavController().navigate(action)
        }, 4000)
    }

}