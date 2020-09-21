package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.MessagesFragmentBinding
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.adapters.MessageData
import com.brian.views.adapters.MyMessagesAdapter
import com.brian.views.adapters.SwipeToDeleteAdapter
import com.ernestoyaquello.dragdropswiperecyclerview.DragDropSwipeRecyclerView
import com.ernestoyaquello.dragdropswiperecyclerview.listener.OnItemSwipeListener
import com.nikhilpanju.recyclerviewenhanced.RecyclerTouchListener
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class MessagesFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: MessagesFragmentBinding
    lateinit var mViewModel: RegisterViewModel
    private val mClickHandler = ClickHandler()

    var messagesAdapter:MyMessagesAdapter?=null
    var list=ArrayList<MessageData>()
    private lateinit var mAdapter: SwipeToDeleteAdapter
    private val onItemSwipeListener = object : OnItemSwipeListener<MessageData> {
        override fun onItemSwiped(position: Int, direction: OnItemSwipeListener.SwipeDirection, item: MessageData): Boolean {
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

        return mBinding.root
    }


    private fun setupRecycler() {
        for (i in 0 until 5){
            list.add(MessageData("${i}"))
        }
        mAdapter = SwipeToDeleteAdapter(list)
        mBinding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            swipeListener = onItemSwipeListener
            adapter = mAdapter
            dragListener = null
            disableSwipeDirection(DragDropSwipeRecyclerView.ListOrientation.DirectionFlag.RIGHT)
            behindSwipedItemLayoutId =  R.layout.layout_behind_item
            orientation = DragDropSwipeRecyclerView.ListOrientation.VERTICAL_LIST_WITH_VERTICAL_DRAGGING
        }
    }
    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)

    }

    /*fun setAdapter(){
        list.clear()
        messagesAdapter= MyMessagesAdapter(R.layout.messages)

        mBinding.recycler.layoutManager=LinearLayoutManager(requireContext())
        mBinding.recycler.adapter=messagesAdapter
        messagesAdapter!!.listener=this.mClickHandler
        messagesAdapter!!.addNewItems(list)
    }*/

    inner class ClickHandler : MyMessagesAdapter.onViewClick{

        override fun onAprroveClick() {
            findNavController().navigate(R.id.chatFragment)
        }
    }

    fun swipeAction(){

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