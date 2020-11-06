package com.brian.views.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.Prefs
import com.brian.base.ScopedFragment
import com.brian.databinding.ChallengeTypeFragmentBinding
import com.brian.internals.DialogUtil
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.models.ChallengeTypeDataItem
import com.brian.viewModels.challenges.ChallengesViewModel
import com.brian.viewModels.challenges.ChallengesViewModelFactory
import com.brian.views.adapters.ChallengeTypeAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class ChallengeTypeFragment : ScopedFragment(), KodeinAware, DialogUtil.SuccessClickListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: ChallengesViewModelFactory by instance()
    lateinit var mBinding: ChallengeTypeFragmentBinding
    lateinit var mViewModel: ChallengesViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = ChallengeTypeFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        mBinding.toolbar.tvTitle.text = getString(R.string.challenge_type)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        if (arguments?.getString("badges").equals("badges")) {
            mBinding.toolbar.tvTitle.setText(getString(R.string.badges_earned))
            mViewModel.viewProfile(Prefs.init().userInfo?.id.toString())

        } else {
            mViewModel.getAllChallenges()
            mViewModel.allChallengesAdapter.listerner = this.ClickHandler()


        }

        setupObserver()
        setupRecyclers()
        setupScrollListener()

        return mBinding.root
    }


    fun setBadgesEarned() {


        var list = ArrayList<ChallengeTypeDataItem>()


        for (badge in Prefs.init().userInfo?.badgesEarneda?.data!!) {
            list.add(ChallengeTypeDataItem(badge.challenge?.image, badge.challengeTitle))
        }
        mViewModel.allChallenges.postValue(list)


    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(ChallengesViewModel::class.java)
    }

    inner class ClickHandler : ChallengeTypeAdapter.onClickView {
        override fun onClickItem(position: Int) {

            getActivity()?.getIntent()?.putExtra(
                getString(R.string.title),
                mViewModel.allChallenges.value!![position].challengeName
            )?.putExtra(
                getString(R.string.id),
                mViewModel.allChallenges.value!![position].id.toString()
            )
            findNavController().navigateUp()

        }


    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (arguments?.getString("badges").equals("badges")) {

            mBinding.toolbar.tvTitle.setText(getString(R.string.badges_earned))


        }
    }

    override fun onOkayClick() {
    }

    private fun setupObserver() {
        mViewModel.apply {
            showMessage.observe(viewLifecycleOwner, Observer {
                if (!TextUtils.isEmpty(it)) {
                    requireContext().showToast(it)
                    showMessage.postValue("")
                }
            })

            mViewModel.loginDataa.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    setBadgesEarned()
                }
            })

            allChallenges.observe(viewLifecycleOwner, Observer {
                if (it.isNotEmpty()) {
                    mViewModel.allChallengesAdapter.setNewItems(it)
                } else {
                    if (currentPageAllChalleneges == 1) {
                        mViewModel.allChallengesAdapter.setNewItems(it)
                    }
                }

                if (mViewModel.allChallenges.value.isNullOrEmpty()) {
                    mBinding.tvNobadges.visibility = View.VISIBLE
                } else {
                    mBinding.tvNobadges.visibility = View.GONE

                }

            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }

    private fun setupScrollListener() {
        /* mBinding.apply {
             recycler.setOnScrollChangeListener { _, _, _, _, _ ->

                 if (mViewModel.allChallenges.value?.isNotEmpty()!!) {
                     val view = recycler.getChildAt(recycler.childCount - 1)
                     val diff = view.bottom - (recycler.height + recycler.scrollY)
                     val offset = mViewModel.allChallenges.value?.size
                     if (diff == 0 && offset!! % 10 == 0 && !mViewModel.allChallengesLoaded) {
                         mViewModel.getAllChallenges()
                     }
                 }

             }

         }*/
    }


    private fun setupRecyclers() {
        mBinding.apply {
            recycler.apply {
                adapter = mViewModel.allChallengesAdapter
            }

        }
    }
}