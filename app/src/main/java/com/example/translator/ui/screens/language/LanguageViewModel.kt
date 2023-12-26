package com.example.translator.ui.screens.language

import androidx.lifecycle.ViewModel
import com.example.translator.utils.Constant.supportedLanguages
import com.example.translator.utils.setLanguage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LanguageViewModel : ViewModel() {
    private val _languageUiState = MutableStateFlow(LanguageUiState())
    val languageUiState: StateFlow<LanguageUiState> = _languageUiState

    fun getDownloadedLanguageModels(downloadedLanguages: List<String>) {
        _languageUiState.value = _languageUiState.value.copy(
            downloadedLanguages = downloadedLanguages.sortedBy { setLanguage(it) },
            notDownloadedLanguages = (supportedLanguages - downloadedLanguages.toSet())
                .sortedBy { setLanguage(it) }
        )
    }
}

data class LanguageUiState(
    val downloadedLanguages: List<String> = emptyList(),
    val notDownloadedLanguages: List<String> = emptyList(),
)