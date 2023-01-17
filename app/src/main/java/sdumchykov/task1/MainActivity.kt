package sdumchykov.task1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import sdumchykov.task1.databinding.ActivityMainBinding


private const val HARDCODED_IMAGE_PATH = "https://www.instagram.com/p/BDdr32ZrvgP/"

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    //TODO dependency injection прочитати
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTextToTextName()
        setURIToImageInstagram()
    }

    private fun setURIToImageInstagram() {
        binding.imageInstagram.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse(HARDCODED_IMAGE_PATH)
                )
            )
        }
    }

    private fun setTextToTextName() {
//        TODO
//        someVariable?.let {
//
//}
        val signupEmail = intent.getStringExtra("email") ?: ""
        val splitted = signupEmail.substring(0, signupEmail.indexOf('@')).split(Regex("\\W"))

        binding.textViewName.text = if (splitted.size > 1) {
            val firstName = splitted[0].replaceFirstChar { it.uppercase() }
            val secondName = splitted[1].replaceFirstChar { it.uppercase() }

            "$firstName $secondName"
        } else {
            signupEmail.substring(0, signupEmail.indexOf('@'))
        }
    }

}