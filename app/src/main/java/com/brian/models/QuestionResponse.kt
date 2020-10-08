package com.brian.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class QuestionResponse(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("data")
	val data: QuestionData? = null,

	@field:SerializedName("error")
	var error: String? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class QuestionData(

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("runners_on")
	val runnersOn: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt:@RawValue Any? = null,

	@field:SerializedName("answers")
	val answers: List<AnswersItem>? = null,

	@field:SerializedName("created_at")
	val createdAt:@RawValue Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("you_are")
	val youAre: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt:@RawValue Any? = null,

	@field:SerializedName("out")
	val out: Int? = null,

	@field:SerializedName("also_think_about_it")
	val alsoThinkAboutIt: String? = null
) : Parcelable

@Parcelize
data class AnswersItem(

	@field:SerializedName("answer")
	val answer: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt:@RawValue Any? = null,

	@field:SerializedName("created_at")
	val createdAt:@RawValue Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("question_id")
	val questionId: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt:@RawValue Any? = null,

	@field:SerializedName("is_correct")
	val isCorrect: Int? = null

) : Parcelable
