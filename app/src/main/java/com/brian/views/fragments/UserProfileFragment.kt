package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.brian.R
import com.brian.base.Prefs
import com.brian.base.ScopedFragment
import com.brian.databinding.*
import com.brian.internals.Utils
import com.brian.models.LoginData
import com.brian.viewModels.login.LoginViewModel
import com.brian.viewModels.login.LoginViewModelFactory
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.adapters.BadgesAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import kotlin.math.log

class UserProfileFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: UserProfileFragmentBinding
    lateinit var mViewModel: RegisterViewModel
    var list = ArrayList<String>()
    var badgesAdapter: BadgesAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = UserProfileFragmentBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }
        mBinding.toolbar.tvTitle.text = getString(R.string.user_profile)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }
        setAdapter()
        val login: LoginData = Prefs.init().userInfo!!
        mBinding.type.text = login.userType

        mBinding.dob.text = "Born on: ${login.dob}"
        mBinding.username.text = login?.name

        if (login?.profilePicture == null) {
            Glide.with(requireContext()).load(R.drawable.ic_use_r).into(mBinding.profilePic)
        } else {
            Glide.with(requireContext()).load(login.profilePicture).into(mBinding.profilePic)
        }

        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }


    fun setAdapter() {
        mBinding.recycler.layoutManager = GridLayoutManager(requireContext(), 4)
        badgesAdapter = BadgesAdapter(R.layout.badge_item)
        mBinding.recycler.adapter = badgesAdapter
        for (i in 0 until 8) {
            list.add("")
        }
        badgesAdapter!!.addNewItems(list)

    }

    inner class ClickHandler {


    }
}