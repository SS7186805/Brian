package com.brian.views.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brian.R
import com.brian.base.EndlessRecyclerViewScrollListener
import com.brian.base.ScopedFragment
import com.brian.databinding.MyChallengesFragmentBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.viewModels.challenges.ChallengesViewModel
import com.brian.viewModels.challenges.ChallengesViewModelFactory
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class MyChallenegesFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: ChallengesViewModelFactory by instance()
    lateinit var mBinding: MyChallengesFragmentBinding
    lateinit var mViewModel: ChallengesViewModel
    private val mClickHandler = ClickHandler()
    var mEndlessMyChallengesRecyclerViewScrollListener: EndlessRecyclerViewScrollListener? = null


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

        mViewModel.getApprovedMyChallenges()
        setupRecyclers()
        setupObserver()
        setupScrollListener()

        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(ChallengesViewModel::class.java)
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

            myChallenges.observe(viewLifecycleOwner, Observer {

                if (it != null && it.isNotEmpty()) {
                    mBinding.tvNoDataFound.visibility = View.GONE
                    mViewModel.myChallengesAdapter.setNewItems(it)
                } else {
                    if (mViewModel.myChallenges.value!!.size == 0) {
                        mBinding.tvNoDataFound.visibility = View.VISIBLE
                    }
                }


            })


            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }

    private fun setupScrollListener() {
        mEndlessMyChallengesRecyclerViewScrollListener =
            object :
                EndlessRecyclerViewScrollListener(mBinding.recycler.layoutManager as LinearLayoutManager) {
                override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                    Log.e("ONLOADMORE", "Onloadmore" + mViewModel.myChallenges.value?.size)

                    if (mViewModel.myChallenges.value!!.size % 10 == 0 && mViewModel.myChallenges.value!!.size != 0) {
                        mViewModel.getApprovedMyChallenges()
                    }

                }
            }



        mBinding.recycler.addOnScrollListener(mEndlessMyChallengesRecyclerViewScrollListener!!)


    }


    private fun setupRecyclers() {
        mBinding.apply {


            recycler.apply {
                adapter = mViewModel.myChallengesAdapter

            }


        }
    }


}