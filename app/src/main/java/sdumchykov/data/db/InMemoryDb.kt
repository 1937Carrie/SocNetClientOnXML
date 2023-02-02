package sdumchykov.data.db

import sdumchykov.domain.model.UserModel
import javax.inject.Inject

class InMemoryDb @Inject constructor() {
    fun getHardcodedUsers(): List<UserModel> = listOf(
        UserModel(
            id = 0,
            name = "Function Let",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 1,
            name = "Function With",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 2,
            name = "Function Run",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 3,
            name = "Function Apply",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 4,
            name = "Function Also",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 5,
            name = "Readonly List",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 6,
            name = "Readonly Map",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 7,
            name = "Lazy Property",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 8,
            name = "If Expression",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        UserModel(
            id = 9,
            name = "Nullable Boolean",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        )
    )
}