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
        if (ConstantsAndVariables.FETCH_CONTACT_LIST) if (mainViewModel.userLiveData.value?.size == 0) getContactsListWithDexter()
    }


    private fun imageButtonArrowBackSetOnClickListener() {
        binding.imageViewMyContactsArrowBack.setOnClickListener {
            finish()
        }
    }

    private fun initRecyclers() {
        binding.recyclerViewMyContactsContactList.run {
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
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewMyContactsContactList)
    }

    fun deleteContact(contact: UserModel?) {
        mainViewModel.removeItem(contact)

        val snackbar = Snackbar.make(
            binding.recyclerViewMyContactsContactList, "${contact?.name} has been deleted", 5000
        )

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

        val button: AppCompatButton = view.findViewById(R.id.buttonAddContactsDialogAdd)
        button.setOnClickListener {
            val name = view.findViewById<AppCompatEditText>(R.id.editTextAddContactsDialogName)
            val surname =
                view.findViewById<AppCompatEditText>(R.id.editTextAddContactsDialogSurname)
            val profession =
                view.findViewById<AppCompatEditText>(R.id.editTextAddContactsDialogProfession)

            mainViewModel.addItem(mainViewModel.userLiveData.value?.size?.let {
                UserModel(
                    id = it,
                    name = "${name.text.toString()} ${surname.text.toString()}",
                    profession = profession.text.toString(),
                    photo = HARDCODED_IMAGE_URL
                )
            })

            name.text?.clear()
            surname.text?.clear()
            profession.text?.clear()

            dialog.dismiss()
        }
        binding.textViewMyContactsAddContacts.setOnClickListener { dialog.show() }
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
        @SuppressLint("Range") get() {
            val phones = contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
            )

            if (phones != null) {
                val userModels = ArrayList<UserModel>()
                while (phones.moveToNext()) {
                    val name =
                        phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val phoneNumber =
                        phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    val contact = UserModel(userModels.size, name, phoneNumber, HARDCODED_IMAGE_URL)

                    userModels.add(contact)
                }
                mainViewModel.addData(userModels)
                phones.close()
            }
        }

}