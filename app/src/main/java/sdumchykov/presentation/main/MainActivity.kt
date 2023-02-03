package sdumchykov.presentation.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.R
import sdumchykov.databinding.ActivityMainBinding
import sdumchykov.domain.constants.ConstantsAndVariables
import sdumchykov.presentation.utils.ext.setImage

private const val HARDCODED_IMAGE_PATH = "https://www.instagram.com/p/BDdr32ZrvgP/"
private const val EMAIL = "email"
private const val SIGN_AT = '@'
private const val PATTERN_NON_CHARACTER = "\\W"
private const val SHOW_CONTACT_LIST = "Fetch contact list"
private const val SHOW_HARDCODED_LIST = "Show hardcoded contact list data"

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setMainPicture()
        setTextToTextName()
        setURIToImageInstagram()
        setListeners()
    }

    private fun setMainPicture() {
        val drawableSource = R.drawable.ic_profile_image
        binding.imageViewMainProfilePicture.setImage(drawableSource)
    }

    private fun setTextToTextName() {
        val signupEmail = intent.getStringExtra(EMAIL) ?: ""
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

    private fun setListeners() {
        imageViewMainProfilePictureSetOnClickListener()
        buttonViewMyContactsSetOnClickListener()
    }

    private fun imageViewMainProfilePictureSetOnClickListener() {
        binding.imageViewMainProfilePicture.setOnClickListener {
            ConstantsAndVariables.FETCH_CONTACT_LIST = !ConstantsAndVariables.FETCH_CONTACT_LIST
            val toastText = if (ConstantsAndVariables.FETCH_CONTACT_LIST) SHOW_CONTACT_LIST
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