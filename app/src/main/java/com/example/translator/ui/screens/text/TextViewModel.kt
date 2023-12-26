package com.example.translator.ui.screens.text

import androidx.lifecycle.ViewModel
import com.example.translator.data.Language
import com.example.translator.utils.convertToLanguageTag
import com.example.translator.utils.setLanguage
import com.example.translator.utils.textTranslator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class TextViewModel : ViewModel() {
    private val _textUiState = MutableStateFlow(TextUiState())
    val textUiState: StateFlow<TextUiState> = _textUiState

    fun setText(text: String) {
        _textUiState.value = _textUiState.value.copy(
            text = text
        )
    }

    fun translator() {
        val sourceTag = convertToLanguageTag(_textUiState.value.sourceLanguage)
        val targetTag = convertToLanguageTag(_textUiState.value.targetLanguage)
        textTranslator(
            sourceLanguageTag = sourceTag,
            targetLanguageTag = targetTag,
            text = _textUiState.value.text,
            onSuccess = { translatedText ->
                _textUiState.value = _textUiState.value.copy(
                    translatedText = translatedText
                )
            }
        )
    }

    fun setSourceLanguage(language: Language) {
        _textUiState.value = _textUiState.value.copy(
            sourceLanguage = language
        )
    }

    fun setTargetLanguage(language: Language) {
        _textUiState.value = _textUiState.value.copy(
            targetLanguage = language
        )
    }

    fun getDownloadedLanguageModel(models: List<String>) {
        _textUiState.value = _textUiState.value.copy(
            models = models
        )
//        getDownloadedAllModel(
//            onSuccess = {
//                _textUiState.value = _textUiState.value.copy(
//                    models = it
//                )
//            }
//        )
    }
}

data class TextUiState(
    val text: String = "",
    val translatedText: String = "",
    val sourceLanguage: Language = setLanguage(Locale.getDefault().language),
    val targetLanguage: Language = Language.English,
    val models: List<String> = emptyList()
)