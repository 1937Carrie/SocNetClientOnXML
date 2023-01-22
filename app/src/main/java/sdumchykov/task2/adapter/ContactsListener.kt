package sdumchykov.task2.adapter

import sdumchykov.task2.model.Contact

interface ContactsListener {
    fun deleteContact(contact: Contact)
}