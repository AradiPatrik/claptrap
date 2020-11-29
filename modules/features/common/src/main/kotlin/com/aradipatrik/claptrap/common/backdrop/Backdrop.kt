package com.aradipatrik.claptrap.common.backdrop

import androidx.fragment.app.Fragment
import com.aradipatrik.claptrap.mvi.ClapTrapFragment

interface Backdrop {
  fun switchMenu(menuFragment: Fragment)
  fun clearMenu()
  fun back()
}

val ClapTrapFragment<*, *, *, *>.backdrop
  get() = parentFragment!!.parentFragment as Backdrop
