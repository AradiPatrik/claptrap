package com.aradipatrik.claptrap.login.ui

import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aradipatrik.claptrap.R
import com.aradipatrik.claptrap.common.util.ViewDelegates
import com.aradipatrik.claptrap.common.util.ViewDelegates.settingEditTextContent
import com.aradipatrik.claptrap.common.util.ViewDelegates.settingTextInputLayoutContent
import com.aradipatrik.claptrap.databinding.FragmentWelcomeBackBinding
import com.aradipatrik.claptrap.domain.User
import com.aradipatrik.claptrap.login.model.*
import com.aradipatrik.claptrap.login.model.Mappers.fromGoogleCredentials
import com.aradipatrik.claptrap.login.model.WelcomeBackViewEffect.NavigateToMainScreen
import com.aradipatrik.claptrap.login.model.WelcomeBackViewEffect.ShowSignInWithGoogleOAuthFlow
import com.aradipatrik.claptrap.login.model.WelcomeBackViewEvent.*
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.aradipatrik.claptrap.mvi.Flows.launchInWhenResumed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import ru.ldralighieri.corbind.view.clicks
import ru.ldralighieri.corbind.widget.textChangeEvents

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
    get() = merge(
      binding.signInWithGoogleButton.clicks().map { SignInWithGoogle },
      binding.signInFab.clicks().map { SignInWithEmailAndPassword },
      binding.emailTextInputLayout.editText!!.textChangeEvents()
        .drop(1)
        .map { EmailTextChange(it.text.toString()) },
      binding.passwordTextInputLayout.editText!!.textChangeEvents()
        .drop(1)
        .map { PasswordTextChange(it.text.toString()) }
    )

  var emailText by settingTextInputLayoutContent { binding.emailTextInputLayout }
  var passwordText by settingTextInputLayoutContent { binding.passwordTextInputLayout }

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
    emailText = viewState.email
    passwordText = viewState.password
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
