package com.brian.internals

import android.content.Context
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.brian.R
import com.brian.base.MainApplication
import com.brian.databinding.ContactUsFragmentBinding
import com.brian.databinding.CreateChallengeFragmentBinding
import com.brian.databinding.CreateTeamFragmentBinding
import java.io.File

class Validator {
    var MEDIA_TYPE_VIDEO = "Video"


    internal object HOLDER {
        val instance = Validator()

    }

    companion object {
        val init: Validator by lazy { HOLDER.instance }
        var error: String? = ""
    }

    private fun isValidEmailId(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun validatePostMediaData(context: Context,path: String?): Boolean {


        val file = File(path)
        Log.e("Size","MB ${((file.length().toDouble()/1024)/1024) }")

        if (getVideoDuration(MainApplication.instance, file) > 60500) {
            Toast.makeText(
                context,
                context.getString(R.string.video_upload_duration_limit),
                Toast.LENGTH_SHORT
            ).show()
            return false
        }else if(((file.length().toDouble()/1024)/1024) > 50 ){
            Toast.makeText(
                context,
                context.getString(R.string.video_size),
                Toast.LENGTH_SHORT
            ).show()
            return false

        }

        return true
    }

    fun getVideoDuration(context: Context?, file: File?): Long {
        val retriever = MediaMetadataRetriever()
        //use one of overloaded setDataSource() functions to set your data source
        retriever.setDataSource(context, Uri.fromFile(file))
        val time =
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        val timeInMillisec = time.toLong()
        retriever.release()
        return timeInMillisec
    }


    fun validateCreateChallengeData(
        binding: CreateChallengeFragmentBinding,
        context: Context
    ): Boolean {
        if (binding.challengeTitle.text.isNullOrBlank()) {
            Toast.makeText(
                context,
                context.getString(R.string.challenge_title_empty_message),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.challengeTitle.text.length < 2) {
            Toast.makeText(
                context,
                context.getString(R.string.challenge_title_validation_message),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.selectChallengeType.text.isNullOrBlank()) {
            Toast.makeText(
                context,
                context.getString(R.string.challenge_type_empty),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.selectDate.text.isNullOrBlank()) {
            Toast.makeText(context, context.getString(R.string.date_empty), Toast.LENGTH_SHORT)
                .show()
            return false
        } else if (binding.selectUser.text.isNullOrBlank()) {
            Toast.makeText(
                context,
                context.getString(R.string.select_user_message),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else {
            return true
        }
        return true

    }

    fun validateContactUsData(binding: ContactUsFragmentBinding, context: Context): Boolean {
        if (binding.title.text.length < 2) {
            Toast.makeText(
                context,
                context.getString(R.string.challenge_title_empty),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.message.text.isNullOrBlank()) {
            Toast.makeText(
                context,
                context.getString(R.string.please_enter_message),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else {
            return true
        }
        return true

    }

    fun validateCreateTeam(binding: CreateTeamFragmentBinding, context: Context): Boolean {
        if (binding.etTeamName.text.length < 2) {
            Toast.makeText(
                context,
                context.getString(R.string.challenge_title_empty_team),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (binding.etSelectUser.text.isNullOrBlank()) {
            Toast.makeText(
                context,
                context.getString(R.string.select_user_message),
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else {
            return true
        }
        return true

    }
}