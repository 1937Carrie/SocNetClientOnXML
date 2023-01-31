package sdumchykov.presentation.main

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Patterns
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.R
import sdumchykov.databinding.ActivitySignUpBinding

private const val MINIMUM_PASSWORD_LENGTH = 8
private const val PATTERN_DIGIT = "\\d"
private const val PATTERN_CHARACTERS = "[a-zA-Z]+"
private const val EMAIL = "email"
private const val PASSWORD = "password"

@AndroidEntryPoint
class SignUpActivity : BaseActivity<ActivitySignUpBinding>(ActivitySignUpBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(binding) {
            buttonRegisterDisable(buttonRegister)

            textInputDoOnTextChanged()

            buttonRegisterSetOnClickListener(buttonRegister, textInputEmail, textInputPassword)

            startMainActivity()

            if (savedInstanceState != null) {
                textInputEmail.setText(savedInstanceState.getString(EMAIL))
                textInputPassword.setText(savedInstanceState.getString(PASSWORD))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        outState.putString(EMAIL, binding.textInputEmail.text.toString())
        outState.putString(PASSWORD, binding.textInputPassword.text.toString())
    }

    private fun startMainActivity() {
        val cachedData = getPreferences(MODE_PRIVATE).getString(EMAIL, "") ?: ""

        if (cachedData.isNotEmpty()) {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

            intent.putExtra(EMAIL, cachedData)

            startActivity(intent)
            finish()
        }
    }

    private fun buttonRegisterSetOnClickListener(
        buttonRegister: Button,
        textInputEmail: AppCompatEditText,
        textInputPassword: AppCompatEditText
    ) {
        buttonRegister.setOnClickListener {
            if (binding.signUpCheckBoxRememberMe.isChecked) {
                val cachedData = getPreferences(MODE_PRIVATE)
                val editor = cachedData.edit()

                editor.putString(EMAIL, textInputEmail.text.toString())
                editor.putString(PASSWORD, textInputPassword.text.toString())

                editor.apply()

                val toast = Toast.makeText(
                    applicationContext, "${cachedData.getString(EMAIL, "Not found")}\n" + "${
                        cachedData.getString(PASSWORD, "Not found")
                    }", Toast.LENGTH_LONG
                )
                toast.show()
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EMAIL, textInputEmail.text.toString())
            startActivity(intent)

            finish()
        }
    }

    private fun buttonRegisterDisable(buttonRegister: Button) {
        buttonRegister.isEnabled = false
    }

    private fun textInputDoOnTextChanged() {
        with(binding) {
            textInputEmail.doOnTextChanged { _, _, _, _ ->
                textInputLayoutEmail.error =
                    if (!Patterns.EMAIL_ADDRESS.matcher(textInputEmail.text.toString()).matches()) {
                        resources.getString(R.string.error_message_email)
                    } else {
                        null
                    }

                val emailError = textInputLayoutEmail.error.isNullOrEmpty()
                val passwordError = textInputLayoutPassword.error.isNullOrEmpty()

                buttonRegister.isEnabled = emailError && passwordError
            }
            textInputPassword.doOnTextChanged { _, _, _, _ ->
                val lessThanEightSymbols =
                    textInputPassword.text.toString().length < MINIMUM_PASSWORD_LENGTH
                val notContainsDigits =
                    !textInputPassword.text.toString().contains(Regex(PATTERN_DIGIT))
                val notContainsCharacters =
                    !textInputPassword.text.toString().contains(Regex(PATTERN_CHARACTERS))

                textInputLayoutPassword.error =
                    if (lessThanEightSymbols || notContainsDigits || notContainsCharacters) {
                        resources.getString(R.string.error_message_password)
                    } else {
                        null
                    }

                val emailError = textInputLayoutEmail.error.isNullOrEmpty()
                val passwordError = textInputLayoutPassword.error.isNullOrEmpty()

                buttonRegister.isEnabled = emailError && passwordError
            }
        }
    }
}