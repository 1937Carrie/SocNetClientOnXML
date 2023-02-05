package sdumchykov.androidApp.domain.repository

import sdumchykov.androidApp.domain.model.UserModel

interface UsersRepository {
    suspend fun getUsers(fetchContacts: Boolean): List<UserModel>
}