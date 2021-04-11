package dev.olaore.nventry.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import dev.olaore.nventry.databinding.FragmentOnboardingBinding
import dev.olaore.nventry.ui.adapters.OnboardingAdapter

class OnboardingFragment : Fragment() {

    private lateinit var binding: FragmentOnboardingBinding
    private lateinit var mOnboardingAdapter: OnboardingAdapter
    private lateinit var mOnPageChangeListener: ViewPager.OnPageChangeListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        binding.currentPage = 0

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mOnboardingAdapter = OnboardingAdapter(requireActivity().applicationContext)

        mOnPageChangeListener = object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                binding.currentPage = position
            }
        }

        binding.skipButton.setOnClickListener {
            Toast.makeText(requireContext(), "Skips to MainActivity at this point!", Toast.LENGTH_LONG).show()
        }

        binding.backButton.setOnClickListener {
            showPreviousPage(binding.onboardingViewPager.currentItem)
        }

        binding.nextButton.setOnClickListener {
            showNextPage(binding.onboardingViewPager.currentItem)
        }

        binding.apply {
            onboardingViewPager.addOnPageChangeListener(mOnPageChangeListener)
            onboardingViewPager.adapter = mOnboardingAdapter
            currentPage = onboardingViewPager.currentItem
        }

    }

    private fun showPreviousPage(currentPosition: Int) {
        binding.onboardingViewPager.setCurrentItem(currentPosition - 1, true)
    }

    private fun showNextPage(currentPosition: Int) {
        binding.onboardingViewPager.setCurrentItem(currentPosition + 1, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.onboardingViewPager.removeOnPageChangeListener(mOnPageChangeListener)
    }

}