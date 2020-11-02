package com.brian.internals

import android.content.Context
import android.util.Patterns
import android.widget.Toast
import com.brian.R
import com.brian.databinding.ContactUsFragmentBinding
import com.brian.databinding.CreateChallengeFragmentBinding
import com.brian.databinding.CreateTeamFragmentBinding

class Validator {

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

    fun validateCreateChallengeData(binding: CreateChallengeFragmentBinding, context: Context): Boolean {
        if (binding.challengeTitle.text.isNullOrBlank()) {
            Toast.makeText(context, context.getString(R.string.challenge_title_empty), Toast.LENGTH_SHORT).show()
            return false
        }  else if (binding.selectChallengeType.text.isNullOrBlank()) {
            Toast.makeText(context, context.getString(R.string.challenge_type_empty), Toast.LENGTH_SHORT).show()
            return false
        } else if (binding.selectDate.text.isNullOrBlank()) {
            Toast.makeText(context, context.getString(R.string.date_empty), Toast.LENGTH_SHORT).show()
            return false
        }
        else if (binding.selectUser.text.isNullOrBlank()) {
            Toast.makeText(context, context.getString(R.string.select_user_message), Toast.LENGTH_SHORT).show()
            return false
        }else {
            return true
        }
        return true

    }

    fun validateContactUsData(binding: ContactUsFragmentBinding, context: Context): Boolean {
        if (binding.title.text.isNullOrBlank()) {
            Toast.makeText(context, context.getString(R.string.challenge_title_empty), Toast.LENGTH_SHORT).show()
            return false
        }  else if (binding.message.text.isNullOrBlank()) {
            Toast.makeText(context, context.getString(R.string.please_enter_message), Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
        return true

    }

    fun validateCreateTeam(binding: CreateTeamFragmentBinding, context: Context): Boolean {
        if (binding.etTeamName.text.isNullOrBlank()) {
            Toast.makeText(context, context.getString(R.string.challenge_title_empty), Toast.LENGTH_SHORT).show()
            return false
        }  else if (binding.etSelectUser.text.isNullOrBlank()) {
            Toast.makeText(context, context.getString(R.string.select_user_message), Toast.LENGTH_SHORT).show()
            return false
        } else {
            return true
        }
        return true

    }
}