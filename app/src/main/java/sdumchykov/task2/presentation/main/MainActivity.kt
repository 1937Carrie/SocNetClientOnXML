package sdumchykov.task2.presentation.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.presentation.utils.ext.setImage
import sdumchykov.task2.*
import sdumchykov.task2.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setMainPicture()
        setTextToTextName()
        setURIToImageInstagram()
        buttonViewMyContactsSetOnClickListener()
    }

    private fun setMainPicture() {
        val drawableSource = R.drawable.ic_profile_image
        binding.imageViewPicture.setImage(this, drawableSource)
    }

    private fun buttonViewMyContactsSetOnClickListener() {
        binding.buttonViewMyContacts.setOnClickListener {
            val intent = Intent(this, MyContactsActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setURIToImageInstagram() {
        binding.imageInstagram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(HARDCODED_IMAGE_PATH)))
        }
    }

    private fun setTextToTextName() {
        val signupEmail = intent.getStringExtra(EMAIL) ?: ""
        val splitted = signupEmail.substring(0, signupEmail.indexOf(SYMBOL_AT))
            .split(Regex(PATTERN_NON_CHARACTER))

        binding.textViewName.text = if (splitted.size > 1) {
            val firstName = splitted[0].replaceFirstChar { it.uppercase() }
            val secondName = splitted[1].replaceFirstChar { it.uppercase() }

            "$firstName $secondName"
        } else {
            signupEmail.substring(0, signupEmail.indexOf(SYMBOL_AT))
        }
    }

}