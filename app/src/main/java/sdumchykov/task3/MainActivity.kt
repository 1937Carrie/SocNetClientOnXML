package sdumchykov.task3

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import sdumchykov.task3.databinding.ActivityMainBinding
import sdumchykov.task3.extensions.setImage


class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setMainPicture()
        setTextToTextName()
        setURIToImageInstagram()
        buttonViewMyContactsSetOnClickListener()
    }

    private fun setMainPicture() {
        val drawableSource = R.drawable.image
        binding.imageViewPicture.setImage(this, drawableSource)
    }

    private fun buttonViewMyContactsSetOnClickListener() {
        binding.buttonViewMyContacts.setOnClickListener {
//            val intent = Intent(this, MyContactsActivity::class.java)
//            startActivity(intent)

            val fragmentMyContacts: Fragment = FragmentMyContacts()
            val fragment: Fragment? =
                supportFragmentManager.findFragmentByTag(FragmentMyContacts::class.java.simpleName)
            if (fragment !is FragmentMyContacts) {
                supportFragmentManager.beginTransaction().add(
                    R.id.main_activity_constraint_layout,
                    fragmentMyContacts,
                    FragmentMyContacts::class.java.simpleName
                ).commit()
            }
            binding.buttonEditProfile.visibility = View.GONE
            binding.buttonViewMyContacts.visibility = View.GONE

        }
    }

    private fun setURIToImageInstagram() {
        binding.imageInstagram.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/p/BDdr32ZrvgP/")
                )
            )
        }
    }

    private fun setTextToTextName() {
        val signupEmail = intent.getStringExtra("email")
        val splitted = signupEmail?.substring(0, signupEmail.indexOf('@'))?.split(Regex("\\W"))

        if (splitted?.size!! > 1) {
            val firstName = splitted[0].replaceFirstChar { it.uppercase() }
            val secondName = splitted[1].replaceFirstChar { it.uppercase() }
            val textContent = "$firstName $secondName"

            binding.textViewName.text = textContent
        } else {
            binding.textViewName.text = signupEmail.substring(0, signupEmail.indexOf('@'))
        }
    }

}