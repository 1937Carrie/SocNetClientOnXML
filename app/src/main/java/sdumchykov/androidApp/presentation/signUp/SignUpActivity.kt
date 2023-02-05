package sdumchykov.androidApp.presentation.signUp

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.doOnTextChanged
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.R
import sdumchykov.androidApp.domain.utils.Constants.EMAIL_KEY
import sdumchykov.androidApp.presentation.base.BaseActivity
import sdumchykov.androidApp.presentation.myProfile.MyProfileActivity
import sdumchykov.databinding.ActivitySignUpBinding

private const val MINIMUM_PASSWORD_LENGTH = 8
private const val PATTERN_DIGIT = "\\d"
private const val PATTERN_CHARACTERS = "[a-zA-Z]+"

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate) {

    private val viewModel: SignUpViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        buttonRegisterDisable()
        textInputDoOnTextChanged()
        buttonRegisterSetOnClickListener()
        startMainActivity()
    }

    private fun buttonRegisterDisable() {
        binding.buttonSignUpRegister.isEnabled = false
    }

    private fun textInputDoOnTextChanged() {
        editTextSignUpEmailDoOnTextChanged()
        editTextSignUpPasswordDoOnTextChanged()
    }

    private fun editTextSignUpEmailDoOnTextChanged() {
        with(binding) {
            editTextSignUpEmail.doOnTextChanged { _, _, _, _ ->
                textInputLayoutSignUpEmail.error =
                    if (!Patterns.EMAIL_ADDRESS.matcher(editTextSignUpEmail.text.toString())
                            .matches()
                    ) resources.getString(R.string.error_message_email)
                    else null

                val emailError = textInputLayoutSignUpEmail.error.isNullOrEmpty()
                val passwordError = textInputLayoutSignUpPassword.error.isNullOrEmpty()

                buttonSignUpRegister.isEnabled = emailError && passwordError
            }
        }
    }

    private fun editTextSignUpPasswordDoOnTextChanged() {
        with(binding) {
            editTextSignUpPassword.doOnTextChanged { _, _, _, _ ->
                val lessThanEightSymbols =
                    editTextSignUpPassword.text.toString().length < MINIMUM_PASSWORD_LENGTH
                val notContainsDigits =
                    !editTextSignUpPassword.text.toString().contains(Regex(PATTERN_DIGIT))
                val notContainsCharacters =
                    !editTextSignUpPassword.text.toString().contains(Regex(PATTERN_CHARACTERS))

                textInputLayoutSignUpPassword.error =
                    if (lessThanEightSymbols || notContainsDigits || notContainsCharacters)
                        resources.getString(R.string.error_message_password)
                    else null

                val emailError = textInputLayoutSignUpEmail.error.isNullOrEmpty()
                val passwordError = textInputLayoutSignUpPassword.error.isNullOrEmpty()

                buttonSignUpRegister.isEnabled = emailError && passwordError
            }
        }
    }

    private fun buttonRegisterSetOnClickListener() {
        with(binding) {
            buttonSignUpRegister.setOnClickListener {
                if (binding.checkBoxSignUpRememberMe.isChecked) {
                    viewModel.saveEmail(editTextSignUpEmail.text.toString())
                    viewModel.savePassword(editTextSignUpPassword.text.toString())

                    Toast.makeText(
                        this@SignUpActivity,
                        "${viewModel.getEmail()}\n" + viewModel.getPassword(),
                        Toast.LENGTH_LONG
                    ).show()
                }

                startActivityWithIntent(editTextSignUpEmail.text.toString())
            }
        }
    }

    private fun startMainActivity() {
        val email = viewModel.getEmail()

        if (email.isNotEmpty()) {
            startActivityWithIntent(email)
        }
    }

    private fun startActivityWithIntent(email: String) {
        val intent = Intent(this, MyProfileActivity::class.java)

        intent.putExtra(EMAIL_KEY, email)

        startActivity(intent)
        finish()
    }

}