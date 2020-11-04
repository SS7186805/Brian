package com.brian.views.fragments

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brian.R
import com.brian.base.EndlessRecyclerViewScrollListener
import com.brian.base.ScopedFragment
import com.brian.databinding.MessagesFragmentBinding
import com.brian.internals.SwipeToDeleteCallback
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.messages.MessagesViewModel
import com.brian.viewModels.messages.MessagesViewModelFactory
import com.brian.views.activities.ChatActivity
import com.brian.views.adapters.MyMessagesAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class MessagesFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: MessagesViewModelFactory by instance()
    lateinit var mBinding: MessagesFragmentBinding
    lateinit var mViewModel: MessagesViewModel
    private val mClickHandler = ClickHandler()
    var mEndlessFriendsecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null
    var removePosition = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = MessagesFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        setupRecycler()
        setupObserver()
        setupScrollListener()

        return mBinding.root
    }


    override fun onResume() {
        super.onResume()
        mViewModel.currentPageAllChats = 1
        mViewModel.allChats.value?.clear()
        mViewModel.messagesAdapter.clearData()
        mViewModel.getAllChats()

    }

    private fun setupRecycler() {

        mViewModel.messagesAdapter.listener = this.mClickHandler
        mBinding.recycler.apply {
            adapter = mViewModel.messagesAdapter

        }
        enableSwipeToDelete()
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(MessagesViewModel::class.java)

    }


    inner class ClickHandler : MyMessagesAdapter.onViewClick {

        override fun onChatClick(position: Int) {
            startActivity(
                Intent(
                    requireContext(),
                    ChatActivity::class.java
                ).putExtra(
                    getString(R.string.other_user_id),
                    mViewModel.allChats.value!![position]?.otherUserDetail?.id
                ).putExtra(
                    getString(R.string.chat_room_id),
                    mViewModel.allChats.value!![position]?.lastMessage?.chatRoomId
                ).putExtra(
                    getString(R.string.user),
                    mViewModel.allChats.value!![position]?.otherUserDetail?.name
                )
            )
            mViewModel.allChats.value?.clear()
            mViewModel.messagesAdapter.clearData()
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

            allChats.observe(viewLifecycleOwner, Observer {
                if (it != null && it.isNotEmpty()) {
                    mBinding.tvNoDataFound.visibility = View.GONE
                    mViewModel.messagesAdapter.setNewItems(it)
                } else {
                    if (mViewModel.allChats.value!!.size == 0) {
                        mBinding.tvNoDataFound.visibility = View.VISIBLE
                    }
                }


            })

            removeChat.observe(viewLifecycleOwner, Observer {
                if (it.result?.contains(getString(R.string.success))!!) {
                    mViewModel.messagesAdapter.removeItem(removePosition)

                }


            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }

    private fun setupScrollListener() {

        mEndlessFriendsecyclerViewScrollListener =
            object :
                EndlessRecyclerViewScrollListener(mBinding.recycler.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    Log.e("ONLOADMOREMyUsers", "Onloadmore" + mViewModel.allChats.value?.size)

                    if (mViewModel.allChats.value!!.size % 10 == 0 && mViewModel.allChats.value!!.size != 0) {
                        mViewModel.getAllChats()
                    }

                }
            }



        mBinding.recycler.addOnScrollListener(mEndlessFriendsecyclerViewScrollListener!!)


    }


    private fun enableSwipeToDelete() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                removePosition = viewHolder.adapterPosition
                mViewModel.removeChatParams.chat_room_id =
                    mViewModel.allChats.value!![removePosition].lastMessage?.chatRoomId
                mViewModel.removeChat()


            }
        }
        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(mBinding.recycler)
    }
}