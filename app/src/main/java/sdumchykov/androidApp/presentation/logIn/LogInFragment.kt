package sdumchykov.androidApp.presentation.logIn

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.FragmentLogInBinding
import sdumchykov.androidApp.domain.utils.Status
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.signUp.CredentialsViewModel
import sdumchykov.androidApp.presentation.utils.ext.gone
import sdumchykov.androidApp.presentation.utils.ext.visible

@AndroidEntryPoint
class LogInFragment : BaseFragment<FragmentLogInBinding>(FragmentLogInBinding::inflate) {

    private val credentialsViewModel: CredentialsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        handleRememberMer()
    }

    override fun setObservers() {
        setStatusObserver()
        setUserObserver()
    }

    private fun setStatusObserver() {
        credentialsViewModel.status.observe(viewLifecycleOwner) { response ->
            with(binding) {
                when (response.status) {
                    Status.SUCCESS -> {
                        textViewLogInAuthorizeResponseText.gone()
                        progressBarLogIn.gone()

                        val action = LogInFragmentDirections.actionLogInFragmentToMainActivity()
                        Navigation.findNavController(binding.root).navigate(action)
                        activity?.finish()
                    }
                    Status.ERROR -> {
                        progressBarLogIn.gone()
                        textViewLogInAuthorizeResponseText.visible()
                        textViewLogInAuthorizeResponseText.text =
                            getString(credentialsViewModel.status.value?.message ?: R.string.oops)
                    }
                    Status.LOADING -> {
                        textViewLogInAuthorizeResponseText.gone()
                        progressBarLogIn.visible()
                    }
                }
            }
        }
    }

    private fun setUserObserver() {
        credentialsViewModel.user.observe(viewLifecycleOwner) {
            with(binding) {
                val user = it

                if (checkBoxLogInRememberMe.isChecked && user != null) {
                    textInputLayoutLogInEmail.editText?.setText(user.email)
                    textInputLayoutLogInPassword.editText?.setText(credentialsViewModel.getPassword())
                    authorizeAccount()
                }
            }
        }
    }

    private fun handleRememberMer() {
        credentialsViewModel.getUser()
    }

    private fun saveRememberMe() {
        with(binding) {
            if (checkBoxLogInRememberMe.isChecked) {
                credentialsViewModel.savePassword(textInputLayoutLogInPassword.editText?.text.toString())
            }
        }
    }

    override fun setListeners() {
        buttonLoginSetListener()
        textViewLogInSignInSetListener()
    }

    private fun buttonLoginSetListener() {
        with(binding) {
            buttonLogInLogin.setOnClickListener {
                saveRememberMe()
                authorizeAccount()
            }
        }
    }

    private fun authorizeAccount() {
        with(binding) {
            val email = textInputLayoutLogInEmail.editText?.text.toString()
            val password = textInputLayoutLogInPassword.editText?.text.toString()

            credentialsViewModel.authorizeUser(email, password)
        }
    }

    private fun textViewLogInSignInSetListener() {
        binding.textViewLogInSignIn.setOnClickListener {

            val action = LogInFragmentDirections.actionLogInFragmentToSignUpFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }
    }

}