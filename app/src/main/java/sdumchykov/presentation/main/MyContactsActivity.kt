package sdumchykov.presentation.main

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.R
import sdumchykov.databinding.ActivityMyContactsBinding
import sdumchykov.domain.model.UserModel
import sdumchykov.presentation.main.adapter.UsersAdapter
import sdumchykov.presentation.main.adapter.listener.UsersListener

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
//region Fetch contacts from phone book
//    private fun getContactsListWithDexter() {
//        Dexter.withActivity(this).withPermission(Manifest.permission.READ_CONTACTS)
//            .withListener(object : PermissionListener {
//                override fun onPermissionGranted(response: PermissionGrantedResponse) {
//                    if (response.permissionName == Manifest.permission.READ_CONTACTS) {
//                        contacts
//                    }
//                }
//
//                override fun onPermissionDenied(response: PermissionDeniedResponse) {
//                    Toast.makeText(
//                        this@MyContactsActivity, "Permission should be granted!", Toast.LENGTH_SHORT
//                    ).show()
//                }
//
//                override fun onPermissionRationaleShouldBeShown(
//                    permission: PermissionRequest, token: PermissionToken
//                ) {
//                    token.continuePermissionRequest()
//                }
//            }).check()
//    }
//
//    private val contacts: Unit
//        @SuppressLint("NotifyDataSetChanged", "Recycle") get() {
//            var phones: Cursor? = null
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                phones = contentResolver.query(
//                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null
//                )
//            }
//
//            while (phones!!.moveToNext()) {
//                @SuppressLint("Range")
//                val name =
//                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
//
//                @SuppressLint("Range")
//                val phoneNumber =
//                    phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
//                val contact = Contact(name, phoneNumber, IMAGE_URL)
//
//                contactList.add(contact)
//            }
//
//        }
//endregion
}