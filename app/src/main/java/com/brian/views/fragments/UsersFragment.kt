package com.brian.views.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
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
import com.brian.models.MyFriendsDataItem
import com.brian.viewModels.users.UsersViewModel
import com.brian.viewModels.users.UsersViewModelFactory
import com.brian.views.adapters.AllUsersAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class UsersFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: UsersViewModelFactory by instance()
    lateinit var mBinding: UsersFragmentBinding
    lateinit var mViewModel: UsersViewModel
    private val mClickHandler = ClickHandler()
    var listener:onSelect?=null


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
            onBackPressed()
        }


        mBinding.etSearchBar.addTextChangedListener(mViewModel.textWatcher)
        mViewModel.usersAdapter.listener = this.mClickHandler
        setupRecyclers()
        setupScrollListener()
        setupObserver()

        if (arguments?.getString(getString(R.string.challenge_type))?.contains(getString(R.string.yes))!!) {
            mViewModel.getMyUsers()
            mViewModel.type = getString(R.string.yes)

        } else {
            mViewModel.getUsers()
            mViewModel.type = getString(R.string.no)

        }


        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(UsersViewModel::class.java)
    }


    inner class ClickHandler : AllUsersAdapter.onViewClick {

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
            mViewModel.acceptRejectRequest(position, getString(R.string.accept))
        }

        override fun rejectRequest(position: Int) {
        }

        override fun removeFriend(position: Int) {
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

            if (arguments?.getString(getString(R.string.challenge_type))?.contains(getString(R.string.yes))!!) {
                recycler.setOnScrollChangeListener { _, _, _, _, _ ->

                    if (mViewModel.myFriends.value?.isNotEmpty()!!) {
                        val view = recycler.getChildAt(recycler.childCount - 1)
                        val diff = view.bottom - (recycler.height + recycler.scrollY)
                        val offset = mViewModel.myFriends.value?.size
                        if (diff == 0 && offset!! % 10 == 0 && !mViewModel.allFriendsLoaded) {
                            mViewModel.getMyUsers()
                        }
                    }


                }

            } else {
                recycler.setOnScrollChangeListener { _, _, _, _, _ ->

                    if (mViewModel.usersList.value?.isNotEmpty()!!) {
                        val view = recycler.getChildAt(recycler.childCount - 1)
                        val diff = view.bottom - (recycler.height + recycler.scrollY)
                        val offset = mViewModel.usersList.value?.size
                        if (diff == 0 && offset!! % 10 == 0 && !mViewModel.allUsersLoaded) {
                            mViewModel.getUsers()
                        }
                    }


                }

            }

        }
    }


    private fun setupRecyclers() {
        mBinding.apply {


            if (arguments?.getString(getString(R.string.challenge_type))?.contains(getString(R.string.yes))!!) {
                recycler.apply {
                    adapter = mViewModel.myFriendsAdapter
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

                Log.e("OnBaclPrese","Onbaclpressed")
               onBackPressed()



                // handle back button's click listener
                true
            } else false
        }
    }

    private fun onBackPressed() {
        var selectedUsers=ArrayList<MyFriendsDataItem>()
        for (user in mViewModel.myFriends.value!!){
            if(user.isSelected){
                selectedUsers.add(user)
            }

        }

        Log.e("Listernerrr","Lisuefh${listener}")

        getActivity()?.getIntent()?.putExtra("key", selectedUsers);

        listener?.onSelectUsers(selectedUsers)
        findNavController().navigateUp()

    }


    interface onSelect{
        fun onSelectUsers(selectedUsers:ArrayList<MyFriendsDataItem>)
    }

}