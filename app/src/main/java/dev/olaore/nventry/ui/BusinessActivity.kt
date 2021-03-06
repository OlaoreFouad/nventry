package dev.olaore.nventry.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.olaore.nventry.R
import dev.olaore.nventry.ui.viewmodels.BusinessViewModel
import dev.olaore.nventry.utils.Utils
import dev.olaore.nventry.utils.obtainViewModel
import dev.olaore.nventry.utils.showSnackbar

class BusinessActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var businessId: String

    private lateinit var viewModel: BusinessViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_business)

        bottomNavigationView = findViewById(R.id.business_bottom_nav)

        this.businessId = intent.extras!!.getString(Utils.BUSINESS_ID, "")
        viewModel = obtainViewModel(BusinessViewModel::class.java)

        viewModel.businessId = this.businessId

        if (this.businessId.isEmpty()) {
            showSnackbar(this.bottomNavigationView, "Business ID was not passed in!")
            finish()
        }

        setupNavigation()

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = (supportFragmentManager.findFragmentById(R.id.business_fragment_container)
                as NavHostFragment).navController
        return navController.navigateUp()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.business_fragment_container) as NavHostFragment
        NavigationUI.setupWithNavController(bottomNavigationView, navHostFragment.navController)
    }
}