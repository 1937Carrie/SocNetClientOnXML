package sdumchykov.task2.data

import sdumchykov.task2.model.Contact

class Datasource {
    private var data = listOf(
        Contact(
            name = "James Mary",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ),
        Contact(
            name = "Robert Patricia",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ), Contact(
            name = "John Jennifer",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ), Contact(
            name = "Michael Linda",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ), Contact(
            name = "David Elizabeth",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ), Contact(
            name = "William Barbara",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ), Contact(
            name = "Richard Susan",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ), Contact(
            name = "Joseph Jessica",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        ), Contact(
            name = "Thomas Sarah",
            profession = "Photograph",
            photo = "https://picsum.photos/200"
        )
    )

    fun get() = data

    fun set(value: ArrayList<Contact>) {
        data = value
    }

    private companion object {

    }
}
