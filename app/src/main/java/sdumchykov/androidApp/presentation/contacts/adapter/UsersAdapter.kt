package sdumchykov.androidApp.presentation.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import sdumchykov.androidApp.databinding.ContactItemBinding
import sdumchykov.androidApp.domain.model.UserModel
import sdumchykov.androidApp.presentation.contacts.adapter.diffCallback.UsersDiffCallback
import sdumchykov.androidApp.presentation.contacts.adapter.listener.UsersListener
import sdumchykov.androidApp.presentation.contacts.adapter.viewHolder.UsersViewHolder

class UsersAdapter(
    private val usersListener: UsersListener
) : ListAdapter<UserModel, UsersViewHolder>(UsersDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        return UsersViewHolder(
            ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            usersListener
        )
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

}