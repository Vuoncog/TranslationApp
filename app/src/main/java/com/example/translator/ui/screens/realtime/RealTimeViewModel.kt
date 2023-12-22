package com.example.translator.ui.screens.realtime

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.translator.utils.Constant
import com.example.translator.utils.Language
import com.example.translator.utils.convertToLanguageTag
import com.example.translator.utils.setLanguage
import com.google.mlkit.nl.translate.TranslateLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class RealtimeViewModel : ViewModel(){
    private val _realtimeUiState = MutableStateFlow(RealtimeUiState())
    val realtimeUiState: StateFlow<RealtimeUiState> = _realtimeUiState

    fun setTargetLanguage(language: Language){
        _realtimeUiState.value = _realtimeUiState.value.copy(
            targetLanguage = language
        )
    }

    fun setSourceLanguage(language: Language){
        _realtimeUiState.value = _realtimeUiState.value.copy(
            sourceLanguage = language
        )
    }

    fun getDownloadedLanguageModels(downloadedLanguages: List<String>) {
        _realtimeUiState.value = _realtimeUiState.value.copy(
            downloadedLanguages = downloadedLanguages.sortedBy { setLanguage(it) },
        )
    }


}

data class RealtimeUiState(
    val sourceLanguage: Language = setLanguage(TranslateLanguage.ENGLISH),
    val targetLanguage: Language = setLanguage(Locale.getDefault().language),
    val downloadedLanguages: List<String> = emptyList(),
)