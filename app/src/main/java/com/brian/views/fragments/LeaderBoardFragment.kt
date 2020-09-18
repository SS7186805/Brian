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
import com.brian.databinding.LeaderboardFragmentBinding
import com.brian.databinding.MyChallengesItemBinding
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.adapters.*
import com.google.android.material.tabs.TabLayout
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class LeaderBoardFragment : ScopedFragment(), KodeinAware, TabLayout.OnTabSelectedListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: LeaderboardFragmentBinding
    lateinit var mViewModel: RegisterViewModel
    var myChallengesAdapter:LeaderboardChallengeAdapter?=null
    var challengeRequestsAdapter:MyChallengesAdapter?=null
    var challenges=ArrayList<LeaderBoardChallenge>()
    private val mClickHandler = ClickHandler()



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = LeaderboardFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        setAdapter()

        mBinding.tabs.setOnTabSelectedListener(this)
        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }


    fun setAdapter(){
        mBinding.rChallenges.layoutManager=LinearLayoutManager(requireContext())
        mBinding.rPlayers.layoutManager=LinearLayoutManager(requireContext())

        //Add My Challenges
        challenges.add(LeaderBoardChallenge(1,50))
        challenges.add(LeaderBoardChallenge(2,49))
        challenges.add(LeaderBoardChallenge(3,48))
        challenges.add(LeaderBoardChallenge(0,30))
        challenges.add(LeaderBoardChallenge(0,30))
        challenges.add(LeaderBoardChallenge(0,30))


        myChallengesAdapter= LeaderboardChallengeAdapter(R.layout.leaderboard)


        mBinding.rChallenges.adapter=myChallengesAdapter

        myChallengesAdapter!!.addNewItems(challenges)

    }

    inner class ClickHandler  {


    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    @SuppressLint("NewApi")
    override fun onTabSelected(tab: TabLayout.Tab?) {

        if(tab?.position==0){
            mBinding.rChallenges.visibility=VISIBLE
            mBinding.rPlayers.visibility=GONE
        }else{
            mBinding.rChallenges.visibility= GONE
            mBinding.rPlayers.visibility= VISIBLE
        }

    }
}