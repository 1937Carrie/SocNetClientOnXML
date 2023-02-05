package sdumchykov.androidApp.data.repository

import sdumchykov.androidApp.data.db.InMemoryDb
import sdumchykov.androidApp.domain.repository.UsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val inMemoryDb: InMemoryDb
) : UsersRepository {
    override suspend fun getUsers(fetchContacts: Boolean) =
        if (fetchContacts) listOf() else inMemoryDb.getHardcodedUsers()
}