package sdumchykov.task1

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import sdumchykov.task1.databinding.ActivityMainBinding


private const val HARDCODED_IMAGE_PATH = "https://www.instagram.com/p/BDdr32ZrvgP/"
private const val EMAIL = "email"
private const val SYMBOL_AT = '@'
private const val PATTERN_NON_CHARACTER = "\\W"

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    //TODO dependency injection прочитати
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTextToTextName()
        setURIToImageInstagram()
    }

    private fun setURIToImageInstagram() {
        binding.imageInstagram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(HARDCODED_IMAGE_PATH)))
        }
    }

    private fun setTextToTextName() {
//        TODO
//        someVariable?.let {
//
//}
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