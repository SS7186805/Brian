package com.brian.views.activities

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.brian.R
import com.brian.base.ScopedActivity
import com.brian.databinding.ActivityAccountHandlerBinding

class AccountHandlerActivity : ScopedActivity() {


    lateinit var mBinding: ActivityAccountHandlerBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_account_handler)
        navController = setNavigationController()

    }

    private fun setNavigationController(): NavController {
        return Navigation.findNavController(this, R.id.main_nav_fragment)
    }


    override fun onBackPressed() {
        if (!navController.navigateUp()) {
            super.onBackPressed()
        }
    }
}
