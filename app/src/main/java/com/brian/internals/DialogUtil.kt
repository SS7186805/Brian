package com.brian.internals


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.InsetDrawable
import android.view.ViewGroup
import com.brian.R
import kotlinx.android.synthetic.main.layout_custom_dialog.*
import kotlinx.android.synthetic.main.layout_custom_dialog.description
import kotlinx.android.synthetic.main.layout_custom_dialog.title
import kotlinx.android.synthetic.main.layout_custom_dialog_logout.*


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
            DialogType.LOGOUT -> {
                dialog?.setContentView(R.layout.layout_custom_dialog_logout)
                dialog?.btYes?.setOnClickListener {
                    dialog?.cancel()
                    builder.yesNoDialogClickListener?.onClickYes()
                }
                dialog?.btNo?.setOnClickListener {
                    dialog?.cancel()
                    builder.yesNoDialogClickListener?.onClickNo()
                }
            }
            DialogType.YES_NO_TYPE -> {
                dialog?.setContentView(R.layout.layout_custom_error)
                dialog?.btYes?.setOnClickListener {
                    dialog?.cancel()
                    builder.yesNoDialogClickListener?.onClickYes()
                }
                dialog?.btNo?.setOnClickListener {
                    dialog?.cancel()
                    builder.yesNoDialogClickListener?.onClickNo()
                }
            }
            DialogType.ERROR -> {
                dialog?.setContentView(R.layout.layout_custom_particular_error)
                dialog?.bOkay?.setOnClickListener {
                    dialog?.cancel()
                    builder.errorClickListener?.onOkayClick()
                }
            }


        }

        val back = ColorDrawable(Color.TRANSPARENT)
        val inset = InsetDrawable(back, 150)
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.window!!.setBackgroundDrawable(inset)
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






