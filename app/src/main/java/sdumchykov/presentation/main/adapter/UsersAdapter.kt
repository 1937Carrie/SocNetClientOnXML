package sdumchykov.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import sdumchykov.domain.model.UserModel
import sdumchykov.presentation.main.adapter.diffCallback.UsersDiffCallback
import sdumchykov.presentation.main.adapter.listener.UsersListener
import sdumchykov.presentation.main.adapter.viewHolder.UsersViewHolder
import sdumchykov.task2.databinding.ContactItemBinding

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