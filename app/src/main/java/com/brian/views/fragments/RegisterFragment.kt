package com.brian.views.fragments

import android.Manifest.permission.*
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.text.method.Touch
import android.util.Base64
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ScrollView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.findNavController
import com.brian.R
import com.brian.base.Prefs
import com.brian.base.ScopedFragment
import com.brian.databinding.FragmentRegisterBinding
import com.brian.internals.*
import com.brian.models.LoginData
import com.brian.viewModels.register.RegisterViewModel
import com.brian.viewModels.register.RegisterViewModelFactory
import com.brian.views.activities.AccountHandlerActivity
import com.brian.views.activities.HomeActivity
import com.bumptech.glide.Glide
import com.livinglifetechway.quickpermissions_kotlin.runWithPermissions
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_register.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.io.ByteArrayOutputStream
import java.io.File

class RegisterFragment : ScopedFragment(), KodeinAware, DialogUtil.SuccessClickListener,OnTouchListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: FragmentRegisterBinding
    lateinit var mViewModel: RegisterViewModel

    private var datePickerDialog: DatePickerDialog? = null
    var image_path: String? = null

    var list = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setupViewModel()
        setupObserver()
        mBinding = FragmentRegisterBinding.inflate(inflater, container, false).apply {
            viewModel = mViewModel
            clickHandler = ClickHandler()
        }


        mBinding.toolbar.tvTitle.text = getString(R.string.register)
        mBinding.toolbar.ivBack.setOnClickListener {
            findNavController().navigateUp()
        }

        list.add("Players")
        list.add("Coaches")
        list.add("Trainers")
        list.add("Scouts")
        list.add("Managers")
        list.add("Parents")


        val arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, list)
        mBinding.apply {
            regUserType.setAdapter(arrayAdapter)
            regUserType.setInputType(0)
            regUserType.setOnClickListener {
                regUserType.showDropDown()
            }
            regUserType.setOnFocusChangeListener { v, hasFocus ->
                regUserType.showDropDown()
            }

            regPassword.setOnFocusChangeListener { v, hasFocus ->
               if(hasFocus) registerScroll.scrollToEditText(v)
            }

            regCnfPassword.setOnFocusChangeListener { v, hasFocus ->
                if(hasFocus) registerScroll.scrollToEditText(v)
            }
        }


        mBinding.regUserType.setOnTouchListener(this)

        keyboardListener()
        setupClickListeners()
      //  mBinding.regCnfPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);

        return mBinding.root
    }

    private fun setupClickListeners() {
        mBinding.apply {
            registerButton.setOnClickListener { mViewModel.onSignUpClick() }
            ClickGuard.guard(registerButton)
        }
    }

    private fun setupObserver() {
        mViewModel.apply {
            showLoading.observe(viewLifecycleOwner, Observer {
                if (it){
                    mBinding.progressBar.visibility = VISIBLE
                }else{
                    mBinding.progressBar.visibility = GONE
                }
            })

            showMessage.observe(viewLifecycleOwner, Observer {

                if (!TextUtils.isEmpty(it))
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            })

            registerSuccess.observe(viewLifecycleOwner, Observer {
                if (it) {
                    DialogUtil.build(requireContext()) {
                        title = getString(R.string.success)
                        dialogType = DialogUtil.DialogType.SUCCESS
                        message = getString(R.string.register_message)
                        successClickListener = this@RegisterFragment
                    }
                }
            })
        }
    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
        mViewModel.showMessage.postValue("")
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Add Photo!")
        builder.setItems(options, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, item: Int) {
                if (options[item] == "Take Photo") {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, 1)
                } else if (options[item] == "Choose from Gallery") {
                    val intent =
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, 2)
                } else if (options[item] == "Cancel") {
                    dialog.dismiss()
                }
            }
        })
        builder.show()
    }

    fun base64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
        return encoded
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                val photo = data?.getExtras()?.get("data") as Bitmap
//                var imagestr = base64(photo)
                val uri_image = getImageUri(photo)
                image_path = uri_image?.let { getPath(it) }
                val file = File(image_path)
                val filePart = MultipartBody.Part.createFormData(
                    "profile_picture",
                    file.name,
                    RequestBody.create("image/*".toMediaTypeOrNull(), file)
                )
                mViewModel.authRequest.get()?.profile_picture = filePart
                circler_image.setImageBitmap(photo)
            } else if (requestCode == 2) {
                val SelectedImage = data?.data as Uri
                image_path = SelectedImage?.let { getPath(it) }
                val file = File(image_path)
                val filePart = MultipartBody.Part.createFormData(
                    "profile_picture",
                    file.name,
                    RequestBody.create("image/*".toMediaTypeOrNull(), file)
                )
                mViewModel.authRequest.get()?.profile_picture = filePart
                circler_image.setImageURI(SelectedImage)
            }
        }
    }

    fun getImageUri(inImage: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path: String =
            MediaStore.Images.Media.insertImage(
                requireContext().contentResolver,
                inImage,
                "Title",
                null
            )
        return Uri.parse(path)
    }

    private fun getPath(contentUri: Uri): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(
            requireContext().getApplicationContext(),
            contentUri,
            proj,
            null,
            null,
            null
        )
        val cursor: Cursor? = loader.loadInBackground()
        val column_index = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor?.moveToFirst()
        val result = column_index?.let { cursor.getString(it) }
        cursor?.close()
        return result
    }

    inner class ClickHandler {

        fun onclickAddImage() {

            runWithPermissions(CAMERA, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE) {
                selectImage()
            }
        }

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
                mBinding.regDOB.text.toString(),
                mBinding.regDOB,
                true
            )
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (arguments?.getString("edit").equals("edit")) {
            mViewModel.isediting = true
            mBinding.regPassword.visibility = GONE
            mBinding.regCnfPassword.visibility = GONE
            mBinding.registerButton.setText(getString(R.string.update))
            mBinding.toolbar.tvTitle.setText(getString(R.string.editProfile))
            mBinding.regName.setText(getString(R.string.user_name))
            mBinding.regEmail.setText("abc@gmail.com")
            mBinding.regDOB.setText(Utils.init.getCurrentDate())
            mBinding.regDOB.isEnabled = false
            mBinding.regUserType.isEnabled = false


            val login: LoginData? = Prefs.init().userInfo
            mBinding.regName.setText(login?.name)

            mViewModel.authRequest.get()?.apply {
                dob = login?.dob
                user_type = login?.userType
                name = login?.name
                email = login?.email
                Glide.with(requireContext()).load(login?.profilePicture).into( mBinding.circlerImage)
            }

        }
    }
    fun keyboardListener() {
        requireActivity().keyboardListener { isOpen ->
            if (!isOpen) {
//                mBinding.regName.requestFocus()
//                mBinding.regName.requestFocusFromTouch()
//                mBinding.regEmail.requestFocus()
//                mBinding.regEmail.requestFocusFromTouch()
//                mBinding.regPassword.requestFocus()
//                mBinding.regPassword.requestFocusFromTouch()
//                mBinding.regCnfPassword.requestFocus()
//                mBinding.regCnfPassword.requestFocusFromTouch()
//                mBinding.circlerImage.requestFocus()
//                mBinding.circlerImage.requestFocusFromTouch()
            }
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

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if(v?.id==R.id.reg_User_type){
            Utils.init.hideKeyBoard(requireContext(),mBinding.root)

        }
        return false
    }
}