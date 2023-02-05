package sdumchykov.androidApp.presentation.utils.ext

import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey

fun AppCompatImageView.setImageCacheless(photo: String?) {
    Glide.with(this).load(photo).signature(ObjectKey(System.currentTimeMillis().toString()))
        .circleCrop().into(this)
}

fun AppCompatImageView.setImage(resourceId: Int?) {
    Glide.with(this).load(resourceId).circleCrop().into(this)
}