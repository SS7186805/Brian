package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.QuestionsFragmentBinding
import com.brian.internals.interfaces.ItemClickListener
import com.brian.internals.toArrayList
import com.brian.models.AnswersItem
import com.brian.viewModels.homescreen.HomeViewModel
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import com.brian.views.adapters.QuestionAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.layout_question_recycler_item.*
import kotlinx.android.synthetic.main.questions_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class QuestionsFragment : ScopedFragment(), ItemClickListener, KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: HomescreenViewModelFactory by instance()
    lateinit var mBinding: QuestionsFragmentBinding
    lateinit var mViewModel: HomeViewModel
    lateinit var questionAdapter: QuestionAdapter
    var answerlist = ArrayList<AnswersItem>()

    var selectedAnswer=true


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()

        mViewModel.questionRespone()
        mBinding = QuestionsFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        mBinding.toolbar.tvTitle.text = getString(R.string.situation_name)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }


        setUpObserver()
        setupRecycler()

        return mBinding.root
    }


    private fun setupRecycler() {
        mBinding.QuestionRecycler.layoutManager = GridLayoutManager(context, 2)
        questionAdapter = QuestionAdapter(R.layout.layout_question_recycler_item, this)
        mBinding.QuestionRecycler.adapter = questionAdapter
        mBinding.QuestionRecycler.itemAnimator?.changeDuration=0
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    private fun setUpObserver() {
        mViewModel.apply {
            data.observe(viewLifecycleOwner, Observer {
                description.text = it.question
                tv_u_r_score.text = it.youAre.toString()
                tv_runner_on_score.text = it.runnersOn.toString()
                tv_out_score.text = it.out.toString()
                thinkDescription.text = it.alsoThinkAboutIt
                answerlist = it.answers!!.toArrayList()
                setupRecycler()
                questionAdapter.setNewItems(answerlist)
            })
        }
    }

    override fun onClick(data: AnswersItem, position: Int) {
        Toast.makeText(context, data.answer, Toast.LENGTH_SHORT).show()
    }



    inner class ClickHandler {
        var count = 0
        fun onNextClick() {
            count++
            mViewModel.questionRespone()
            if (count == 5) {
                findNavController().navigate(R.id.gameSummaryFragment)
            }
        }
    }
}