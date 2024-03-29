package sdumchykov.androidApp.presentation.viewPager.addContacts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import sdumchykov.androidApp.databinding.FragmentAddContactBinding
import sdumchykov.androidApp.domain.model.User
import sdumchykov.androidApp.domain.utils.Status
import sdumchykov.androidApp.presentation.base.BaseFragment
import sdumchykov.androidApp.presentation.utils.ext.showToast
import sdumchykov.androidApp.presentation.viewPager.addContacts.adapter.UsersAdapter
import sdumchykov.androidApp.presentation.viewPager.addContacts.adapter.listener.UsersListener
import sdumchykov.androidApp.presentation.viewPager.contacts.ContactsViewModel

@AndroidEntryPoint
class AddContactFragment :
    BaseFragment<FragmentAddContactBinding>(FragmentAddContactBinding::inflate) {

    private val contactsViewModel: ContactsViewModel by viewModels()
    private val allUsersViewModel: ServerContactsViewModel by viewModels()
    private val usersAdapter: UsersAdapter by lazy {
        UsersAdapter(
            usersListener = object : UsersListener {
                override fun onUserClickAction(user: User, adapterPosition: Int) {
                    contactsViewModel.apiAddContact(user.id)
                    contactsViewModel.apiGetUserContacts()
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    override fun setListeners() {
        buttonArrowSetListener()
        refreshLayoutSetListener()
    }

    override fun setObservers() {
        setUserLiveDataObserver()
    }

    private fun initRecyclerView() {
        with(binding) {
            recyclerViewAddContactContactList.run {
                adapter = usersAdapter
                layoutManager = LinearLayoutManager(context)
            }
        }
    }

    private fun setUserLiveDataObserver() {
        allUsersViewModel.statusAllUsers.observe(viewLifecycleOwner) { response ->
            with(binding) {
                when (response.status) {
                    Status.SUCCESS -> {

                    }
                    Status.ERROR -> {
                        context?.showToast("Failed to pull users")
                    }
                    Status.LOADING -> {

                    }
                }
            }
        }


        allUsersViewModel.allUsers.observe(this) { users ->
            usersAdapter.submitList(users.toMutableList())
        }
    }

    private fun buttonArrowSetListener() {
        with(binding) {
            imageViewAddContactArrowBack.setOnClickListener {
//                val action =
//                    AddContactFragmentDirections.actionAddContactFragmentToViewPagerFragment()
//                Navigation.findNavController(binding.root).navigate(action)
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun refreshLayoutSetListener() {
        with(binding) {
            swiperefresh.setOnRefreshListener {
                swiperefresh.isRefreshing = true
                allUsersViewModel.getAllUsers()
                swiperefresh.isRefreshing = false
            }
        }
    }
}