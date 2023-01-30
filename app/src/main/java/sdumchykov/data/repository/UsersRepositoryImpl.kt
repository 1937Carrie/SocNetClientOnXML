package sdumchykov.data.repository

import sdumchykov.data.db.InMemoryDb
import sdumchykov.domain.repository.UsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val inMemoryDb: InMemoryDb
) : UsersRepository {
    override suspend fun getUsers() = inMemoryDb.getHardcodedUsers()
}