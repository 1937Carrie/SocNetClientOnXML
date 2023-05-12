package sdumchykov.androidApp.presentation.editProfile

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
import sdumchykov.androidApp.domain.model.requestModels.EditProfileUser
import sdumchykov.androidApp.domain.repository.NetworkUsersRepository
import sdumchykov.androidApp.domain.storage.Storage
import sdumchykov.androidApp.domain.utils.Constants
import sdumchykov.androidApp.domain.utils.Response
import sdumchykov.androidApp.domain.utils.Status
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val db: AppDatabase,
    private val serverApi: NetworkUsersRepository,
    private val sharedPreferencesStorage: Storage
) : ViewModel() {

    private val _statusUserContacts = MutableLiveData<Response<Status>>()
    val statusUserContacts: LiveData<Response<Status>> = _statusUserContacts

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _user.postValue(db.userDao().getUser())
        }
    }

    fun apiEditProfile(
        name: String, phone: String, address: String, career: String, DOB: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val userDao = db.userDao()
            val userId = userDao.getUser().id

            setLoadingStatus()

            val response = try {
                serverApi.editUser(
                    userId, Constants.BEARER_TOKEN + getAccessToken(), EditProfileUser(
                        name = name,
                        phone = phone,
                        address = address,
                        career = career,
                        birthday = DOB,
                    )
                )
            } catch (e: IOException) {
                setErrorStatus(R.string.messageIOException)
                return@launch
            } catch (e: HttpException) {
                setErrorStatus(R.string.messageHTTPException)
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

                setSuccessStatus()
            } else {
                setErrorStatus(R.string.messageUnexpectedState)
            }
        }
    }

    private fun setSuccessStatus() {
        _statusUserContacts.postValue(Response.success(Status.SUCCESS))
    }

    private fun setLoadingStatus() {
        _statusUserContacts.postValue(Response.loading(null))
    }

    private fun setErrorStatus(messageResourceId: Int) {
        _statusUserContacts.postValue(Response.error(messageResourceId, null))
    }

    private fun getAccessToken(): String {
        return sharedPreferencesStorage.getString(Constants.ACCESS_TOKEN) ?: ""
    }

}