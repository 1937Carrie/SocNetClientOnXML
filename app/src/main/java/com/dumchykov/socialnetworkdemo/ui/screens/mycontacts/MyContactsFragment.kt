package com.dumchykov.socialnetworkdemo.ui.screens.mycontacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.data.contactsprovider.IndicatorContact
import com.dumchykov.socialnetworkdemo.data.webapi.ResponseState
import com.dumchykov.socialnetworkdemo.databinding.FragmentMyContactsBinding
import com.dumchykov.socialnetworkdemo.domain.webapi.models.ContactId
import com.dumchykov.socialnetworkdemo.domain.webapi.models.MultipleContactResponse
import com.dumchykov.socialnetworkdemo.ui.SharedViewModel
import com.dumchykov.socialnetworkdemo.ui.notification.NOTIFICATION_ID
import com.dumchykov.socialnetworkdemo.ui.notification.createNotificationChannel
import com.dumchykov.socialnetworkdemo.ui.notification.createOnRemoveNotification
import com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.adapter.ContactsAdapter
import com.dumchykov.socialnetworkdemo.ui.screens.mycontacts.adapter.ContactsItemDecoration
import com.dumchykov.socialnetworkdemo.ui.screens.pager.Page
import com.dumchykov.socialnetworkdemo.ui.screens.pager.PagerFragment
import com.dumchykov.socialnetworkdemo.ui.util.handleStandardResponse
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyContactsFragment : Fragment() {
    private var _binding: FragmentMyContactsBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactsAdapter: ContactsAdapter
    private val viewModel: MyContactsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                val contact = viewModel.processingContact.value
                showNotification(contact.id, contact.name)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Post notification permission was not granted",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getAllUsers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyContactsBinding.inflate(inflater, container, false)
        val animation =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementEnterTransition = animation
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authorizedUser.collect { user ->
                if (user.id == -1) return@collect

                updateUserContacts()
                setBackPressDispatcher()
                setArrowBackClickListener()
                setSearchRelatedListeners()
                setAddContactClickListener()
                setFabClickListener()
                initAdapter()
                observeApiResponse()
                createNotificationChannel(requireContext())
                postponeEnterTransition()
                binding.recyclerContacts.doOnPreDraw { startPostponedEnterTransition() }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUserContacts() {
        val userId = viewModel.authorizedUser.value.id
        val bearerToken = sharedViewModel.shareState.value.accessToken
        viewModel.getUserContacts(userId, bearerToken)
    }

    private fun getAllUsers() {
        val bearerToken = sharedViewModel.shareState.value.accessToken
        viewModel.getAllUsers(bearerToken)
    }

    private fun observeApiResponse() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.myContactsState.collect { state ->
                handleStandardResponse(
                    state = state,
                    context = requireContext(),
                    scope = this,
                    progressLayout = binding.layoutProgress.root
                ) {
                    binding.layoutProgress.root.visibility = View.GONE
                    when ((state as ResponseState.Success<*>).data) {
                        is MultipleContactResponse -> {
                            state.data as MultipleContactResponse
                            sharedViewModel.updateState { copy(userContactIdList = state.data.apiContacts.map { it.id }) }
                            viewModel.updateContactListState {
                                copy(indicatorContacts = state.data.apiContacts.map {
                                    IndicatorContact(
                                        id = it.id,
                                        name = it.name.toString(),
                                        career = it.career.toString(),
                                        address = it.address.toString()
                                    )
                                })
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initAdapter() {
        contactsAdapter = ContactsAdapter(
            context = requireContext(),
            onClick = { view, indicatorContact ->
                val contactBundle = bundleOf(
                    INDICATOR_CONTACT_ID to indicatorContact.id,
                    INDICATOR_CONTACT_NAME to indicatorContact.name
                )
                val extras =
                    FragmentNavigatorExtras(view to "${indicatorContact.id}_${indicatorContact.name}")
                findNavController().navigate(
                    R.id.action_pagerFragment_to_detailsFragment,
                    contactBundle,
                    null,
                    extras
                )
            },
            onDelete = { indicatorContact ->
                val userId = viewModel.authorizedUser.value.id
                val bearerToken = sharedViewModel.shareState.value.accessToken
                viewModel.removeContact(userId, indicatorContact.id, bearerToken)
                Snackbar
                    .make(
                        binding.recyclerContacts,
                        getString(R.string.contact_has_been_deleted, indicatorContact.name),
                        Snackbar.LENGTH_LONG
                    )
                    .setAction(getString(R.string.undo)) {
                        viewModel.addContact(bearerToken, userId, ContactId(indicatorContact.id))
                    }
                    .show()
                viewModel.setProcessingContact(indicatorContact)
                val isRequiredTiramisu = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                if (isRequiredTiramisu && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    showNotification(indicatorContact.id, indicatorContact.name)
                }
            },
            onChangeSelect = { indicatorContact ->
                viewModel.updateContactCheckState(indicatorContact)
            }
        )

        binding.recyclerContacts.adapter = contactsAdapter
        binding.recyclerContacts.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.recyclerContacts.addItemDecoration(ContactsItemDecoration(requireContext()))

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.contactListState.collect { myContactsState ->
                contactsAdapter.updateMultiSelectState { myContactsState.isMultiselect }
                contactsAdapter.submitList(myContactsState.indicatorContacts)

                binding.fab.visibility =
                    if (myContactsState.isMultiselect) View.VISIBLE else View.GONE
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun showNotification(
        indicatorContactId: Int,
        indicatorContactName: String,
    ) {
        val notification =
            createOnRemoveNotification(requireContext(), indicatorContactId, indicatorContactName)
        NotificationManagerCompat.from(requireContext())
            .notify(NOTIFICATION_ID, notification)
    }

    private fun setFabClickListener() {
        binding.fab.setOnClickListener {
            val userId = viewModel.authorizedUser.value.id
            val bearerToken = sharedViewModel.shareState.value.accessToken
            viewModel.multipleRemovingContact(userId, bearerToken)
        }
    }

    private fun setAddContactClickListener() {
        binding.textAddContacts.setOnClickListener {
            findNavController().navigate(R.id.action_pagerFragment_to_addContactsFragment)
        }
    }

    private fun setSearchRelatedListeners() {
        binding.buttonSearch.setOnClickListener {
            binding.layoutTop.visibility = View.GONE
            binding.layoutSearch.visibility = View.VISIBLE
        }

        binding.imageCloseSearch.setOnClickListener {
            binding.textInputSearchEditText.setText("")
            binding.layoutTop.visibility = View.VISIBLE
            binding.layoutSearch.visibility = View.GONE
        }

        binding.textInputSearchEditText.doOnTextChanged { text, _, _, _ ->
            val filteredList = viewModel.contactListState.value.indicatorContacts
                .filter { it.name.lowercase().contains(text.toString().lowercase()) }
            contactsAdapter.submitList(filteredList)
        }
    }

    private fun setArrowBackClickListener() {
        binding.buttonArrowBack.setOnClickListener {
            (parentFragment as PagerFragment).changeCurrentItem(Page.MyProfile.ordinal)
        }
    }

    private fun setBackPressDispatcher() {
        requireActivity().onBackPressedDispatcher.addCallback(
            owner = viewLifecycleOwner,
            onBackPressed = {
                (parentFragment as PagerFragment).changeCurrentItem(Page.MyProfile.ordinal)
            }
        )
    }

    companion object {
        const val INDICATOR_CONTACT_ID = "indicatorContact.id"
        const val INDICATOR_CONTACT_NAME = "indicatorContact.name"
    }
}