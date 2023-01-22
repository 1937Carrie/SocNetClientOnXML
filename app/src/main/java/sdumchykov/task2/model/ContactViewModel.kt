package sdumchykov.task2.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class ContactViewModel @inject constructor(private val repository: List<Contact>) : ViewModel() {
    private val _contactList = MutableLiveData<List<Contact>>()
    val contactList: LiveData<List<Contact>> = _contactList

    init {
        getAllContacts()
    }

    private fun getAllContacts() {
        _contactList.value = repository
    }

    fun addItem(contact: Contact) {
        _contactList.value = contactList.value?.toMutableList()?.apply { add(contact) }
    }

    fun updateItem(index: Int, newContact: Contact) {
        _contactList.value = contactList.value?.toMutableList()?.apply { set(index, newContact) }
    }

    fun removeItem(contact: Contact) {
        _contactList.value = contactList.value?.toMutableList()?.apply { remove(contact) }
    }
}