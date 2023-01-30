package sdumchykov.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import sdumchykov.domain.model.UserModel
import sdumchykov.domain.repository.UsersRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val usersRepository: UsersRepository

) : ViewModel() {
    private val _userLiveData = MutableLiveData<List<UserModel>>(listOf())
    val userLiveData: LiveData<List<UserModel>> = _userLiveData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _userLiveData.postValue(usersRepository.getUsers())
        }
    }


}