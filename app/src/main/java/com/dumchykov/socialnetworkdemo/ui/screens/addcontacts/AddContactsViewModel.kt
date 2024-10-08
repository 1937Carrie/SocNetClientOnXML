package com.dumchykov.socialnetworkdemo.ui.screens.addcontacts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumchykov.socialnetworkdemo.data.contactsprovider.IndicatorContact
import com.dumchykov.socialnetworkdemo.data.room.ContactsDatabase
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.domain.logic.toContact
import com.dumchykov.socialnetworkdemo.domain.logic.toIndicatorContact
import com.dumchykov.socialnetworkdemo.domain.webapi.ContactRepository
import com.dumchykov.socialnetworkdemo.domain.webapi.models.ContactId
import com.dumchykov.socialnetworkdemo.domain.webapi.models.MultipleUserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.dumchykov.socialnetworkdemo.ui.screens.myprofile.Contact as PlainContact

@HiltViewModel
class AddContactsViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val database: ContactsDatabase,
) : ViewModel() {
    private val _addContactsState = MutableStateFlow<ResponseState>(ResponseState.Initial)
    val addContactsState get() = _addContactsState.asStateFlow()

    private val _processingIndicatorContact = MutableStateFlow(IndicatorContact())
    val processingContact get() = _processingIndicatorContact.asStateFlow()

    private val _authorizedUser = MutableStateFlow(PlainContact(id = -1))
    val authorizedUser get() = _authorizedUser.asStateFlow()

    private val _allUsers = MutableStateFlow<List<IndicatorContact>>(emptyList())
    val allUsers get() = _allUsers.asStateFlow()

    private val _userContactIdList = MutableStateFlow<List<Int>>(emptyList())
    val userContactIdList get() = _userContactIdList.asStateFlow()

    init {
        getAuthorizedUser()
    }

    private fun getAuthorizedUser() {
        viewModelScope.launch {
            val authUser = withContext(Dispatchers.IO) {
                database.contactsDao.getCurrentUser()
            }
            _authorizedUser.update { authUser.toContact() }
        }
    }

    fun updateState(reducer: ResponseState.() -> ResponseState) {
        _addContactsState.update(reducer)
    }

    fun getAllUsers(bearerToken: String) {
        viewModelScope.launch {
            updateState { ResponseState.Loading }
            val getUsersResponse = contactRepository.getUsers(bearerToken)
            if (getUsersResponse is ResponseState.Success<*>) {
                getUsersResponse.data as MultipleUserResponse
                _allUsers.update { getUsersResponse.data.users.map { it.toIndicatorContact() } }
            }
            updateState { getUsersResponse }
        }
    }

    fun addContact(bearerToken: String, userId: Int, contactId: ContactId) {
        viewModelScope.launch {
            updateState { ResponseState.Loading }
            val addContactResponse = contactRepository.addContact(bearerToken, userId, contactId)
            updateState { addContactResponse }
        }
    }

    fun isUserAddedToContact(userId: Int): Boolean {
        return userContactIdList.value.contains(userId)
    }

    fun setProcessingContact(indicatorContact: IndicatorContact) {
        _processingIndicatorContact.update { indicatorContact }
    }

    fun setUserContactIdList(userContactIdList: List<Int>) {
        _userContactIdList.update { userContactIdList }
    }
}