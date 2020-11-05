package com.brian.views.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.MyStatsFragmentBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.models.DataBadges
import com.brian.viewModels.homescreen.HomeViewModel
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import com.brian.views.adapters.BadgesAdapter
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class MyStatsFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: HomescreenViewModelFactory by instance()
    lateinit var mBinding: MyStatsFragmentBinding
    lateinit var mViewModel: HomeViewModel
    var badgesAdapter: BadgesAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = MyStatsFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        setupObserver()
        mViewModel.getMyStats()

        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    inner class ClickHandler {


    }


    private fun setupObserver() {
        mViewModel.apply {
            showMessage.observe(viewLifecycleOwner, Observer {
                if (!TextUtils.isEmpty(it)) {
                    requireContext().showToast(it)
                    showMessage.postValue("")
                }
            })

            myStats.observe(viewLifecycleOwner, Observer {
                if (it != null) {

                    for (progress in 0..it.defensiveAvg?.toInt()!!) {
                        mBinding.progressBar.progress = progress

                    }
                    mBinding.tvChalleneges.setText(it.challengesCompleted.toString())
                    mBinding.tvAverage.setText("${it.defensiveAvg.toString()} %")
                    if (it.badgesEarneda?.data?.isNotEmpty()!!) {
                        mBinding.tvNoBadges.visibility = View.GONE
                        setBadgesAdapter(it.badgesEarneda?.data!!)

                    } else {
                        mBinding.tvNoBadges.visibility = View.VISIBLE
                    }
                }
            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }

    fun setBadgesAdapter(list: ArrayList<DataBadges>) {
        mBinding.recycler.layoutManager = GridLayoutManager(requireContext(), 4)
        badgesAdapter = BadgesAdapter(R.layout.badge_item)
        badgesAdapter!!.setNewItems(list)
        mBinding.recycler.adapter = badgesAdapter

    }

}