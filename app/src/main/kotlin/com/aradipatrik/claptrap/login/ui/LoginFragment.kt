package com.aradipatrik.claptrap.login.ui

import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.StartIntentSenderForResult
import androidx.fragment.app.viewModels
import com.aradipatrik.claptrap.R
import com.aradipatrik.claptrap.databinding.FragmentLoginBinding
import com.aradipatrik.claptrap.login.model.LoginViewEffect
import com.aradipatrik.claptrap.login.model.LoginViewEvent
import com.aradipatrik.claptrap.login.model.LoginViewModel
import com.aradipatrik.claptrap.login.model.LoginViewState
import com.aradipatrik.claptrap.mvi.ClapTrapFragment
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.ldralighieri.corbind.view.clicks
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : ClapTrapFragment<
  LoginViewState,
  LoginViewEvent,
  LoginViewEffect,
  FragmentLoginBinding>(R.layout.fragment_login, FragmentLoginBinding::inflate) {
  override val viewModel by viewModels<LoginViewModel>()
  override val viewEvents: Flow<LoginViewEvent>
    get() = binding.signInWithGoogleButton.clicks()
      .map { LoginViewEvent.SignInWithGoogle }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun render(viewState: LoginViewState) {

  }

  private val loginResultHandler = registerForActivityResult(
    StartIntentSenderForResult()
  ) { result: ActivityResult? ->
    result ?: error("result is null")
    try {
      val credential = Identity.getSignInClient(requireActivity())
        .getSignInCredentialFromIntent(result.data)
      Timber.tag("APDEBUG").d("Credential: ${credential.displayName} ${credential.id}")
    } catch (exception: ApiException) {
      Timber.tag("APDEBUG").d(exception.message)
    }

  }

  override fun react(viewEffect: LoginViewEffect) = when (viewEffect) {
    is LoginViewEffect.SignInWithGoogle -> signIn()
  }

  private fun signIn() {
    val request = GetSignInIntentRequest.builder()
      .setServerClientId("93355315460-f7k9fm1v7q708j4gi234ktp8slttmhu5.apps.googleusercontent.com")
      .build()

    Identity.getSignInClient(requireActivity())
      .getSignInIntent(request)
      .addOnSuccessListener {
        loginResultHandler.launch(IntentSenderRequest.Builder(it).build())
      }
  }
}
