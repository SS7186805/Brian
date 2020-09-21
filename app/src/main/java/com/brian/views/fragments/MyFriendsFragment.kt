package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.MyFriendsFragmentBinding
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.adapters.MyFriends
import com.brian.views.adapters.MyFriendsAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class MyFriendsFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: MyFriendsFragmentBinding
    lateinit var mViewModel: RegisterViewModel
    var list = ArrayList<MyFriends>()
    var friendsAdapter: MyFriendsAdapter? = null
    private val mClickHandler = ClickHandler()


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
        setAdapter()
        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    fun setAdapter() {
        list.clear()
        friendsAdapter = MyFriendsAdapter(R.layout.my_friends_item)

        if(arguments?.getString("no").equals("no")){

            list.add(MyFriends(false, true, false))
            list.add(MyFriends(false, true, false))
            list.add(MyFriends(false, true, false))
            list.add(MyFriends(false, true, false))
        }else{
            list.add(MyFriends(false, false, false))
            list.add(MyFriends(false, false, false))
            list.add(MyFriends(false, true, false))
            list.add(MyFriends(false, true, false))
        }



        mBinding.recycler.layoutManager = LinearLayoutManager(requireContext())
        mBinding.recycler.adapter = friendsAdapter
        friendsAdapter!!.listener = this.mClickHandler
        friendsAdapter!!.addNewItems(list)
    }


    inner class ClickHandler : MyFriendsAdapter.onViewClick {
        override fun onAprroveClick() {
            findNavController().navigate(R.id.userProfileFragment)
        }

    }
}