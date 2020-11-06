package com.brian.views.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
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
import com.brian.databinding.UsersFragmentBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.models.MyFriendsDataItem
import com.brian.viewModels.users.UsersViewModel
import com.brian.viewModels.users.UsersViewModelFactory
import com.brian.views.activities.ChatActivity
import com.brian.views.adapters.AllUsersAdapter
import com.brian.views.adapters.SelectFriendsAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class UsersFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: UsersViewModelFactory by instance()
    lateinit var mBinding: UsersFragmentBinding
    lateinit var mViewModel: UsersViewModel
    private val mClickHandler = ClickHandler()
    var listener: onSelect? = null
    var isChallenegeType = false
    var mEndlessMyChallengesRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null


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

        if (arguments?.getString(getString(R.string.no)).equals(getString(R.string.no))) {
            mBinding.toolbar.tvTitle.text = getString(R.string.my_friends)

        } else if (arguments?.getString(getString(R.string.teams)).equals(getString(R.string.teams))) {
            mBinding.toolbar.tvTitle.text = getString(R.string.team_player)

        } else {
            mBinding.toolbar.tvTitle.text = getString(R.string.users)

        }

        mBinding.toolbar.ivBack.setOnClickListener {
            onBackPressed()
        }


        mBinding.etSearchBar.addTextChangedListener(mViewModel.textWatcher)
        mViewModel.usersAdapter.listener = this.mClickHandler

        loadData()
        setupRecyclers()
        setupScrollListener()
        setupObserver()
        onSwipe()

        return mBinding.root
    }

    fun loadData() {

        if (arguments?.getString(getString(R.string.challenge_type)).equals(getString(R.string.yes))) {
            var selectedUsers =
                arguments?.getParcelableArrayList<MyFriendsDataItem>(getString(R.string.select_user))
            isChallenegeType = true
            mViewModel.myFriends.value?.clear()
            mViewModel.selectFriendsAdapter.clearData()
            mViewModel.selectFriendsAdapter.selectedUsers.clear()
            mViewModel.selectFriendsAdapter.selectedUsers = selectedUsers!!
            mViewModel.currentPageMyUsers = 1
            mViewModel.getMyUsers()
            mViewModel.type = getString(R.string.yes)

        } else if (arguments?.getString(getString(R.string.register_message)).equals(getString(R.string.yes))) {
            isChallenegeType = true
            mViewModel.myFriends.value?.clear()
            mViewModel.selectFriendsAdapter.clearData()
            mViewModel.currentPageMyUsers = 1
            mViewModel.getMyUsers()
            mViewModel.type = getString(R.string.yes)
            mViewModel.selectFriendsAdapter.listenerChat = this.mClickHandler

        } else {
            isChallenegeType = false
            mViewModel.usersList.value?.clear()
            mViewModel.usersAdapter.clearData()
            mViewModel.currentPageAllUsers = 1
            mViewModel.getUsers()
            mViewModel.type = getString(R.string.no)

        }
    }

    fun onSwipe() {
        mBinding.lSwipe.setProgressBackgroundColorSchemeColor(resources.getColor(R.color.yellow));
        mBinding.lSwipe.setOnRefreshListener {
            loadData()
            mBinding.lSwipe.isRefreshing = false

        }
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsersViewModel::class.java)
    }


    inner class ClickHandler : AllUsersAdapter.onViewClick,
        SelectFriendsAdapter.onChatClickListener {

        override fun viewUserProfile(position: Int) {

            Log.e("positionnn", position.toString())

            var id = ""
            if (isChallenegeType) {
                id = mViewModel.myFriends.value!![position].senderUserId.toString()
            } else {
                id = mViewModel.usersList.value!![position].id.toString()

            }

            Log.e("IDDD", "IDD${id.toString()}")
            findNavController().navigate(
                R.id.userProfileFragment,
                bundleOf(getString(R.string.user_id) to id)
            )
        }

        override fun sendRequest(position: Int) {
            mViewModel.sendRequest(position)
        }

        override fun cancelrequest(position: Int) {
            mViewModel.cancelRequest(position)
        }

        override fun acceptRequest(position: Int) {
            mViewModel.acceptRejectRequest(position, getString(R.string.accept))
        }

        override fun rejectRequest(position: Int) {
            mViewModel.acceptRejectRequest(position, getString(R.string.reject))

        }

        override fun removeFriend(position: Int) {
            mViewModel.removeFriend(position)

        }

        override fun onChatClick(position: Int) {
            startActivity(
                Intent(
                    requireContext(),
                    ChatActivity::class.java
                ).putExtra(
                    getString(R.string.other_user_id),
                    mViewModel.myFriends.value!![position]?.otherUserDetail?.id
                ).putExtra(
                    getString(R.string.chat_room_id),
                    0
                ).putExtra(
                    getString(R.string.user),
                    mViewModel.myFriends.value!![position]?.otherUserDetail?.name
                )
            )
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

                if (it != null && it.isNotEmpty()) {
                    mBinding.tvNoDataFound.visibility = View.GONE
                    mViewModel.usersAdapter.setNewItems(it)
                } else {
                    if (mViewModel.usersList.value!!.size == 0) {
                        mBinding.tvNoDataFound.visibility = View.VISIBLE
                    }
                }


            })

            myFriends.observe(viewLifecycleOwner, Observer {


                Log.e("FRIENDDATA", it.size.toString())
                if (it != null && it.isNotEmpty()) {
                    mBinding.tvNoDataFound.visibility = View.GONE
                    mViewModel.selectFriendsAdapter.setNewItems(it)
                } else {
                    if (mViewModel.myFriends.value!!.size == 0) {
                        mBinding.tvNoDataFound.visibility = View.VISIBLE
                    }
                }
            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }


    private fun setupScrollListener() {

        if (isChallenegeType) {
            mEndlessMyChallengesRecyclerViewScrollListener =
                object :
                    EndlessRecyclerViewScrollListener(mBinding.recycler.layoutManager as LinearLayoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                        Log.e("ONLOADMOREMyUsers", "Onloadmore" + mViewModel.usersList.value?.size)

                        if (mViewModel.usersList.value!!.size % 10 == 0 && mViewModel.usersList.value!!.size != 0) {
                            mViewModel.getMyUsers()
                        }

                    }
                }
        } else {
            mEndlessMyChallengesRecyclerViewScrollListener =
                object :
                    EndlessRecyclerViewScrollListener(mBinding.recycler.layoutManager as LinearLayoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                        Log.e("ONLOADMOREAllUsers", "Onloadmore" + mViewModel.usersList.value?.size)

                        if (mViewModel.usersList.value!!.size % 10 == 0 && mViewModel.usersList.value!!.size != 0) {
                            mViewModel.getUsers()
                        }

                    }
                }
        }





        mBinding.recycler.addOnScrollListener(mEndlessMyChallengesRecyclerViewScrollListener!!)


    }


    private fun setupRecyclers() {
        mBinding.apply {


            if (isChallenegeType) {
                recycler.apply {
                    adapter = mViewModel.selectFriendsAdapter
                }
            } else {
                recycler.apply {
                    adapter = mViewModel.usersAdapter
                }

            }


        }
    }


    override fun onResume() {
        super.onResume()
        if (view == null) {
            return
        }
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener { v, keyCode, event ->
            if (event.action === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {

                Log.e("OnBaclPrese", "Onbaclpressed")
                onBackPressed()


                // handle back button's click listener
                true
            } else false
        }
    }

    private fun onBackPressed() {
        var selectedUsers = ArrayList<MyFriendsDataItem>()
        for (user in mViewModel.myFriends.value!!) {
            if (user.isSelected) {
                selectedUsers.add(user)
            }

        }

        Log.e("Listernerrr", "Lisuefh${listener}")

        getActivity()?.getIntent()?.putExtra("key", selectedUsers);

        listener?.onSelectUsers(selectedUsers)
        findNavController().navigateUp()

    }


    interface onSelect {
        fun onSelectUsers(selectedUsers: ArrayList<MyFriendsDataItem>)
    }

}