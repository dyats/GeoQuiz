package com.denchik.geoquiz.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.denchik.geoquiz.R

private const val EXTRA_ANSWER_IS_TRUE =  "com.denchik.geoquiz.activities.answer_is_true"
private const val EXTRA_COUNT_OF_SHOWN_ANSWERS = "com.denchik.geoquiz.activities.count_of_shown_answers"
const val EXTRA_ANSWER_SHOWN = "com.denchik.geoquiz.activities.answer_shown"
const val EXTRA_ANSWER_CHEATED = "com.denchik.geoquiz.activities.answer_cheated"

class CheatActivity : AppCompatActivity() {

    private var answerIsTrue = false

    private lateinit var answerTextView: TextView
    private lateinit var showAnswerButton: Button
    private lateinit var apiLevelTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        answerTextView = findViewById(R.id.answer_text_view)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiLevelTextView = findViewById(R.id.api_level_text_view)
        showAnswerButton.setOnClickListener {
            val answerText = when {
                answerIsTrue -> R.string.true_button
                else -> R.string.false_button
            }
            val countOfAnswersCheated = intent.getIntExtra(EXTRA_COUNT_OF_SHOWN_ANSWERS, 0);

            answerTextView.setText(answerText)
            setAnswerShownResult(true, countOfAnswersCheated + 1)

            Toast.makeText(this, R.string.judgment_toast, Toast.LENGTH_SHORT).show()
        }
        apiLevelTextView.setText(showAndroidVersion())
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean, countOfShownAnswers: Int): Intent{
            return Intent(packageContext, CheatActivity::class.java).apply{
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
                putExtra(EXTRA_COUNT_OF_SHOWN_ANSWERS, countOfShownAnswers)
            }
        }
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean, countOfShownAnswers: Int){
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
            putExtra(EXTRA_ANSWER_CHEATED, countOfShownAnswers)
        }
        setResult(Activity.RESULT_OK, data)
    }

    private fun showAndroidVersion(): String {
        return "API Level ${Build.VERSION.SDK_INT}"
    }
}