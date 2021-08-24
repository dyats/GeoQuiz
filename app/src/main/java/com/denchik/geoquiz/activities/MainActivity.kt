package com.denchik.geoquiz.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.denchik.geoquiz.R
import com.denchik.geoquiz.viewmodels.QuizViewModel

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val REQUEST_CODE_CHEAT = 0

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var corAnswersTextView: TextView
    private lateinit var cheatedAnswersTextView: TextView

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        setContentView(R.layout.activity_main)

        val currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0
        quizViewModel.currentIndex = currentIndex

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        prevButton = findViewById(R.id.prev_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        corAnswersTextView = findViewById(R.id.correct_answers_text_view)
        cheatedAnswersTextView = findViewById(R.id.cheated_answers_text_view)

        trueButton.setOnClickListener {
            checkAnswer(true)
            nextButton.callOnClick()
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
            nextButton.callOnClick()
        }

        nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        prevButton.setOnClickListener {
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        cheatButton.setOnClickListener {
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this, answerIsTrue)
            startActivityForResult(intent, REQUEST_CODE_CHEAT)
        }

        questionTextView.setOnClickListener {
            nextButton.callOnClick()
        }

        corAnswersTextView.setOnClickListener {
            Toast.makeText(
                this,
                "You got ${quizViewModel.correctAnswers} right answers",
                Toast.LENGTH_LONG
            ).show()
        }

        updateQuestion()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode != Activity.RESULT_OK){
            return
        }

        if(requestCode == REQUEST_CODE_CHEAT){
            quizViewModel.currentQuestionIsCheated = data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        if (!quizViewModel.currentQuestionIsAnswered) {
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        } else {
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        }

        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        corAnswersTextView.text = getString(R.string.correct_answers, quizViewModel.correctAnswers)
        cheatedAnswersTextView.text = getString(R.string.cheated_answers, quizViewModel.cheatedAnswers)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer


        val messageResId =
            when {
                quizViewModel.currentQuestionIsCheated -> {
                    quizViewModel.cheatedAnswers++
                    R.string.judgment_toast
                }
                userAnswer == correctAnswer -> {
                    quizViewModel.correctAnswers++
                    R.string.correct_toast
                }
                else -> R.string.incorrect_toast
            }

        quizViewModel.currentQuestionIsAnswered = true

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}