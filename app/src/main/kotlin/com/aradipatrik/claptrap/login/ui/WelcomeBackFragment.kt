package com.aradipatrik.claptrap.login.ui

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aradipatrik.claptrap.R
import com.aradipatrik.claptrap.databinding.FragmentWelcomeBackBinding
import com.aradipatrik.claptrap.domain.User
import com.aradipatrik.claptrap.login.model.*
import com.aradipatrik.claptrap.login.model.Mappers.fromGoogleCredentials
import com.aradipatrik.claptrap.login.model.WelcomeBackViewEffect.NavigateToMainScreen
import com.aradipatrik.claptrap.login.model.WelcomeBackViewEffect.ShowSignInWithGoogleOAuthFlow
import com.aradipatrik.claptrap.login.model.WelcomeBackViewEvent.SignInSuccessful
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.mvi.Flows.launchInWhenResumed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import ru.ldralighieri.corbind.view.clicks

@AndroidEntryPoint
class WelcomeBackFragment : ClapTrapFragment<
  WelcomeBackViewState,
  WelcomeBackViewEvent,
  WelcomeBackViewEffect,
  FragmentWelcomeBackBinding>(
  R.layout.fragment_welcome_back,
  FragmentWelcomeBackBinding::inflate
) {
  override val viewModel by activityViewModels<WelcomeBackViewModel>()
  override val viewEvents: Flow<WelcomeBackViewEvent>
    get() = binding.signInWithGoogleButton.clicks()
      .map { WelcomeBackViewEvent.SignInWithGoogle }

  private lateinit var googleSignInComponent: GoogleSignInComponent

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    createGoogleSignInComponent()
    lifecycle.addObserver(googleSignInComponent)
  }

  private fun createGoogleSignInComponent() {
    googleSignInComponent = GoogleSignInComponent(
      requireActivity(),
      requireActivity().activityResultRegistry
    ).apply {
      signInSuccessFlow
        .map { SignInSuccessful(User.fromGoogleCredentials(it)) }
        .onEach(extraViewEventsFlow::emit)
        .launchInWhenResumed(lifecycleScope)
    }
  }

  override fun render(viewState: WelcomeBackViewState) {

  }

  override fun react(viewEffect: WelcomeBackViewEffect) = when (viewEffect) {
    is ShowSignInWithGoogleOAuthFlow -> showGoogleSignIn()
    is NavigateToMainScreen -> navigateToMainScreen()
  }

  private fun navigateToMainScreen() = findNavController().navigate(R.id.to_main)

  private fun showGoogleSignIn() {
    googleSignInComponent.showSignInDialog()
  }
}
