package com.brian.views.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.UsersFragmentBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.users.UsersViewModel
import com.brian.viewModels.users.UsersViewModelFactory
import com.brian.views.adapters.MyFriendsAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class UsersFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: UsersViewModelFactory by instance()
    lateinit var mBinding: UsersFragmentBinding
    lateinit var mViewModel: UsersViewModel
    private val mClickHandler = ClickHandler()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = UsersFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        if (arguments?.getString("no").equals("no")) {
            mBinding.toolbar.tvTitle.text = getString(R.string.my_friends)

        } else if (arguments?.getString("team").equals("team")) {
            mBinding.toolbar.tvTitle.text = getString(R.string.team_player)

        } else {
            mBinding.toolbar.tvTitle.text = getString(R.string.users)

        }

        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }


        mBinding.etSearchBar.addTextChangedListener(mViewModel.textWatcher)
        mViewModel.usersAdapter.listener = this.mClickHandler
        setupRecyclers()
        setupScrollListener()
        setupObserver()



        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsersViewModel::class.java)
    }


    inner class ClickHandler : MyFriendsAdapter.onViewClick {

        override fun viewUserProfile(position: Int) {
            findNavController().navigate(R.id.userProfileFragment)
        }

        override fun sendRequest(position: Int) {
            mViewModel.sendRequest(position)
        }

        override fun cancelrequest(position: Int) {
            mViewModel.cancelRequest(position)
        }

        override fun acceptRequest(position: Int) {
            mViewModel.acceptRejectRequest(position,getString(R.string.accept))
        }

        override fun rejectRequest(position: Int) {
        }

        override fun removeFriend(position: Int) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

            usersList.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    mViewModel.usersAdapter.setNewItems(it)
                } else {
                    if (currentPage == 1) {
                        mViewModel.usersAdapter.setNewItems(it)
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
                    val offset = mViewModel.usersList.value?.size
                    if (diff == 0 && offset!! % 10 == 0 && !mViewModel.allVideosLoaded) {
                        mViewModel.getUsers()
                    }
                }

            }

        }
    }


    private fun setupRecyclers() {
        mBinding.apply {
            recycler.apply {
                adapter = mViewModel.usersAdapter
            }

        }
    }

}