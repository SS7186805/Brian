package com.brian.views.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brian.R
import com.brian.base.EndlessRecyclerViewScrollListener
import com.brian.base.ScopedFragment
import com.brian.databinding.MyFriendsFragmentBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.users.UsersViewModel
import com.brian.viewModels.users.UsersViewModelFactory
import com.brian.views.activities.ChatActivity
import com.brian.views.adapters.MyFriendsAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class MyFriendsFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: UsersViewModelFactory by instance()
    lateinit var mBinding: MyFriendsFragmentBinding
    lateinit var mViewModel: UsersViewModel
    var mEndlessFriendsecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null


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
        mViewModel.currentPageMyFriends = 1
        mViewModel.myFriends.value?.clear()
        mViewModel.myFriendsAdapter.clearData()
        mViewModel.getMyFriends()
        mViewModel.myFriendsAdapter.listener = this.ClickHandler()
        return mBinding.root
    }


    private fun setupScrollListener() {

        mEndlessFriendsecyclerViewScrollListener =
            object :
                EndlessRecyclerViewScrollListener(mBinding.recycler.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    Log.e("ONLOADMOREMyUsers", "Onloadmore" + mViewModel.myFriends.value?.size)

                    if (mViewModel.myFriends.value!!.size % 10 == 0 && mViewModel.myFriends.value!!.size != 0) {
                        mViewModel.getMyFriends()
                    }

                }
            }



        mBinding.recycler.addOnScrollListener(mEndlessFriendsecyclerViewScrollListener!!)


    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsersViewModel::class.java)
    }

    inner class ClickHandler : MyFriendsAdapter.onViewClick {
        override fun viewUserProfile(position: Int) {
            findNavController().navigate(
                R.id.userProfileFragment,
                bundleOf(getString(R.string.user_id) to mViewModel.myFriends.value!![position].otherUserDetail?.id.toString())
            )

        }

        override fun startChat(position: Int) {
            startActivity(
                Intent(
                    requireContext(),
                    ChatActivity::class.java
                ).putExtra(
                    getString(R.string.other_user_id),
                    mViewModel.myFriends.value!![position]?.otherUserDetail?.id
                ).putExtra(
                    getString(R.string.user),
                    mViewModel.myFriends.value!![position]?.otherUserDetail?.name
                )
            )
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
                if (it != null && it.isNotEmpty()) {
                    mBinding.tvNoDatFound.visibility = View.GONE
                    mViewModel.myFriendsAdapter.setNewItems(it)
                } else {
                    if (mViewModel.myFriends.value!!.size == 0) {
                        mBinding.tvNoDatFound.visibility = View.VISIBLE
                    }
                }

            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
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