package com.brian.views.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.ChallengesFragmentBinding
import com.brian.databinding.MyChallengesItemBinding
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.adapters.ChallengeRequestsAdapter
import com.brian.views.adapters.MyChallenges
import com.brian.views.adapters.MyChallengesAdapter
import com.google.android.material.tabs.TabLayout
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ChallengesFragment : ScopedFragment(), KodeinAware, TabLayout.OnTabSelectedListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: ChallengesFragmentBinding
    lateinit var mViewModel: RegisterViewModel
    var myChallengesAdapter:MyChallengesAdapter?=null
    var challengeRequestsAdapter:MyChallengesAdapter?=null
    var myChallenges=ArrayList<MyChallenges>()
    var challengeRequests=ArrayList<MyChallenges>()
    private val mClickHandler = ClickHandler()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = ChallengesFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        mBinding.tabs.setOnTabSelectedListener(this)
        setAdapter()
        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }


    fun setAdapter(){
        mBinding.rMyChallenges.layoutManager=LinearLayoutManager(requireContext())
        mBinding.rChallengeRequests.layoutManager=LinearLayoutManager(requireContext())

        //Add My Challenges
        myChallenges.add((MyChallenges(false,false,false)))
        myChallenges.add((MyChallenges(true,false,false)))
        myChallenges.add((MyChallenges(true,true,false)))

        //Add Challenge Requests
        challengeRequests.add((MyChallenges(false,false,true)))
        challengeRequests.add((MyChallenges(false,false,true)))
        challengeRequests.add((MyChallenges(false,false,true)))

        myChallengesAdapter= MyChallengesAdapter(R.layout.my_challenges_item)
        challengeRequestsAdapter=MyChallengesAdapter(R.layout.my_challenges_item)

        challengeRequestsAdapter!!.listener=this.mClickHandler

        mBinding.rMyChallenges.adapter=myChallengesAdapter
        mBinding.rChallengeRequests.adapter=challengeRequestsAdapter

        myChallengesAdapter!!.addNewItems(myChallenges)
        challengeRequestsAdapter!!.addNewItems(challengeRequests)

    }

    inner class ClickHandler : MyChallengesAdapter.onViewClick {
        override fun onAprroveClick() {
            findNavController().navigate(R.id.challenegeFragment)
        }

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    @SuppressLint("NewApi")
    override fun onTabSelected(tab: TabLayout.Tab?) {

        if(tab?.position==0){
            mBinding.rMyChallenges.visibility=VISIBLE
            mBinding.rChallengeRequests.visibility=GONE
        }else{
            mBinding.rMyChallenges.visibility= GONE
            mBinding.rChallengeRequests.visibility= VISIBLE
        }

    }
}