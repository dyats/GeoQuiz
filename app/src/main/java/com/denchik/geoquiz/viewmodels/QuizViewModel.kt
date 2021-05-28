package com.denchik.geoquiz.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.denchik.geoquiz.R
import com.denchik.geoquiz.models.Question

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_australia, true, false),
        Question(R.string.question_oceans, true, false),
        Question(R.string.question_mideast, false, false),
        Question(R.string.question_africa, false, false),
        Question(R.string.question_americas, true, false),
        Question(R.string.question_asia, true, false)
    )

    var currentIndex = 0

    var correctAnswers = 0

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    var currentQuestionIsAnswered: Boolean
        get() = questionBank[currentIndex].isAnswered
        set(value){
            questionBank[currentIndex].isAnswered = value
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