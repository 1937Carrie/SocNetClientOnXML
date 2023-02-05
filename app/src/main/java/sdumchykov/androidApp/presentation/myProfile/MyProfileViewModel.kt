package sdumchykov.androidApp.presentation.myProfile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import sdumchykov.androidApp.domain.storage.Storage
import sdumchykov.androidApp.domain.utils.Constants.FETCH_CONTACT_LIST_KEY
import javax.inject.Inject

@HiltViewModel
class MyProfileViewModel @Inject constructor(
    private val storage: Storage
) : ViewModel() {
    fun getFetchContactList(): Boolean {
        return storage.getBoolean(FETCH_CONTACT_LIST_KEY)
    }

    fun setFetchContactList(state: Boolean) {
        storage.save(FETCH_CONTACT_LIST_KEY, state)
    }
}