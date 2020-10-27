package com.brian.views.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.MyFriendsFragmentBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.users.UsersViewModel
import com.brian.viewModels.users.UsersViewModelFactory
import com.brian.views.adapters.MyFriendsAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class MyFriendsFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: UsersViewModelFactory by instance()
    lateinit var mBinding: MyFriendsFragmentBinding
    lateinit var mViewModel: UsersViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = MyFriendsFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        setupObserver()
        setupRecyclers()
        setupScrollListener()
        mViewModel.getMyFriends()
        mViewModel.myFriendsAdapter.listener = this.ClickHandler()
        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsersViewModel::class.java)
    }

    inner class ClickHandler : MyFriendsAdapter.onViewClick {
        override fun viewUserProfile(position: Int) {
            findNavController().navigate(R.id.userProfileFragment, bundleOf(getString(R.string.challenge_type) to getString(R.string.no)))

        }

        override fun acceptRequest(position: Int) {
            mViewModel.acceptRejectRequestFriends(position, getString(R.string.accept))
        }

        override fun rejectRequest(position: Int) {
            mViewModel.acceptRejectRequestFriends(position, getString(R.string.reject))

        }


    }


    private fun setupObserver() {
        mViewModel.apply {
            showMessage.observe(viewLifecycleOwner, Observer {
                if (!TextUtils.isEmpty(it)) {
                    requireContext().showToast(it)
                    showMessage.postValue("")
                }
            })

            myFriends.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    mViewModel.myFriendsAdapter.setNewItems(it)
                } else {
                    if (currentPage == 1) {
                        mViewModel.myFriendsAdapter.setNewItems(it)
                    }
                }

            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }

    private fun setupScrollListener() {
        mBinding.apply {
            recycler.setOnScrollChangeListener { _, _, _, _, _ ->

                if (mViewModel.usersList.value?.isNotEmpty()!!) {
                    val view = recycler.getChildAt(recycler.childCount - 1)
                    val diff = view.bottom - (recycler.height + recycler.scrollY)
                    val offset = mViewModel.myFriends.value?.size
                    if (diff == 0 && offset!! % 10 == 0 && !mViewModel.allUsersLoaded) {
                        mViewModel.getUsers()
                    }
                }

            }

        }
    }


    private fun setupRecyclers() {
        mBinding.apply {
            recycler.apply {
                adapter = mViewModel.myFriendsAdapter
            }

        }
    }

}