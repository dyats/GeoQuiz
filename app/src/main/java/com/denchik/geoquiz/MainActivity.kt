package com.denchik.geoquiz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.denchik.geoquiz.models.Question
import com.denchik.geoquiz.viewmodels.QuizViewModel

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var prevButton: ImageButton
    private lateinit var questionTextView: TextView
    private lateinit var corAnswersTextView: TextView

    private val quizViewModel: QuizViewModel by lazy{
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
        questionTextView = findViewById(R.id.question_text_view)
        corAnswersTextView = findViewById(R.id.correct_answers_text_view)

        trueButton.setOnClickListener { view: View ->
            checkAnswer(true)
            nextButton.callOnClick()
        }

        falseButton.setOnClickListener { view: View ->
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

        questionTextView.setOnClickListener{view: View ->
            nextButton.callOnClick()
        }

        corAnswersTextView.setOnClickListener {view: View ->
            Toast.makeText(this, "You got ${quizViewModel.correctAnswers} right answers", Toast.LENGTH_LONG).show()
        }

        updateQuestion()
    }

    override fun onStart(){
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume(){
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause(){
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onStop(){
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy(){
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion(){
        if(!quizViewModel.currentQuestionIsAnswered){
            trueButton.isEnabled = true
            falseButton.isEnabled = true
        }
        else{
            trueButton.isEnabled = false
            falseButton.isEnabled = false
        }

        val questionTextResId = quizViewModel.currentQuestionText
        questionTextView.setText(questionTextResId)
        corAnswersTextView.setText("Correct answers: ${quizViewModel.correctAnswers}")
    }

    private fun checkAnswer(userAnswer: Boolean){
        val correctAnswer = quizViewModel.currentQuestionAnswer

        val messageResId = if (userAnswer == correctAnswer) {
            quizViewModel.correctAnswers++
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }

        quizViewModel.currentQuestionIsAnswered = true

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }
}