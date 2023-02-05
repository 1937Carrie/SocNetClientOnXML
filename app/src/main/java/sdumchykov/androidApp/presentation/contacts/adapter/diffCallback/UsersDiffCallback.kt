package sdumchykov.androidApp.presentation.contacts.adapter.diffCallback

import androidx.recyclerview.widget.DiffUtil
import sdumchykov.androidApp.domain.model.UserModel

class UsersDiffCallback : DiffUtil.ItemCallback<UserModel>() {
    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean =
        oldItem.id == newItem.id


    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean =
        oldItem == newItem
}