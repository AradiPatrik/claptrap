package com.aradipatrik.claptrap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.addCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.aradipatrik.claptrap.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import timber.log.Timber

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {
  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val nestedNavHostFragment = childFragmentManager
      .findFragmentById(R.id.child_host) as NavHostFragment
    val nestedNavController = nestedNavHostFragment.navController

    requireActivity().onBackPressedDispatcher.addCallback(this) {
      if (nestedNavController.previousBackStackEntry != null) {
        nestedNavController.popBackStack()
      } else {
        isEnabled = false
        requireActivity().onBackPressed()
        isEnabled = true
      }
    }
  }
}
