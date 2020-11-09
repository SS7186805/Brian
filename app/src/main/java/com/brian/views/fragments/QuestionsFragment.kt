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
import androidx.recyclerview.widget.GridLayoutManager
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.QuestionsFragmentBinding
import com.brian.internals.DialogUtil
import com.brian.internals.interfaces.ItemClickListener
import com.brian.internals.showToast
import com.brian.internals.toArrayList
import com.brian.models.AnswersItem
import com.brian.models.QuestionData
import com.brian.viewModels.homescreen.HomeViewModel
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import com.brian.views.adapters.QuestionAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.questions_fragment.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance


class QuestionsFragment : ScopedFragment(), ItemClickListener, KodeinAware,
    DialogUtil.ErrorClickListener {
    override val kodein by closestKodein()
    private val viewModelFactory: HomescreenViewModelFactory by instance()
    lateinit var mBinding: QuestionsFragmentBinding
    lateinit var mViewModel: HomeViewModel
    lateinit var questionAdapter: QuestionAdapter
    var answerlist = ArrayList<AnswersItem>()
    var count = 0
    var currentSelect: Int = 0
    var previousSelect: Int = 0
    var correctAnswer: Int = 0
    var wrongeAnswer: Int = 0
    var questionSummary = ArrayList<QuestionData>()
    var questionId = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()

        mViewModel.questionsId = arguments?.getString(getString(R.string.id)).toString().toInt()
        mViewModel.submitAnswerParams.selected_defensive_situation_id =
            arguments?.getInt(getString(R.string.defensive_situation), 0)

        mViewModel.questionRespone()
        mBinding = QuestionsFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
//        mBinding.toolbar.tvTitle.text = getString(R.string.situation_name)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        if (count <= 5)
            mBinding.Questiontitle.setText("Question ${count + 1}")


        setUpObserver()
        setupRecycler()

        return mBinding.root
    }


    private fun setupRecycler() {
        mBinding.QuestionRecycler.layoutManager = GridLayoutManager(context, 2)
        questionAdapter = QuestionAdapter(R.layout.layout_question_recycler_item, this)
        mBinding.QuestionRecycler.adapter = questionAdapter
        mBinding.QuestionRecycler.itemAnimator?.changeDuration = 0
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    private fun setUpObserver() {
        mViewModel.apply {

            showMessage.observe(viewLifecycleOwner, Observer {
                if (!TextUtils.isEmpty(it)) {
                    if (it.contains(getString(R.string.Questions_not_available))) {
                        DialogUtil.build(requireContext()) {
                            title = getString(R.string.error)
                            dialogType = DialogUtil.DialogType.ERROR
                            message = getString(R.string.Questions_not_available)
                            errorClickListener = this@QuestionsFragment
                        }
                        showMessage.postValue("")

                    } else {
                        requireContext().showToast(it)
                        showMessage.postValue("")
                    }

                }
            })

            data.observe(viewLifecycleOwner, Observer {

                Log.e("QuestionIdd", it.id.toString())
                description.text = it.question
                tv_u_r_score.text = it.youAre.toString()
                tv_runner_on_score.text = it.runnersOn.toString()
                tv_out_score.text = it.out.toString()
                thinkDescription.text = it.alsoThinkAboutIt
                answerlist = it.answers!!.toArrayList()
                questionSummary.add(it)
                setupRecycler()
                questionAdapter.setNewItems(answerlist)
            })

            showLoading.observe(viewLifecycleOwner, Observer {
                if (it) {
                    mBinding.QProgressBar.visibility = View.VISIBLE
                } else {
                    mBinding.QProgressBar.visibility = View.GONE
                }
            })
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var pageName = arguments?.getString("name")
        mBinding.toolbar.tvTitle.text = pageName
    }

    override fun onClick(data: AnswersItem, position: Int, correct: Boolean) {
//        Toast.makeText(context, data.answer, Toast.LENGTH_SHORT).show()

        Log.e("OnCLickQuestionIdd", data.questionId.toString())

        mViewModel.submitAnswerParams.answer_id = data.id
        mViewModel.submitAnswerParams.question_id = data.questionId
        mViewModel.submitAnswer()


        ++currentSelect
        if (correct && count == 0) {
            correctAnswer++
            view1.setBackgroundResource(R.drawable.green_dot)
        } else if (!correct && count == 0) {
            wrongeAnswer++
            view1.setBackgroundResource(R.drawable.red_dot)
        } else if (correct && count == 1) {
            correctAnswer++
            view2.setBackgroundResource(R.drawable.green_dot)
        } else if (!correct && count == 1) {
            wrongeAnswer++
            view2.setBackgroundResource(R.drawable.red_dot)
        } else if (correct && count == 2) {
            correctAnswer++
            view3.setBackgroundResource(R.drawable.green_dot)
        } else if (!correct && count == 2) {
            wrongeAnswer++
            view3.setBackgroundResource(R.drawable.red_dot)
        } else if (correct && count == 3) {
            correctAnswer++
            view4.setBackgroundResource(R.drawable.green_dot)
        } else if (!correct && count == 3) {
            wrongeAnswer++
            view4.setBackgroundResource(R.drawable.red_dot)
        } else if (correct && count == 4) {
            correctAnswer++
            view5.setBackgroundResource(R.drawable.green_dot)
        } else if (!correct && count == 4) {
            wrongeAnswer++
            view5.setBackgroundResource(R.drawable.red_dot)
        }

    }

    inner class ClickHandler {

        fun onNextClick() {
            if (currentSelect == previousSelect + 1) {
                previousSelect = currentSelect
                count++
                Questiontitle.setText("Question ${count + 1}")
                mViewModel.questionRespone()
                if (count == 5) {
                    println("correctAnswer->$correctAnswer")
                    println("wrongeAnswer->$wrongeAnswer")
                    findNavController().navigate(
                        R.id.gameSummaryFragment,
                        bundleOf(
                            "correctAnswer" to correctAnswer,
                            "wrongeAnswer" to wrongeAnswer,
                            "questionSammary" to questionSummary,
                            getString(R.string.defensive_situation) to   mViewModel.submitAnswerParams.selected_defensive_situation_id
                        )
                    )
                }
            }
        }
    }

    override fun onOkayClick() {
        findNavController().navigateUp()
    }
}