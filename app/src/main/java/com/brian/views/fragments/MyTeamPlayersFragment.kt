package com.brian.views.fragments

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
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.MyTeamsPlayersFragmentBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.models.MyTeamMembersItem
import com.brian.viewModels.homescreen.HomeViewModel
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import com.brian.views.adapters.MyTeamMembersAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class MyTeamPlayersFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: HomescreenViewModelFactory by instance()
    lateinit var mBinding: MyTeamsPlayersFragmentBinding
    lateinit var mViewModel: HomeViewModel
    var teamMembers = ArrayList<MyTeamMembersItem>()
    var clickHandler = ClickHandler()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = MyTeamsPlayersFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        mBinding.toolbar.tvTitle.text = getString(R.string.team_player)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        mViewModel.teamPlayersAdapter.listener = this.clickHandler

        setupRecyclers()
        return mBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        teamMembers =
            arguments?.getParcelableArrayList<MyTeamMembersItem>(getString(R.string.team_player)) as ArrayList<MyTeamMembersItem>

        if (teamMembers.isNotEmpty()) {
            mViewModel.teamPlayersAdapter.setNewItems(teamMembers)
        }


    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    inner class ClickHandler : MyTeamMembersAdapter.onClickEvents {


        override fun onPlayerClick(position: Int) {
            Log.e("Userid", teamMembers[position].userId.toString())
            findNavController().navigate(
                R.id.userProfileFragment,
                bundleOf(getString(R.string.user_id) to teamMembers[position].userId.toString())
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



            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }


    private fun setupRecyclers() {
        mBinding.apply {
            recycler.apply {
                adapter = mViewModel.teamPlayersAdapter
            }

        }
    }


}