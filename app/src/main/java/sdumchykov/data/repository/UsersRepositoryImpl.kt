package sdumchykov.data.repository

import sdumchykov.data.db.InMemoryDb
import sdumchykov.domain.constants.ConstantsAndVariables
import sdumchykov.domain.repository.UsersRepository
import javax.inject.Inject

class UsersRepositoryImpl @Inject constructor(
    private val inMemoryDb: InMemoryDb
) : UsersRepository {
    override suspend fun getUsers() = if (ConstantsAndVariables.FETCH_CONTACT_LIST) listOf() else inMemoryDb.getHardcodedUsers()
}