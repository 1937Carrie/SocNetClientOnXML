package sdumchykov.presentation.main.adapter.listener

import sdumchykov.domain.model.UserModel

interface UsersListener {
    fun onUserClickAction(userModel: UserModel)
    fun onTrashIconClickAction(userModel: UserModel)
}