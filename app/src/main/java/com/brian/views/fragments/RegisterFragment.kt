package com.brian.views.fragments

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.Prefs
import com.brian.base.ScopedFragment
import com.brian.databinding.FragmentRegisterBinding
import com.brian.internals.DialogUtil
import com.brian.internals.Utils
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.activities.AccountHandlerActivity
import com.brian.views.activities.HomeActivity
import kotlinx.android.synthetic.main.activity_main.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class RegisterFragment : ScopedFragment(), KodeinAware, DialogUtil.SuccessClickListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: FragmentRegisterBinding
    lateinit var mViewModel: RegisterViewModel
    private var datePickerDialog: DatePickerDialog? = null
    var list = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setupViewModel()
        mBinding = FragmentRegisterBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }

        mBinding.toolbar.tvTitle.text = getString(R.string.register)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        list.add("Male")
        list.add("Female")

        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, list)
        mBinding.regUserType.setAdapter(arrayAdapter)
        mBinding.regUserType.setInputType(0)




        mBinding.regUserType.setOnClickListener {
            mBinding.regUserType.showDropDown()

        }


        mBinding.regUserType.setOnFocusChangeListener { v, hasFocus ->
            mBinding.regUserType.showDropDown()
        }
        return mBinding.root
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    inner class ClickHandler {

        fun onRegisterClick() {
            var meaasge = getString(R.string.register_message)

            if (arguments?.getString("edit").equals("edit")) {
                meaasge = getString(R.string.update_profile_message)
            }
            DialogUtil.build(requireContext()) {
                title = getString(R.string.success)
                dialogType = DialogUtil.DialogType.SUCCESS
                message = meaasge
                successClickListener = this@RegisterFragment
            }
        }

        fun selectDate() {
            hideKeyboard(requireView())
            Utils.init.selectDate(
                requireContext(),
                Utils.init.getCurrentDate(),
                mBinding.regDOB,
                false
            )
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments?.getString("edit").equals("edit")) {

            mBinding.regPassword.visibility = GONE
            mBinding.regCnfPassword.visibility = GONE
            mBinding.registerButton.setText(getString(R.string.update))
            mBinding.toolbar.tvTitle.setText(getString(R.string.editProfile))
            mBinding.regName.setText(getString(R.string.user_name))
            mBinding.regEmail.setText("abc@gmail.com")
            mBinding.regDOB.setText(Utils.init.getCurrentDate())

        }
    }

    override fun onOkayClick() {


        if (arguments?.getString("edit").equals("edit")) {
            findNavController().navigateUp()

        } else {
            Prefs.init().isLogIn = "true"
            startActivity(Intent(requireContext(), HomeActivity::class.java))
            (requireActivity() as AccountHandlerActivity).finish()

        }

    }


}