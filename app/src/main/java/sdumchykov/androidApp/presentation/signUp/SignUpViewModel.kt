package sdumchykov.androidApp.presentation.signUp

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import sdumchykov.androidApp.domain.storage.Storage
import sdumchykov.androidApp.domain.utils.Constants.EMAIL_KEY
import sdumchykov.androidApp.domain.utils.Constants.PASSWORD_KEY
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val storage: Storage
) : ViewModel() {

    fun saveEmail(email: String) {
        storage.save(EMAIL_KEY, email)
    }

    fun getEmail(): String {
        return storage.getString(EMAIL_KEY) ?: ""
    }

    fun savePassword(password: String) {
        storage.save(PASSWORD_KEY, password)
    }

    fun getPassword(): String {
        return storage.getString(PASSWORD_KEY) ?: ""
    }

}
