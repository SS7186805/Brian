package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.FragmentLoginBinding
import com.brian.databinding.GameSummaryFragmentBinding
import com.brian.databinding.PitcherFragmentBinding
import com.brian.databinding.TrainingVideosBinding
import com.brian.models.AnswersItem
import com.brian.models.QuestionData
import com.brian.viewModels.login.LoginViewModel
import com.brian.viewModels.login.LoginViewModelFactory
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.adapters.GameSummaryAdapter
import com.brian.views.adapters.QuestionAdapter
import com.brian.views.adapters.QuestionSummaryAdapter
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.game_summary_fragment.*
import kotlinx.android.synthetic.main.question_item.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class GameSummaryFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: GameSummaryFragmentBinding
    lateinit var mViewModel: RegisterViewModel
    lateinit var gameSummaryAdapter: GameSummaryAdapter

    var correctAnswer=0
    var wrongeAnswer=0
    var gameSummarylist=ArrayList<QuestionData>()

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
        mBinding.toolbar.tvTitle.text=getString(R.string.game_summary)
        mBinding.toolbar.ivBack.visibility=GONE
        mBinding.totalQuestionsCount.text="5"

        setupRecycler()
        return mBinding.root
    }

    private fun setupRecycler() {
        mBinding.recyclerGameSummary.layoutManager = LinearLayoutManager(context)
        gameSummaryAdapter = GameSummaryAdapter(R.layout.question_item)
        mBinding.recyclerGameSummary.adapter = gameSummaryAdapter
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        correctAnswer= arguments?.getInt("correctAnswer")!!
        wrongeAnswer=arguments?.getInt("wrongeAnswer")!!
        gameSummarylist=arguments?.getParcelableArrayList<QuestionData>("questionSammary")!!
        println(gameSummarylist)
        gameSummaryAdapter.setNewItems(gameSummarylist)
        mBinding.correctAnswersCount.text=correctAnswer.toString()
        mBinding.incorrectAnswersCount.text=wrongeAnswer.toString()
    }

    inner class ClickHandler{

        fun onPitchClick(){
            findNavController().navigate(R.id.homeFragment)
        }

    }
}