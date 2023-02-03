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
            buttonRegisterDisable(buttonSignUpRegister)

            textInputDoOnTextChanged()

            buttonRegisterSetOnClickListener(
                buttonSignUpRegister,
                editTextSignUpEmail,
                editTextSignUpPassword
            )

            startMainActivity()

            if (savedInstanceState != null) {
                editTextSignUpEmail.setText(savedInstanceState.getString(EMAIL))
                editTextSignUpPassword.setText(savedInstanceState.getString(PASSWORD))
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        outState.putString(EMAIL, binding.editTextSignUpEmail.text.toString())
        outState.putString(PASSWORD, binding.editTextSignUpPassword.text.toString())
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
        buttonSignUpRegister: Button,
        editTextSignUpEmail: AppCompatEditText,
        editTextSignUpPassword: AppCompatEditText
    ) {
        buttonSignUpRegister.setOnClickListener {
            if (binding.checkBoxSignUpRememberMe.isChecked) {
                val cachedData = getPreferences(MODE_PRIVATE)
                val editor = cachedData.edit()

                editor.putString(EMAIL, editTextSignUpEmail.text.toString())
                editor.putString(PASSWORD, editTextSignUpPassword.text.toString())

                editor.apply()

                val toast = Toast.makeText(
                    applicationContext, "${cachedData.getString(EMAIL, "Not found")}\n" + "${
                        cachedData.getString(PASSWORD, "Not found")
                    }", Toast.LENGTH_LONG
                )
                toast.show()
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(EMAIL, editTextSignUpEmail.text.toString())
            startActivity(intent)

            finish()
        }
    }

    private fun buttonRegisterDisable(buttonSignUpRegister: Button) {
        buttonSignUpRegister.isEnabled = false
    }

    private fun textInputDoOnTextChanged() {
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

}