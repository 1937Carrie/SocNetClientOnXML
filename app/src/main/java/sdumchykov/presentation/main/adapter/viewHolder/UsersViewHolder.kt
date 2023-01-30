package sdumchykov.presentation.main.adapter.viewHolder

import androidx.recyclerview.widget.RecyclerView
import sdumchykov.domain.model.UserModel
import sdumchykov.presentation.main.adapter.listener.UsersListener
import sdumchykov.presentation.utils.ext.setImageCacheless
import sdumchykov.task2.databinding.ContactItemBinding

class UsersViewHolder(
    private val binding: ContactItemBinding,
    private val usersListener: UsersListener,

    ) : RecyclerView.ViewHolder(binding.root) {
    fun bindTo(user: UserModel) {
//        binding.imageViewPhoto.loadCircleImage(user.imageId)
        with(binding) {
            textViewName.text = user.name
            textViewProfession.text = user.profession
            imageViewPhoto.setImageCacheless("https://picsum.photos/200")

            setListeners(user)
        }
    }

    private fun setListeners(user: UserModel) {
        binding.root.setOnClickListener {
            usersListener.onUserClickAction(user)
        }
    }
}