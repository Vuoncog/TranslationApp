package com.example.translator.ui.screens.text

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.result.contract.ActivityResultContract
import com.example.translator.data.Language
import com.example.translator.utils.convertToLanguageTag

class SpeechToTextContract(language: Language) : ActivityResultContract<Unit, ArrayList<String>?>() {
    private val languageTag = convertToLanguageTag(language)
    override fun createIntent(context: Context, input: Unit): Intent {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
//            Locale.getDefault()
            languageTag
        )
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak Something")
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): ArrayList<String>? {
        if (resultCode != Activity.RESULT_OK){
            return null
        }
        return intent?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
    }
}