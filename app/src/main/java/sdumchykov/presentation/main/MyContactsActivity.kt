package sdumchykov.presentation.main

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
import sdumchykov.domain.model.UserModel
import sdumchykov.presentation.main.adapter.UsersAdapter
import sdumchykov.presentation.main.adapter.listener.UsersListener
import sdumchykov.task2.BaseActivity
import sdumchykov.task2.R
import sdumchykov.task2.SwipeToDeleteCallback
import sdumchykov.task2.databinding.ActivityMyContactsBinding
import sdumchykov.task2.model.Contact

@AndroidEntryPoint
class MyContactsActivity :
    BaseActivity<ActivityMyContactsBinding>(ActivityMyContactsBinding::inflate),
    UsersListener {

    private val mainViewModel: MainViewModel by viewModels()
    private val usersAdapter: UsersAdapter by lazy {
        UsersAdapter(usersListener = this)
    }
//    private lateinit var contactList: ArrayList<UserModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        imageButtonArrowBackSetOnClickListener()

        initRecyclers()
        setObservers()

        val swipeToDeleteCallback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val contact = mainViewModel.userLiveData.value?.get(position)

                deleteContact(contact)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewContacts)

        createAddContactDialog()

        binding.textViewAddContacts.setOnClickListener { dialog.show() }
    }

    override fun onUserClickAction(userModel: UserModel) {
        Log.d("MainActivity", "onUserClickAction: ${userModel.id}")
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

    private fun deleteContact(contact: UserModel?) {
        mainViewModel.removeItem(contact)

        val snackbar =
            Snackbar.make(binding.recyclerViewContacts, "${contact.name} has been deleted", 5000)

        snackbar.setAction("Undo") {
            viewModel.addItem(contact)
            Toast.makeText(
                applicationContext, "${contact.name} has been restored", Toast.LENGTH_LONG
            ).show()
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

            viewModel.addItem(
                Contact(
                    "${name.text.toString()} ${surname.text.toString()}",
                    profession.text.toString(),
                    "https://picsum.photos/200"
                            IMAGE_URL
                )
            )

            name.text?.clear()
            surname.text?.clear()
            profession.text?.clear()

            dialog.dismiss()
        }
    }
}