package sdumchykov.androidApp.presentation.contacts.adapter.listener

import sdumchykov.androidApp.domain.model.UserModel

interface UsersListener {
    fun onUserClickAction(userModel: UserModel)
    fun onTrashIconClickAction(userModel: UserModel, userIndex: Int)
}