package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.FragmentLoginBinding
import com.brian.databinding.PitcherFragmentBinding
import com.brian.databinding.TrainingVideosBinding
import com.brian.internals.ClickGuard
import com.brian.models.DataItem
import com.brian.viewModels.homescreen.HomeViewModel
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.pitcher_fragment.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class PitcherFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: HomescreenViewModelFactory by instance()
    lateinit var mBinding: PitcherFragmentBinding
    lateinit var mViewModel: HomeViewModel
    var nextName = ""


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = PitcherFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
//        mBinding.toolbar.tvTitle.text = getString(R.string.situation_name)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        setupClickListeners()

        return mBinding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var list = arguments?.getParcelableArrayList<DataItem>("list")
        var name = arguments?.getString("name")

        SetUpScreen(name!!)

        for (item in list!!) {
            if (item.situationName.equals("Pitcher")) {
                tv_data.text = item.description
                break
            }
        }
    }

    private fun SetUpScreen(name: String) {
        nextName = name
        when (name) {
            "Pitcher" -> mBinding.toolbar.tvTitle.text = "Pitcher"
            "Catcher" -> mBinding.toolbar.tvTitle.text = "Catcher"
            "First Base" -> mBinding.toolbar.tvTitle.text = "First Base"
            "Second Base" -> mBinding.toolbar.tvTitle.text = "Second Base"
            "Third Base" -> mBinding.toolbar.tvTitle.text = "Third Base"
            "Short Step" -> mBinding.toolbar.tvTitle.text = "Short Step"
            "Left Field" -> mBinding.toolbar.tvTitle.text = "Left Field"
            "Center Field" -> mBinding.toolbar.tvTitle.text = "Center Field"
            "Right Field" -> mBinding.toolbar.tvTitle.text = "Right Field"

            else -> ""

        }
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    private fun setupClickListeners(){

        mBinding.apply {
            pitcher.setOnClickListener{ clickHandler!!.onPitchClick()}

            ClickGuard.guard(pitcher)
        }


    }

    inner class ClickHandler {

        fun onPitchClick() {
            findNavController().navigate(R.id.questionsFragment, bundleOf("name" to nextName))

        }

    }
}