package dev.olaore.nventry.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import dev.olaore.nventry.R


class OnboardingAdapter(
    private val ctx: Context
) : PagerAdapter() {

    private lateinit var layoutInflater: LayoutInflater

    private val images = emptyList<Int>()
    private val headers = emptyList<String>()
    private val bodies = emptyList<String>()

    override fun getCount() = 3

    @SuppressLint("SetTextI18n")
    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val layoutInflater = LayoutInflater.from(ctx)
        val onboardingView = layoutInflater.inflate(R.layout.item_onboarding, container, false)

        val onboardingImage: ImageView = onboardingView.findViewById(R.id.onboarding_image)
        val onboardingHeader: TextView = onboardingView.findViewById(R.id.onboarding_heading)
        val onboardingBody: TextView = onboardingView.findViewById(R.id.onboarding_description)

        onboardingImage.setImageResource(R.drawable.ic_launcher_background)
        onboardingHeader.text = "The Heading $position"
//        onboardingBody.text = ctx.resources.getString(bodies[position])

        container.addView(onboardingView)

        return onboardingView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (view == (`object` as ConstraintLayout))
    }

}