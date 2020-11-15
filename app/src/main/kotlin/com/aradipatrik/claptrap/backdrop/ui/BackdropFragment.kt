package com.aradipatrik.claptrap.backdrop.ui

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.aradipatrik.claptrap.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChromeFragment : Fragment(R.layout.fragment_main) {
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
