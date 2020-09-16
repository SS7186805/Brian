package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.FragmentLoginBinding
import com.brian.databinding.GameSummaryFragmentBinding
import com.brian.databinding.PitcherFragmentBinding
import com.brian.databinding.TrainingVideosBinding
import com.brian.viewModels.login.LoginViewModel
import com.brian.viewModels.login.LoginViewModelFactory
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class GameSummaryFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: GameSummaryFragmentBinding
    lateinit var mViewModel: RegisterViewModel



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

        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    inner class ClickHandler{

        fun onPitchClick(){

        }

    }
}