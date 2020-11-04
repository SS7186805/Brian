package com.brian.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.brian.R
import com.brian.base.ScopedFragment
import com.brian.databinding.UserProfileFragmentBinding
import com.brian.internals.Utils
import com.brian.models.DataBadges
import com.brian.models.LoginData
import com.brian.viewModels.myProfile.MyProfileViewModel
import com.brian.viewModels.myProfile.MyProfileViewModelFactory
import com.brian.views.adapters.BadgesAdapter
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class UserProfileFragment : ScopedFragment(), KodeinAware {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: MyProfileViewModelFactory by instance()
    lateinit var mBinding: UserProfileFragmentBinding
    lateinit var mViewModel: MyProfileViewModel
    var list = ArrayList<String>()
    var badgesAdapter: BadgesAdapter? = null
    var userData = LoginData()
    var id = ""


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

        setObserver()


        return mBinding.root
    }


    private fun setObserver() {
        mViewModel.loginData.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                setData(it)

            }
        })
    }

    private fun setData(login: LoginData) {

        mBinding.type.text = login.userType
        mBinding.dob.text =
            "Born on: ${Utils.init.getUserProfileFormattedDate(login.dob.toString())}"
        mBinding.username.text = login.name

        if (login?.profilePicture == null) {
            Glide.with(requireContext()).load(R.drawable.ic_use_r).into(mBinding.profilePic)
        } else {
            Glide.with(requireContext()).load(login.profilePicture).into(mBinding.profilePic)
        }


        if (login.badgesEarneda?.data?.isNotEmpty()!!) {
            mBinding.tvNobadges.visibility = View.GONE
            setBadgesAdapter(login.badgesEarneda?.data!!)

        } else {
            mBinding.tvNobadges.visibility = View.VISIBLE
        }


    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(MyProfileViewModel::class.java)
    }


    fun setBadgesAdapter(list: ArrayList<DataBadges>) {
        mBinding.recycler.layoutManager = GridLayoutManager(requireContext(), 4)
        badgesAdapter = BadgesAdapter(R.layout.badge_item)
        badgesAdapter!!.setNewItems(list)
        mBinding.recycler.adapter = badgesAdapter

    }

    override fun onResume() {
        super.onResume()
        id = arguments?.getString(getString(R.string.user_id))!!
        mViewModel.viewProfile(id)

    }

    inner class ClickHandler {


    }
}