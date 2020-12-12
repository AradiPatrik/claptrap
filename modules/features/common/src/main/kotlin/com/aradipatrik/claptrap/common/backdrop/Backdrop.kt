package com.aradipatrik.claptrap.common.backdrop

import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.aradipatrik.claptrap.mvi.ClapTrapFragment

interface Backdrop {
  val backdropNavController: NavController

  fun switchMenu(menuFragmentClass: Class<out Fragment>)
  fun clearMenu()
  fun back()
}

val ClapTrapFragment<*, *, *, *>.backdrop
  get() = parentFragment!!.parentFragment as Backdrop
