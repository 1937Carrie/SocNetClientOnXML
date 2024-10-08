package com.dumchykov.socialnetworkdemo.ui.screens.myprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dumchykov.socialnetworkdemo.R
import com.dumchykov.socialnetworkdemo.databinding.FragmentMyProfileBinding
import com.dumchykov.socialnetworkdemo.ui.SharedViewModel
import com.dumchykov.socialnetworkdemo.ui.screens.pager.Page
import com.dumchykov.socialnetworkdemo.ui.screens.pager.PagerFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyProfileFragment : Fragment() {
    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MyProfileViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUi()
        setLogOutClickListener()
        setEditProfileClickListener()
        setViewMyContactsClickListener()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setViewMyContactsClickListener() {
        binding.buttonViewMyContacts.setOnClickListener {
            (parentFragment as PagerFragment).changeCurrentItem(Page.MyContacts.ordinal)
        }
    }

    private fun setEditProfileClickListener() {
        binding.buttonEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_pagerFragment_to_editProfileFragment)
        }
    }

    private fun setLogOutClickListener() {
        binding.textLogOut.setOnClickListener {
            viewModel.clearCredentials()
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.exitFlag.collect {
                    if (it) {
                        findNavController().navigate(R.id.action_pagerFragment_to_loginFragment)
                    }
                }
            }
        }
    }

    private fun updateUi() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.authorizedUser.collect { contact ->
                if (contact.id == -1) return@collect

                binding.textName.text = contact.name
                val currentUser = viewModel.authorizedUser.value
                if (currentUser.facebook.isEmpty() || currentUser.linkedin.isEmpty() || currentUser.instagram.isEmpty()) {
                    binding.flowIcons.visibility = View.GONE
                    binding.textGoToSettingsAndFillOut.visibility = View.VISIBLE
                } else {
                    binding.flowIcons.visibility = View.VISIBLE
                    binding.textGoToSettingsAndFillOut.visibility = View.GONE
                }
            }
        }
    }
}