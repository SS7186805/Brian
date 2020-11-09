package com.brian.models

import com.google.gson.annotations.SerializedName

data class ResponseGameSummary(

	@field:SerializedName("result")
	val result: String? = null,

	@field:SerializedName("data")
	val data: GameSummaryData? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("error")
	var error: String? = null
)
data class SubmittedAnswersItem(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("question")
	val question: GameSummaryQuestion? = null,

	@field:SerializedName("user_id")
	val userId: Int? = null,

	@field:SerializedName("selected_defensive_situation_id")
	val selectedDefensiveSituationId: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("question_id")
	val questionId: Int? = null,

	@field:SerializedName("answer_id")
	val answerId: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null
)

data class GameSummaryData(

	@field:SerializedName("score")
	val score: Int? = null,

	@field:SerializedName("average")
	val average: Int? = null,

	@field:SerializedName("submitted_answers")
	val submittedAnswers: List<SubmittedAnswersItem?>? = null,

	@field:SerializedName("correct_answer")
	val correctAnswer: Int? = null,

	@field:SerializedName("total_question")
	val totalQuestion: Int? = null,

	@field:SerializedName("incorrect_answer")
	val incorrectAnswer: Int? = null
)

data class GameSummaryQuestion(

	@field:SerializedName("defensive_situation_id")
	val defensiveSituationId: Int? = null,

	@field:SerializedName("question")
	val question: String? = null,

	@field:SerializedName("runners_on")
	val runnersOn: Int? = null,

	@field:SerializedName("updated_at")
	val updatedAt: Any? = null,

	@field:SerializedName("answers")
	val answers: List<AnswersItem?>? = null,

	@field:SerializedName("created_at")
	val createdAt: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("you_are")
	val youAre: Int? = null,

	@field:SerializedName("deleted_at")
	val deletedAt: Any? = null,

	@field:SerializedName("out")
	val out: Int? = null,

	@field:SerializedName("also_think_about_it")
	val alsoThinkAboutIt: String? = null
)
