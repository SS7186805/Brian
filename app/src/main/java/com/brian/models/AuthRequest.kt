package com.brian.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import okhttp3.MultipartBody

@Parcelize
data class AuthRequest(
    @field:SerializedName("name")
    var name: String ? = null,

    @field:SerializedName("email")
    var email: String? = null,

    @field:SerializedName("dob")
    var dob: String? = null,

    @field:SerializedName("user_type")
    var user_type: String? = null,

    @field:SerializedName("password")
    var password: String? = null,

    @field:SerializedName("cnf_password")
    var cnf_password: String? = null,

    @field:SerializedName("device_type")
    var deviceType: String? = "Android",

    @field:SerializedName("device_token")
     var deviceToken: String? = null
): Parcelable