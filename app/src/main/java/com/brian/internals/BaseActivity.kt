package com.brian.internals

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.widget.Toast
import com.brian.R


internal var progressDialog: Dialog? = null
internal var toast: Toast? = null


internal fun showProgress(context: Context) {
    progressDialog = Dialog(context)
    progressDialog?.let {
        it.setContentView(R.layout.layout_progress_dialog)
        it.setCancelable(false)
        it.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        it.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        it.show()
    }
}

internal fun hideProgress() {
    progressDialog?.cancel()
}



