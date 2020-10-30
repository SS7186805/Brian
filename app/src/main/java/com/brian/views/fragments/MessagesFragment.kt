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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.MessagesFragmentBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.messages.MessagesViewModel
import com.brian.viewModels.messages.MessagesViewModelFactory
import com.brian.views.adapters.MessageData
import com.brian.views.adapters.SwipeToDeleteAdapter
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
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

        mAdapter = SwipeToDeleteAdapter(mViewModel.allChats.value!!)
        mAdapter.listener = this.mClickHandler
        mBinding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            swipeListener = onItemSwipeListener
            adapter = mAdapter
            dragListener = null
            disableSwipeDirection(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.RIGHT)
            behindSwipedItemLayoutId = R.layout.layout_behind_item
            orientation =
                DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING
        }
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(MessagesViewModel::class.java)

    }


    inner class ClickHandler : SwipeToDeleteAdapter.onViewClick {

        override fun onAprroveClick() {
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
                    mAdapter.dataSet = mViewModel.allChats.value!!
                    mAdapter.notifyDataSetChanged()
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
                val view = recycler.getChildAt(recycler.childCount - 1)
                val diff = view.bottom - (recycler.height + recycler.scrollY)
                val offset = mViewModel.allChats.value?.size
                if (diff == 0 && offset!! % 10 == 0 && !mViewModel.allChatsLoaded) {
                    mViewModel.getAllChats()
                }
            }

        }
    }


    fun swipeAction() {

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT or ItemTouchHelper.DOWN or ItemTouchHelper.UP
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                //Remove swiped item from list and notify the RecyclerView
                val position = viewHolder.adapterPosition
            }
        }
        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(mBinding.recycler)
    }
}