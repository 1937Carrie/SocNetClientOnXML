package sdumchykov.data.db

import sdumchykov.domain.model.UserModel
import javax.inject.Inject

class InMemoryDb @Inject constructor() {
    fun getHardcodedUsers(): List<UserModel> = listOf(
        UserModel(
            id = 1,
            name = "John Johnson",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 2,
            name = "John Johnson",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 3,
            name = "John Johnson",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 4,
            name = "John Johnson",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 5,
            name = "John Johnson",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 6,
            name = "John Johnson",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 7,
            name = "John Johnson",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 8,
            name = "John Johnson",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 9,
            name = "John Johnson",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 10,
            name = "John Johnson",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        )
    )
}