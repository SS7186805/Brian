package com.brian.internals

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.text.InputFilter
import android.text.Spanned
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.BindingAdapter
import com.brian.R
import com.brian.models.LoginData
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class Utils private constructor() {

    private var datePickerDialog: DatePickerDialog? = null

    private object HOLDER {
        val INSTANCE = Utils()
    }

    companion object {
        val init: Utils by lazy { HOLDER.INSTANCE }

        @JvmStatic
        @BindingAdapter("bind:imageUrl")
        fun setImage(image: ImageView, url: String?) {
            if (!url.isNullOrEmpty()) {
                Glide.with(image.context).load(url).centerCrop()
                    .into(image)
            }
        }
    }

    fun toTextRequestBody(value: String): RequestBody {
        return value.toRequestBody("text/plain".toMediaTypeOrNull())
    }

    fun toFileRequestBody(file: File): RequestBody {
        return file.asRequestBody("*/*".toMediaTypeOrNull())
    }

    fun setFullWidth(inputLayout: TextInputLayout) {
        val errorTextView = inputLayout.findViewById<TextView>(R.id.textinput_error)
        var params = errorTextView?.layoutParams
        params?.width = 20
        params?.height = 20
        errorTextView?.layoutParams = params
    }

    fun getAge(dobString: String): Int {
        try {
            var date: Date? = null
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            try {
                date = sdf.parse(dobString)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            if (date == null) return 0
            val dob = Calendar.getInstance()
            val today = Calendar.getInstance()
            dob.time = date
            val year = dob[Calendar.YEAR]
            val month = dob[Calendar.MONTH]
            val day = dob[Calendar.DAY_OF_MONTH]
            return getUserAge(year, month, day)
        } catch (e: Exception) {
            return 0
        }
    }

    private fun getUserAge(year: Int, month: Int, day: Int): Int {
        val dob = Calendar.getInstance()
        val today = Calendar.getInstance()
        dob[year, month] = day
        var age = today[Calendar.YEAR] - dob[Calendar.YEAR]
        if (today[Calendar.DAY_OF_YEAR] < dob[Calendar.DAY_OF_YEAR]) {
            age--
        }
        val ageInt = age
        return ageInt
    }

    fun setupFullScreen(activity: Activity) {
        activity.window.apply {
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
    }

    fun hideKeyBoard(context: Context, view: View) {
        val inputManager: InputMethodManager? =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        inputManager?.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }

    fun readJSONFromAsset(context: Context, path: String): String? {
        val json: String
        try {
            val inputStream: InputStream = context.assets.open(path)
            json = inputStream.bufferedReader().use { it.readText() }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return null
        }
        return json
    }

    fun getFilter(limit: Int, spaceEnable: Boolean): Array<InputFilter> {
        val inputFilter = InputFilter(fun(
            source: CharSequence,
            start: Int,
            end: Int,
            _: Spanned,
            _: Int,
            _: Int
        ): String? {
            for (index in start until end) {
                val type = Character.getType(source[index])
                if (spaceEnable) {
                    if (type == Character.SURROGATE.toInt() || type == Character.NON_SPACING_MARK.toInt()) {
                        return ""
                    }
                } else {
                    if (type == Character.SURROGATE.toInt() || type == Character.NON_SPACING_MARK.toInt() || type == Character.OTHER_SYMBOL.toInt() || Character.isWhitespace(
                            source[index]
                        )
                    ) {
                        return ""
                    }
                }
            }
            return null
        })
        return arrayOf(inputFilter, InputFilter.LengthFilter(limit))
    }

    fun selectDate(
        context: Context?,
        startFrom: String,
        editText: TextView,
        disableFutureDate: Boolean
    ) {
        datePickerDialog?.let {
            if (it.isShowing) {
                return
            }
        }
        val calendar = Calendar.getInstance()
        val dateFormats = listOf("yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy")
        var date: Date? = null
        var startDate: Date? = null
        for (dateFormat in dateFormats) {
            try {
                startDate = SimpleDateFormat(dateFormat).parse(startFrom)
                date = SimpleDateFormat(dateFormat).parse(editText.text.toString())
            } catch (e: java.lang.Exception) {
                Log.d("Exception", "getFormattedDate: unable to parse date.")
            }
        }
        if (date != null) {
            calendar.time = date
        }
        val mYear = calendar[Calendar.YEAR]
        val mMonth = calendar[Calendar.MONTH]
        val mDay = calendar[Calendar.DAY_OF_MONTH]
        datePickerDialog = DatePickerDialog(
            context!!,
            OnDateSetListener { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                editText.text =
                    getFormattedDate(year.toString() + "-" + (month + 1) + "-" + dayOfMonth)
            },
            mYear,
            mMonth,
            mDay
        )
        if (disableFutureDate) {
            val dob = Calendar.getInstance()
            dob.set(Calendar.YEAR, 2008)
            datePickerDialog?.datePicker?.maxDate = dob.time.time
        } else {
            startDate?.let {
                datePickerDialog?.datePicker?.minDate = it.time
            }
        }
        datePickerDialog?.show()
    }

    fun getFormattedDate(time: String): String {
        val dateFormats = listOf("yyyy-MM-dd", "yyyy/MM/dd")
        var currentTime = ""
        var date: Date? = null
        for (dateFormat in dateFormats) {
            try {
                date = SimpleDateFormat(dateFormat).parse(time)
            } catch (e: java.lang.Exception) {
                Log.d("Exception", "getFormattedDate: unable to parse date.")
            }
        }
        if (date != null) {
            currentTime = SimpleDateFormat("yyyy-MM-dd").format(date)
        }
        return currentTime
    }

    fun getRatingFormattedDate(time: String): String {
        val dateFormats = listOf("yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy")
        var currentTime = ""
        var date: Date? = null
        for (dateFormat in dateFormats) {
            try {
                date = SimpleDateFormat(dateFormat).parse(time)
            } catch (e: java.lang.Exception) {
                Log.d("Exception", "getFormattedDate: unable to parse date.")
            }
        }
        if (date != null) {
            currentTime = SimpleDateFormat("dd MMMM yyyy").format(date)
        }
        return currentTime
    }

    fun getCurrentDate(): String {
        val df = SimpleDateFormat("dd/MM/yyyy")
        //val df = SimpleDateFormat("yyyy-MM-dd")
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = Calendar.getInstance().timeInMillis
        return df.format(calendar.time)
    }

    fun getDate(dateString: String): Date {
        val dateFormats = listOf("yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy")
        val calendar = Calendar.getInstance()
        var date: Date? = null
        for (dateFormat in dateFormats) {
            try {
                date = SimpleDateFormat(dateFormat).parse(dateString)
            } catch (e: java.lang.Exception) {
                Log.d("Exception", "getFormattedDate: unable to parse date.")
            }
        }
        date?.let {
            calendar.time = date
        }
        return calendar.time
    }

    fun getApiFormatDate(dateString: String): String {
        val dateFormats = listOf("yyyy-MM-dd", "yyyy/MM/dd", "dd/MM/yyyy")
        var currentTime = ""
        var date: Date? = null
        for (dateFormat in dateFormats) {
            try {
                date = SimpleDateFormat(dateFormat).parse(dateString)
            } catch (e: java.lang.Exception) {
                Log.d("Exception", "getFormattedDate: unable to parse date.")
            }
        }
        if (date != null) {
            currentTime = SimpleDateFormat("yyyy-MM-dd").format(date)
        }
        return currentTime
    }
}

fun String.convertJSON(className: LoginData): LoginData {
    return Gson().fromJson<LoginData>(this, LoginData::class.java)
}

fun <T> List<T>.toArrayList(): ArrayList<T> {
    val temp = ArrayList<T>()
    temp.addAll(this)
    return temp
}

fun ScrollView.scrollToEditText(et: View) {
    smoothScrollTo(et.left, et.top)
}

fun Context.showToast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Activity.keyboardListener(keyboard: (isOpen: Boolean) -> Unit) {
    KeyboardVisibilityEvent.setEventListener(
        this,
        object : KeyboardVisibilityEventListener {
            override fun onVisibilityChanged(isOpen: Boolean) {
                keyboard(isOpen)
            }
        })
}

fun isValide(email:String):Boolean{
    return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}