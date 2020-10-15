package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.FragmentLoginBinding
import com.brian.databinding.PitcherFragmentBinding
import com.brian.databinding.TrainingVideosBinding
import com.brian.models.DataItem
import com.brian.viewModels.homescreen.HomeViewModel
import com.brian.viewModels.homescreen.HomescreenViewModelFactory
import kotlinx.android.synthetic.main.pitcher_fragment.*
import kotlinx.android.synthetic.main.toolbar_layout.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class PitcherFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: HomescreenViewModelFactory by instance()
    lateinit var mBinding: PitcherFragmentBinding
    lateinit var mViewModel: HomeViewModel


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

    private fun SetUpScreen(name:String){

        when(name){
            "Pitcher" ->  mBinding.toolbar.tvTitle.text = "Pitcher"
            "Catcher" ->  mBinding.toolbar.tvTitle.text = "Catcher"
            "FirstBase" ->  mBinding.toolbar.tvTitle.text = "FirstBase"
            "SecondBase" ->  mBinding.toolbar.tvTitle.text = "SecondBase"
            "ThirdBase" ->  mBinding.toolbar.tvTitle.text = "ThirdBase"
            "ShortStep" ->  mBinding.toolbar.tvTitle.text = "ShortStep"
            "LeftField" ->  mBinding.toolbar.tvTitle.text = "LeftField"
            "CenterField" ->  mBinding.toolbar.tvTitle.text = "CenterField"
            "RightField" ->  mBinding.toolbar.tvTitle.text = "RightField"

            else->""

        }
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    inner class ClickHandler {

        fun onPitchClick() {
            findNavController().navigate(R.id.questionsFragment)

        }

    }
}