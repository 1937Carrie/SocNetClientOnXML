package sdumchykov.domain.repository

import sdumchykov.domain.model.UserModel

interface UsersRepository {
    suspend fun getUsers(): List<UserModel>
}