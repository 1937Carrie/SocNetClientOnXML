package sdumchykov.androidApp.domain.local

import androidx.room.*

@Dao
interface UserDao {

    @Query("SELECT * FROM User LIMIT 1")
    fun getUser(): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: User)

}