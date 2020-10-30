package com.brian.views.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.MessagesFragmentBinding
import com.brian.internals.SwipeToDeleteCallback
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.messages.MessagesViewModel
import com.brian.viewModels.messages.MessagesViewModelFactory
import com.brian.views.adapters.MessageData
import com.brian.views.adapters.MyMessagesAdapter
import com.brian.views.adapters.SwipeToDeleteAdapter
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class MessagesFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: MessagesViewModelFactory by instance()
    lateinit var mBinding: MessagesFragmentBinding
    lateinit var mViewModel: MessagesViewModel
    private val mClickHandler = ClickHandler()
    private lateinit var mAdapter: SwipeToDeleteAdapter
    private val onItemSwipeListener = object : OnItemSwipeListener<MessageData> {
        override fun onItemSwiped(
            position: Int,
            direction: OnItemSwipeListener.SwipeDirection,
            item: MessageData
        ): Boolean {
            // Handle action of item swiped
            // Return false to indicate that the swiped item should be removed from the adapter's data set (default behaviour)
            // Return true to stop the swiped item from being automatically removed from the adapter's data set (in this case, it will be your responsibility to manually update the data set as necessary)
            return false
        }
    }


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

        mViewModel.getAllChats()
        return mBinding.root
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

        override fun onChatClick() {
            mViewModel.allChats.value?.clear()
            mViewModel.messagesAdapter.clearData()
            findNavController().navigate(R.id.chatFragment)
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
                if (it.isNotEmpty()) {
                    messagesAdapter.addNewItems(it)
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
                if(mViewModel.allChats.value?.isNotEmpty()!!){
                    val view = recycler.getChildAt(recycler.childCount - 1)
                    val diff = view.bottom - (recycler.height + recycler.scrollY)
                    val offset = mViewModel.allChats.value?.size
                    if (diff == 0 && offset!! % 10 == 0 && !mViewModel.allChatsLoaded) {
                        mViewModel.getAllChats()
                    }
                }

            }

        }
    }


    private fun enableSwipeToDelete() {
        val swipeToDeleteCallback = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, i: Int) {
                val position = viewHolder.adapterPosition
                mViewModel.messagesAdapter.removeItem(position)


            }
        }
        val itemTouchhelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchhelper.attachToRecyclerView(mBinding.recycler)
    }
}