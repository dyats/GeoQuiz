package com.denchik.geoquiz.viewmodels

import androidx.lifecycle.ViewModel
import com.denchik.geoquiz.R
import com.denchik.geoquiz.models.Question

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    var currentIndex = 0
    var correctAnswers = 0
    var cheatedAnswers = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    var currentQuestionIsAnswered: Boolean
        get() = questionBank[currentIndex].isAnswered
        set(value){
            questionBank[currentIndex].isAnswered = value
        }

    var currentQuestionIsCheated: Boolean
        get() = questionBank[currentIndex].isCheated
        set(value){
            questionBank[currentIndex].isCheated = value
        }

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext(){
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev(){
        currentIndex = (currentIndex + questionBank.size - 1) % questionBank.size
    }
}