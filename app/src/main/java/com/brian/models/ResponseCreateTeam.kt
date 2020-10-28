package com.brian.models

import com.google.gson.annotations.SerializedName

data class ResponseCreateTeam(

    @field:SerializedName("result")
    val result: String? = null,

    @field:SerializedName("data")
    val data: CreateTeamData? = null,

    @field:SerializedName("message")
    val message: String? = null
)

data class CreateTeamData(

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("team_members")
    val teamMembers: ArrayList<TeamMembersItem>? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: Any? = null,

    @field:SerializedName("team_name")
    val teamName: String? = null
)

data class TeamMembersItem(

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("user_id")
    val userId: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("team_id")
    val teamId: Int? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: Any? = null,

    @field:SerializedName("user")
    val user: User? = null
)

data class User(

    @field:SerializedName("is_block")
    val isBlock: Int? = null,

    @field:SerializedName("is_verify")
    val isVerify: Int? = null,

    @field:SerializedName("created_at")
    val createdAt: String? = null,

    @field:SerializedName("profile_picture")
    val profilePicture: Any? = null,

    @field:SerializedName("device_type")
    val deviceType: String? = null,

    @field:SerializedName("deleted_at")
    val deletedAt: String? = null,

    @field:SerializedName("weekly_situation")
    val weeklySituation: Any? = null,

    @field:SerializedName("weekly_complete_challenge")
    val weeklyCompleteChallenge: Any? = null,

    @field:SerializedName("user_type")
    val userType: String? = null,

    @field:SerializedName("updated_at")
    val updatedAt: String? = null,

    @field:SerializedName("dob")
    val dob: String? = null,

    @field:SerializedName("device_token")
    val deviceToken: String? = null,

    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("id")
    val id: Int? = null,

    @field:SerializedName("email")
    val email: String? = null
)