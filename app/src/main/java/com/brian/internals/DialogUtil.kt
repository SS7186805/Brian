package com.brian.internals


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import com.brian.R
import kotlinx.android.synthetic.main.layout_custom_dialog.*

class DialogUtil() {

    var dialog: Dialog? = null

    private constructor(builder: Builder) : this() {
        if (dialog != null) {
            dialog?.dismiss()
        }
        dialog = Dialog(builder.context)
        when (builder.dialogType) {

            //dialog type success
            DialogType.SUCCESS -> {
                dialog?.setContentView(R.layout.layout_custom_dialog)
                dialog?.bOkay?.setOnClickListener {
                    dialog?.cancel()
                    builder.successClickListener?.onOkayClick()
                }
            }


        }
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog?.title?.text = builder.title
        dialog?.description?.text = builder.message
        dialog?.show()
    }

    companion object {
        inline fun build(context: Context, block: Builder.() -> Unit) =
            Builder(context).apply(block).build()
    }

    class Builder(val context: Context) {

        var title: String? = null
        var message: String? = null
        var dialogType: DialogType? = null
        var yesNoDialogClickListener: YesNoDialogClickListener? = null
        var successClickListener: SuccessClickListener? = null
        var errorClickListener: ErrorClickListener? = null
        var inputDialogListener: InputDialogListener? = null

        fun build() {
            when {
                title.isNullOrEmpty() -> {
                    throw IllegalArgumentException("Dialog title required.")
                }
                message.isNullOrEmpty() -> {
                    throw IllegalArgumentException("Dialog message required.")
                }
                dialogType == null -> {
                    throw IllegalArgumentException("Dialog type required.")
                }
                else -> {
                    if (dialogType == DialogType.SUCCESS && successClickListener == null) {
                        throw IllegalArgumentException("Dialog SuccessClickListener required.")
                    } else if (dialogType == DialogType.ERROR && errorClickListener == null) {
                        throw IllegalArgumentException("Dialog ErrorClickListener required.")
                    } else if (dialogType == DialogType.YES_NO_TYPE && yesNoDialogClickListener == null) {
                        throw IllegalArgumentException("Dialog YesNoClickListener required.")
                    } else if (dialogType == DialogType.INPUT_TYPE && inputDialogListener == null) {
                        throw IllegalArgumentException("Dialog inputDialogListener required.")
                    } else {
                        DialogUtil(this)
                    }
                }
            }
        }
    }

    enum class DialogType {
        ALERT,
        ERROR,
        SUCCESS,
        YES_NO_TYPE,
        INPUT_TYPE,
        INPUT_TYPE_DESTINATION,
        LOGOUT
    }

    interface InputDialogListener {
        fun fetchValue(string: String)
    }

    interface SuccessClickListener {
        fun onOkayClick()
    }

    interface ErrorClickListener {
        fun onOkayClick()
    }

    interface YesNoDialogClickListener {
        fun onClickYes()
        fun onClickNo()
    }
}






