package com.brian.views.fragments

import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
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
import kotlinx.android.synthetic.main.fragment_register.*
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RegisterFragment : ScopedFragment(), KodeinAware, DialogUtil.SuccessClickListener {
    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: RegisterViewModelFactory by instance()
    lateinit var mBinding: FragmentRegisterBinding
    lateinit var mViewModel: RegisterViewModel
    private var datePickerDialog: DatePickerDialog? = null

    private val PERMISSION_REQUEST_CODE: Int = 101
    private var mCurrentPhotoPath: String? = null
    val REQUEST_IMAGE_CAPTURE = 1

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

        setupObserver()
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

    private fun setupObserver() {
        mViewModel.apply {
            showLoading.observe(viewLifecycleOwner, Observer {
                if (it)
                    progress_bar.visibility = View.VISIBLE
            })

            showMessage.observe(viewLifecycleOwner, Observer {
                progress_bar.visibility = View.GONE
                if (!TextUtils.isEmpty(it))
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            })

            registerSuccess.observe(viewLifecycleOwner, Observer {
                if (it) {
                    DialogUtil.build(requireContext()) {
                        title = getString(R.string.success)
                        dialogType = DialogUtil.DialogType.SUCCESS
                        message = "success"
                        successClickListener = this@RegisterFragment
                    }
                }
            })
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {

                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {

                   selectImage()

                } else {
                    Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
                return
            }

            else -> {

            }
        }
    }
//    private fun takePicture() {
//
//        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//        val file: File = createFile()
//
//        val uri: Uri = FileProvider.getUriForFile(
//            requireContext(),
//            "com.example.android.fileprovider",
//            file
//        )
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
//        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
//    }


    private fun checkPersmission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.CAMERA
        ) ==
                PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(READ_EXTERNAL_STORAGE, CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }

//    @Throws(IOException::class)
//    private fun createFile(): File {
//        // Create an image file name
//        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
//        val storageDir: File? = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
//        return File.createTempFile(
//            "JPEG_${timeStamp}_", /* prefix */
//            ".jpg", /* suffix */
//            storageDir /* directory */
//        ).apply {
//            // Save a file: path for use with ACTION_VIEW intents
//            mCurrentPhotoPath = absolutePath
//        }
//    }

    private fun setupViewModel() {
        mViewModel =
            ViewModelProvider(this, viewModelFactory).get(RegisterViewModel::class.java)
    }

    private fun selectImage() {
        val options = arrayOf<CharSequence>("Take Photo", "Choose from Gallery", "Cancel")
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle("Add Photo!")
        builder.setItems(options, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface, item: Int) {
                if (options[item] == "Take Photo") {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    val f = File(Environment.getExternalStorageDirectory(), "temp.jpg")
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f))
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                var f = File(Environment.getExternalStorageDirectory().toString())
                for (temp in f.listFiles()) {
                    if (temp.name == "temp.jpg") {
                        f = temp
                        break
                    }
                }
                try {
                    val bitmap: Bitmap
                    val bitmapOptions = BitmapFactory.Options()
                    bitmap = BitmapFactory.decodeFile(
                        f.absolutePath,
                        bitmapOptions
                    )
                    circler_image.setImageBitmap(bitmap)
                    val path = (Environment
                        .getExternalStorageDirectory()
                        .toString() + File.separator
                            + "Phoenix" + File.separator + "default")
                    f.delete()
                    var outFile: OutputStream? = null
                    val file = File(path, System.currentTimeMillis().toString() + ".jpg")
                    try {
                        outFile = FileOutputStream(file)
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile)
                        outFile.flush()
                        outFile.close()
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (requestCode == 2) {
                val selectedImage = data?.data
                val filePath = arrayOf(MediaStore.Images.Media.DATA)
                val c: Cursor? =
                    selectedImage?.let { context?.getContentResolver()?.query(
                        it,
                        filePath,
                        null,
                        null,
                        null
                    ) }
                c?.moveToFirst()

                val columnIndex: Int = c!!.getColumnIndex(filePath[0])
                val picturePath: String = c!!.getString(columnIndex)
                c.close()
                val thumbnail = BitmapFactory.decodeFile(picturePath)
                Log.w(
                    "path of image from gallery......******************.........",
                    picturePath + ""
                )
                circler_image.setImageBitmap(thumbnail)
            }
        }
    }

    inner class ClickHandler {

        fun onclickAddImage(){
          if (checkPersmission()) selectImage() else requestPermission()
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