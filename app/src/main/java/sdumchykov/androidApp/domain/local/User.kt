package sdumchykov.androidApp.domain.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val uid: Int = 0,
    val address: String?,
    val birthday: String?,
    val career: String?,
    val email: String?,
    val facebook: String?,
    val id: Int,
    val image: String?,
    val instagram: String?,
    val linkedin: String?,
    val name: String?,
    val phone: String?,
    val twitter: String?,
)
