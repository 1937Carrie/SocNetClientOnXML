package sdumchykov.androidApp.presentation.myProfile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.ActivityMyProfileBinding
import sdumchykov.androidApp.domain.utils.Constants.EMAIL_KEY
import sdumchykov.androidApp.presentation.base.BaseActivity
import sdumchykov.androidApp.presentation.contacts.MyContactsActivity
import sdumchykov.androidApp.presentation.utils.ext.setImage

private const val HARDCODED_IMAGE_PATH = "https://www.instagram.com/p/BDdr32ZrvgP/"
private const val SIGN_AT = '@'
private const val PATTERN_NON_CHARACTER = "\\W"
private const val SHOW_CONTACT_LIST = "Fetch contact list"
private const val SHOW_HARDCODED_LIST = "Show hardcoded contact list data"

@AndroidEntryPoint
class MyProfileActivity : BaseActivity<ActivityMyProfileBinding>(ActivityMyProfileBinding::inflate) {
    private val viewModel: MyProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setMainPicture()
        setTextToTextName()
        setURIToImageInstagram()
    }

    private fun setMainPicture() {
        val drawableSource = R.drawable.ic_profile_image
        binding.imageViewMainProfilePicture.setImage(drawableSource)
    }

    private fun setTextToTextName() {
        val signupEmail = intent.getStringExtra(EMAIL_KEY) ?: ""
        val splitted = signupEmail.substring(0, signupEmail.indexOf(SIGN_AT))
            .split(Regex(PATTERN_NON_CHARACTER))

        binding.textViewMainName.text = if (splitted.size > 1) {
            val firstName = splitted[0].replaceFirstChar { it.uppercase() }
            val secondName = splitted[1].replaceFirstChar { it.uppercase() }

            "$firstName $secondName"
        } else {
            signupEmail.substring(0, signupEmail.indexOf(SIGN_AT))
        }
    }

    private fun setURIToImageInstagram() {
        binding.imageViewMainInstagram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(HARDCODED_IMAGE_PATH)))
        }
    }

    override fun setListeners() {
        imageViewMainProfilePictureSetOnClickListener()
        buttonViewMyContactsSetOnClickListener()
    }

    private fun imageViewMainProfilePictureSetOnClickListener() {
        binding.imageViewMainProfilePicture.setOnClickListener {
            val fetchContactList = !viewModel.getFetchContactList()
            viewModel.setFetchContactList(fetchContactList)

            val toastText = if (fetchContactList) SHOW_CONTACT_LIST
            else SHOW_HARDCODED_LIST
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
        }
    }

    private fun buttonViewMyContactsSetOnClickListener() {
        binding.buttonMainViewMyContacts.setOnClickListener {
            val intent = Intent(this, MyContactsActivity::class.java)
            startActivity(intent)
        }
    }

}