package sdumchykov.task2.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sdumchykov.presentation.utils.ext.setImageCacheless
import sdumchykov.task2.databinding.ContactItemBinding
import sdumchykov.task2.model.Contact

class ItemAdapter(private val contactsListener: ContactsListener) :
    ListAdapter<Contact, ItemAdapter.ContactsViewHolder>(ContactsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContactItemBinding.inflate(inflater, parent, false)

        return ContactsViewHolder(binding)
    }

    inner class ContactsViewHolder(private val binding: ContactItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindTo(contact: Contact) {
            with(binding) {

                textViewName.text = contact.name
                textViewProfession.text = contact.profession
                imageViewPhoto.setImageCacheless(contact.photo)

                setListeners(contact)
            }

        }

        private fun ContactItemBinding.setListeners(contact: Contact) {
            imageButtonDelete.setOnClickListener { contactsListener.deleteContact(contact) }
        }
    }

    override fun onBindViewHolder(viewHolder: ContactsViewHolder, position: Int) {
        viewHolder.bindTo(getItem(position))
    }

}