package com.brian.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import okhttp3.MultipartBody

@Parcelize
data class ChangePassword(
    @field:SerializedName("old_password")
    var old_password: String ? = null,

    @field:SerializedName("new_password")
    var new_password: String? = null,

    @field:SerializedName("confirm_password")
    var confirm_password: String? = null
): Parcelable