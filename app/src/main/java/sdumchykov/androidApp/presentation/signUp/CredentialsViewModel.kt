package sdumchykov.androidApp.presentation.signUp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.HttpException
import sdumchykov.androidApp.R
import sdumchykov.androidApp.domain.local.AppDatabase
import sdumchykov.androidApp.domain.local.User
import sdumchykov.androidApp.domain.model.requestModels.AuthorizeModel
import sdumchykov.androidApp.domain.model.requestModels.EditProfileUser
import sdumchykov.androidApp.domain.repository.NetworkUsersRepository
import sdumchykov.androidApp.domain.storage.Storage
import sdumchykov.androidApp.domain.utils.Constants
import sdumchykov.androidApp.domain.utils.Response
import sdumchykov.androidApp.domain.utils.Status
import java.io.IOException
import javax.inject.Inject

private const val PASSWORD = "PASSWORD"

@HiltViewModel
class CredentialsViewModel @Inject constructor(
    private val db: AppDatabase,
    private val sharedPreferencesStorage: Storage,
    private val serverRepository: NetworkUsersRepository
) : ViewModel() {

    private val _status = MutableLiveData<Response<Status>>()
    val status: LiveData<Response<Status>> = _status

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(db.userDao().getUser())
        }
    }

    fun authorizeUser(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoadingStatus(_status)
            val response = try {
                serverRepository.authorizeUser(AuthorizeModel(email, password))
            } catch (e: IOException) {
                setErrorStatus(_status, R.string.messageIOException)
                return@launch
            } catch (e: HttpException) {
                setErrorStatus(_status, R.string.messageHTTPException)
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                val userDao = db.userDao()

                val user = response.body()?.data?.user
                val userData = User(
                    address = user?.address,
                    birthday = user?.birthday,
                    career = user?.career,
                    email = user?.email,
                    facebook = user?.facebook,
                    id = user?.id ?: 0,
                    image = user?.image,
                    instagram = user?.instagram,
                    linkedin = user?.linkedin,
                    name = user?.name,
                    phone = user?.phone,
                    twitter = user?.twitter
                )
                userDao.insert(userData)

                sharedPreferencesStorage.save(
                    Constants.ACCESS_TOKEN, response.body()?.data?.accessToken ?: ""
                )
                setSuccessStatus(_status)
            } else {
                setErrorStatus(_status, R.string.messageUnexpectedState)
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            setLoadingStatus(_status)
            val response = try {
                serverRepository.registerUser(email, password)
            } catch (e: IOException) {
                setErrorStatus(_status, R.string.messageIOException)
                return@launch
            } catch (e: HttpException) {
                setErrorStatus(_status, R.string.messageHTTPException)
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                val userDao = db.userDao()

                val user = response.body()?.data?.user

                userDao.insert(
                    User(
                        address = user?.address,
                        birthday = user?.birthday,
                        career = user?.career,
                        email = user?.email,
                        facebook = user?.facebook,
                        id = user?.id ?: 0,
                        image = user?.image,
                        instagram = user?.instagram,
                        linkedin = user?.linkedin,
                        name = user?.name,
                        phone = user?.phone,
                        twitter = user?.twitter
                    )
                )

                sharedPreferencesStorage.save(
                    Constants.ACCESS_TOKEN, response.body()?.data?.accessToken ?: ""
                )
                setSuccessStatus(_status)
            } else {
                setErrorStatus(_status, R.string.messageUnexpectedState)
            }
        }
    }

    fun apiEditProfile(
        name: String, phone: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val userDao = db.userDao()
            val userId = userDao.getUser().id

            setLoadingStatus(_status)

            val response = try {
                serverRepository.editUser(
                    userId, Constants.BEARER_TOKEN + getAccessToken(), EditProfileUser(
                        name = name,
                        phone = phone
                    )
                )
            } catch (e: IOException) {
                setErrorStatus(_status, R.string.messageIOException)
                return@launch
            } catch (e: HttpException) {
                setErrorStatus(_status, R.string.messageHTTPException)
                return@launch
            }
            if (response.isSuccessful) {
                val user = response.body()?.data?.user
                val userData = User(
                    address = user?.address,
                    birthday = user?.birthday,
                    career = user?.career,
                    email = user?.email,
                    facebook = user?.facebook,
                    id = user?.id ?: 0,
                    image = user?.image,
                    instagram = user?.instagram,
                    linkedin = user?.linkedin,
                    name = user?.name,
                    phone = user?.phone,
                    twitter = user?.twitter
                )
                userDao.insert(userData)

                setSuccessStatus(_status)
            } else {
                setErrorStatus(_status, R.string.messageUnexpectedState)
            }
        }
    }

    private fun setSuccessStatus(destination: MutableLiveData<Response<Status>>) {
        destination.postValue(Response.success(Status.SUCCESS))
    }

    private fun setLoadingStatus(destination: MutableLiveData<Response<Status>>) {
        destination.postValue(Response.loading(null))
    }

    private fun setErrorStatus(
        destination: MutableLiveData<Response<Status>>, messageResourceId: Int
    ) {
        destination.postValue(Response.error(messageResourceId, null))
    }

    fun savePassword(password: String) = sharedPreferencesStorage.save(PASSWORD, password)

    fun getPassword(): String = sharedPreferencesStorage.getString(PASSWORD) ?: ""

    private fun getAccessToken(): String {
        return sharedPreferencesStorage.getString(Constants.ACCESS_TOKEN) ?: ""
    }

}
