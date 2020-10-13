package com.brian.internals.interfaces

import com.brian.models.AnswersItem

interface ItemClickListener {
  fun onClick(data:AnswersItem, position: Int,correct:Boolean)
}