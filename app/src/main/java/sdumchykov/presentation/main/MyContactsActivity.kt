package sdumchykov.presentation.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.R
import sdumchykov.databinding.ActivityMyContactsBinding
import sdumchykov.domain.constants.ConstantsAndVariables
import sdumchykov.domain.model.UserModel
import sdumchykov.presentation.main.adapter.UsersAdapter
import sdumchykov.presentation.main.adapter.listener.UsersListener

private const val HARDCODED_IMAGE_URL = "https://picsum.photos/200"

@AndroidEntryPoint
class MyContactsActivity :
    BaseActivity<ActivityMyContactsBinding>(ActivityMyContactsBinding::inflate) {

    private val mainViewModel: MainViewModel by viewModels()
    private val usersAdapter: UsersAdapter by lazy {
        UsersAdapter(usersListener = object : UsersListener {
            override fun onUserClickAction(userModel: UserModel) {
                Log.d("MainActivity", "onUserClickAction: ${userModel.id}")
            }

            override fun onTrashIconClickAction(userModel: UserModel) {
                deleteContact(userModel)
            }
        })
    }
    private lateinit var dialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageButtonArrowBackSetOnClickListener()
        initRecyclers()
        setObservers()
        setSwipeToDelete()
        createAddContactDialog()
        if (ConstantsAndVariables.FETCH_CONTACT_LIST)
            if (mainViewModel.userLiveData.value?.size == 0)
                getContactsListWithDexter()

    }


    private fun imageButtonArrowBackSetOnClickListener() {
        binding.imageButtonArrowBack.setOnClickListener {
            finish()
        }
    }

    private fun initRecyclers() {
        binding.recyclerViewContacts.run {
            adapter = usersAdapter
            layoutManager =
                LinearLayoutManager(this@MyContactsActivity, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun setObservers() {
        mainViewModel.userLiveData.observe(this) { users ->
            usersAdapter.submitList(users.toMutableList())
        }
    }

    private fun setSwipeToDelete() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val contact = mainViewModel.userLiveData.value?.get(position)

                deleteContact(contact)

            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewContacts)
    }

    fun deleteContact(contact: UserModel?) {
        mainViewModel.removeItem(contact)

        val snackbar =
            Snackbar.make(binding.recyclerViewContacts, "${contact?.name} has been deleted", 5000)

        snackbar.setAction("Undo") {
            mainViewModel.addItem(contact)
            if (contact != null) {
                Toast.makeText(
                    applicationContext, "${contact.name} has been restored", Toast.LENGTH_LONG
                ).show()
            }
        }
        snackbar.show()
    }

    private fun createAddContactDialog() {
        val view = layoutInflater.inflate(R.layout.add_contacts_dialog, null)
        dialog = AlertDialog.Builder(this).setView(view).create()

        val button: AppCompatButton = view.findViewById(R.id.button_addcontactsdialog_add)
        button.setOnClickListener {
            val name =
                view.findViewById<AppCompatEditText>(R.id.textinputedittext_addcontactsdialog_name)
            val surname =
                view.findViewById<AppCompatEditText>(R.id.textinputedittext_addcontactsdialog_surname)
            val profession =
                view.findViewById<AppCompatEditText>(R.id.textinputedittext_addcontactsdialog_profession)

            mainViewModel.addItem(
                mainViewModel.userLiveData.value?.size?.let { it1 ->
                    UserModel(
                        id = it1,
                        name = "${name.text.toString()} ${surname.text.toString()}",
                        profession = profession.text.toString(),
                        photo = "https://picsum.photos/200"
                    )
                }
            )

            name.text?.clear()
            surname.text?.clear()
            profession.text?.clear()

            dialog.dismiss()
        }
        binding.textViewAddContacts.setOnClickListener { dialog.show() }
    }

    private fun getContactsListWithDexter() {
        Dexter.withActivity(this).withPermission(Manifest.permission.READ_CONTACTS)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    if (response.permissionName == Manifest.permission.READ_CONTACTS) {
                        contacts
                    }
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Toast.makeText(
                        this@MyContactsActivity, "Permission should be granted!", Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest, token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private val contacts: Unit
        @SuppressLint("Range")
        get() {
            val phones =  contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
            )

            if (phones != null) {
                while (phones.moveToNext()) {
                    val name =
                        phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

                    val phoneNumber =
                        phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val contact = mainViewModel.userLiveData.value?.size?.let {
                        UserModel(
                            it,
                            name,
                            phoneNumber,
                            HARDCODED_IMAGE_URL
                        )
                    }

                    mainViewModel.addItem(contact)
                }
                phones.close()
            }

        }
}