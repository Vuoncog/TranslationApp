package com.example.translator.ui.screens.camera

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.translator.data.Character
import com.example.translator.data.Language
import com.example.translator.utils.convertToLanguageTag
import com.example.translator.utils.languageIdentifier
import com.example.translator.utils.setLanguage
import com.example.translator.utils.textTranslator
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Locale

class CameraViewModel : ViewModel() {

    private val _bitmap = MutableStateFlow<Bitmap?>(null)
    val bitmap: StateFlow<Bitmap?> = _bitmap

    private val _textRecognition = MutableStateFlow("")
    val textRecognition: StateFlow<String> = _textRecognition

    private val _character =
        MutableStateFlow(TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS))
    val character: StateFlow<TextRecognizer> = _character

    private val _cameraUiState = MutableStateFlow(CameraUiState())
    val cameraUiState: StateFlow<CameraUiState> = _cameraUiState

    fun assignPhoto(bitmap: Bitmap) {
        _bitmap.value = bitmap
    }

    fun recognizeText(value: String) {
        _textRecognition.value = value
        _cameraUiState.value = _cameraUiState.value.copy(
            translatedText = "Translating"
        )
    }

    fun assignCharacter(char: Character) {
        _character.value = char.textRecognizer
        _cameraUiState.value = _cameraUiState.value.copy(
            char = char
        )
    }

    fun setTargetLanguage(language: Language) {
        _cameraUiState.value = _cameraUiState.value.copy(
            targetLanguage = language
        )
        Log.d("MLKIT_TARGET", _cameraUiState.value.targetLanguage.name)
    }

    fun setSourceLanguage() {
        if (_textRecognition.value != "") {
            languageIdentifier(
                text = _textRecognition.value,
                onSuccess = {
                    _cameraUiState.value = _cameraUiState.value.copy(
                        sourceLanguage = setLanguage(it)
                    )
                }
            )
        }
    }

    fun translator(text: String) {
        if (_cameraUiState.value.translatorCheck) {
            textTranslator(
                sourceLanguageTag = convertToLanguageTag(_cameraUiState.value.sourceLanguage),
                targetLanguageTag = convertToLanguageTag(_cameraUiState.value.targetLanguage),
                text = text,
                onSuccess = {
                    _cameraUiState.value = _cameraUiState.value.copy(
                        translatedText = it
                    )
                }
            )
        }
    }

    fun checkModel() {
        val models = _cameraUiState.value.models
        val sourceLanguage = convertToLanguageTag(_cameraUiState.value.sourceLanguage)
        val targetLanguage = convertToLanguageTag(_cameraUiState.value.targetLanguage)
        if (models.isNotEmpty()) {
            val checkModel = models.contains(sourceLanguage) && models.contains(targetLanguage)
            _cameraUiState.value = _cameraUiState.value.copy(
                translatorCheck = checkModel
            )
            Log.d(
                "MLKIT_CHECKRUN",
                sourceLanguage + " " + targetLanguage + " " +
                        checkModel.toString()
            )
        }
    }

    fun getDownloadedLanguageModel(models: List<String>) {
        _cameraUiState.value = _cameraUiState.value.copy(
            models = models
        )
    }

    fun isDownloaded(language: Language): Boolean {
        val tag = convertToLanguageTag(language)
        return _cameraUiState.value.models.contains(tag)
    }

    fun resetTranslatedText() {
        _cameraUiState.value = _cameraUiState.value.copy(
            translatedText = "Translating"
        )
    }
}

data class CameraUiState(
    val char: Character = Character.Latin,
    val sourceLanguage: Language = Language.English,
    val targetLanguage: Language = setLanguage(Locale.getDefault().language),
    val translatorCheck: Boolean = false,
    val models: List<String> = emptyList(),
    val translatedText: String = ""
)