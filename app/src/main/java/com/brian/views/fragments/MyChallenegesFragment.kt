package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.MyChallengesFragmentBinding
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.adapters.MyChallenges
import com.brian.views.adapters.MyChallengesAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class MyChallenegesFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: MyChallengesFragmentBinding
    lateinit var mViewModel: RegisterViewModel
    var myChallengesAdapter: MyChallengesAdapter? = null
    var myChallenges = ArrayList<MyChallenges>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = MyChallengesFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        mBinding.toolbar.tvTitle.text = getString(R.string.my_challenges)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }
/*
    fun setAdapter(){
        mBinding.recycler.layoutManager= LinearLayoutManager(requireContext())

        //Add My Challenges
        myChallenges.add((MyChallenges(true,false,false)))
        myChallenges.add((MyChallenges(true,false,false)))
        myChallenges.add((MyChallenges(true,false,false)))

        myChallengesAdapter= MyChallengesAdapter(R.layout.my_challenges_item)


        mBinding.recycler.adapter=myChallengesAdapter

        myChallengesAdapter!!.addNewItems(myChallenges)

    }*/

    inner class ClickHandler {

        fun onSelectChallenge() {

        }

    }

}