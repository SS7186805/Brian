package com.brian.views.fragments

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.GameSummaryFragmentBinding
import com.brian.internals.hideProgress
import com.brian.internals.showProgress
import com.brian.internals.showToast
import com.brian.models.QuestionData
import com.brian.models.SubmittedAnswersItem
import com.brian.viewModels.homescreen.HomeViewModel
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import com.brian.views.adapters.GameSummaryAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class GameSummaryFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: HomescreenViewModelFactory by instance()
    lateinit var mBinding: GameSummaryFragmentBinding
    lateinit var mViewModel: HomeViewModel
    lateinit var gameSummaryAdapter: GameSummaryAdapter

    var correctAnswer = 0
    var wrongeAnswer = 0
    var gameSummarylist = ArrayList<QuestionData>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = GameSummaryFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        mBinding.toolbar.tvTitle.text = getString(R.string.game_summary)
        mBinding.toolbar.ivBack.visibility = GONE
        mBinding.totalQuestionsCount.text = "5"

        mViewModel.selectDefensiveId =
            arguments?.getInt(getString(R.string.defensive_situation), 0)!!

        mViewModel.getGameSummary()

        setupObserver()

        setupRecycler()
        return mBinding.root
    }

    private fun setupRecycler() {
        mBinding.recyclerGameSummary.layoutManager = LinearLayoutManager(context)
        gameSummaryAdapter = GameSummaryAdapter(R.layout.question_item)
        mBinding.recyclerGameSummary.adapter = gameSummaryAdapter
    }


    private fun setupObserver() {
        mViewModel.apply {
            showMessage.observe(viewLifecycleOwner, Observer {
                if (!TextUtils.isEmpty(it)) {
                    requireContext().showToast(it)
                    showMessage.postValue("")
                }
            })

            gameSummaryResponse.observe(viewLifecycleOwner, Observer {
                mBinding.tScore.text = "Score: ${it.data?.correctAnswer!!.toInt() * 100}"
                mBinding.average.text = "Average: ${(it.data?.correctAnswer * 100) / 5}%"

                mBinding.correctAnswersCount.text = it.data?.correctAnswer.toString()
                mBinding.incorrectAnswersCount.text = it.data?.incorrectAnswer.toString()

                gameSummaryAdapter.setNewItems(gameSummaryResponse.value?.data?.submittedAnswers as List<SubmittedAnswersItem>)

            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) showProgress(requireContext()) else hideProgress()
            })
        }
    }


    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        correctAnswer = arguments?.getInt("correctAnswer")!!
        wrongeAnswer = arguments?.getInt("wrongeAnswer")!!

    }

    inner class ClickHandler {

        fun onPitchClick() {
            findNavController().navigate(R.id.homeFragment)
        }

    }


}