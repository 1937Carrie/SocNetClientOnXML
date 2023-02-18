package sdumchykov.androidApp.presentation.viewPager.myProfile

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.R
import sdumchykov.androidApp.databinding.FragmentMyProfileBinding
import sdumchykov.androidApp.domain.utils.Constants.EMAIL_KEY
import sdumchykov.androidApp.presentation.MainActivityArgs
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.signUp.SignUpViewModel
import sdumchykov.androidApp.presentation.utils.ext.setImage
import sdumchykov.androidApp.presentation.viewPager.ViewPagerFragment
import sdumchykov.androidApp.presentation.viewPager.contacts.fetchContacts.FetchContacts

private const val HARDCODED_IMAGE_PATH = "https://www.instagram.com/p/BDdr32ZrvgP/"
private const val SIGN_AT = '@'
private const val PATTERN_NON_CHARACTER = "\\W"
private const val SHOW_CONTACT_LIST = "Fetch contact list"
private const val SHOW_HARDCODED_LIST = "Show hardcoded contact list data"

@AndroidEntryPoint
class MyProfileFragment :
    BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {
    private val viewModel: MyProfileViewModel by viewModels()
    private val pagerFragment by lazy { parentFragment as ViewPagerFragment }
    private val args: MainActivityArgs by navArgs()
    val parentViewModel by lazy { pagerFragment.myContactsViewModel }
    val signUpViewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMainPicture()
        setTextToTextName()
        setURIToImageInstagram()
        handleRecyclerViewContent() // TODO if remove it here or on :121, then it will not work
    }

    override fun setListeners() {
        imageViewMainProfilePictureSetOnClickListener()
        buttonViewMyContactsSetOnClickListener()
    }

    private fun setMainPicture() {
        val drawableSource = R.drawable.ic_profile_image
        binding.imageViewMainProfilePicture.setImage(drawableSource)
    }

    private fun setTextToTextName() {
        var receivedEmail = signUpViewModel.getEmail()

        if (receivedEmail == "") {
            /*TODO The problem has occurred, I can't get args.email.
            Maybe I'm passing an argument to MainActivity but I go directly to ViewPager and
            don't have access to MainActivity. I did a stub on :89 line for like temporary solution*/
            receivedEmail = args.email

            cacheEmailToSharedPreferences()
        }

        val splittedEmail = receivedEmail.substring(0, receivedEmail.indexOf(SIGN_AT))
            .split(Regex(PATTERN_NON_CHARACTER))
        binding.textViewMainName.text = if (splittedEmail.size > 1) {
            val firstName = splittedEmail[0].replaceFirstChar { it.uppercase() }
            val secondName = splittedEmail[1].replaceFirstChar { it.uppercase() }
            val textContent = "$firstName $secondName"
            textContent
        } else {
            receivedEmail.substring(0, receivedEmail.indexOf(SIGN_AT))
        }

        if (signUpViewModel.getPassword() == "") {
            signUpViewModel.saveEmail("")
        }
    }

    private fun cacheEmailToSharedPreferences() {
        val cachedData =
            this.requireActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE)
        val editor = cachedData.edit()

        editor.putString(EMAIL_KEY, args.email)

        editor.apply()
    }

    private fun setURIToImageInstagram() {
        binding.imageViewMainInstagram.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(HARDCODED_IMAGE_PATH)))
        }
    }

    private fun imageViewMainProfilePictureSetOnClickListener() {
        binding.imageViewMainProfilePicture.setOnClickListener {
//            activity?.viewModelStore.clear()

            val fetchContactList = !viewModel.getFetchContactList()
            viewModel.setFetchContactList(fetchContactList)

            val toastText = if (fetchContactList) SHOW_CONTACT_LIST
            else SHOW_HARDCODED_LIST
            Toast.makeText(activity, toastText, Toast.LENGTH_SHORT).show()

            handleRecyclerViewContent()
            if (!parentViewModel.getFetchContactList()) parentViewModel.initContactList()
        }
    }

    private fun handleRecyclerViewContent() {
        if (parentViewModel.getFetchContactList()) getContactsListWithDexter()
    }

    private fun getContactsListWithDexter() {
        val fetchContacts = FetchContacts(parentViewModel)
        fetchContacts.fetchContacts(activity as AppCompatActivity)
    }

    private fun buttonViewMyContactsSetOnClickListener() {
        binding.buttonMainViewMyContacts.setOnClickListener {
            (parentFragment as ViewPagerFragment).viewPager.currentItem =
                ViewPagerFragment.Tabs.CONTACTS.ordinal
        }
    }
}
