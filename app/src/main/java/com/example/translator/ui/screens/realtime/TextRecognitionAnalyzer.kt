package com.example.translator.ui.screens.realtime

import android.media.Image
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.example.translator.utils.Language
import com.example.translator.utils.convertToLanguageTag
import com.example.translator.utils.languageIdentifier
import com.example.translator.utils.textTranslator
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class TextRecognitionAnalyzer(
    private val onDetectedTextUpdated: (String) -> Unit,
    private val onDetectedLanguageUpdated: (String) -> Unit,
    private val sourceLanguage: Language,
    private val targetLanguage: Language,
    private val downloadedLanguages: List<String>
) : ImageAnalysis.Analyzer {

    companion object {
        const val TIMEOUT_MS = 1_000L
    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val textRecognizer: TextRecognizer =
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
            val mediaImage: Image = imageProxy.image ?: run { imageProxy.close(); return@launch }
            val inputImage: InputImage =
                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            suspendCoroutine { continuation ->
                textRecognizer.process(inputImage)
                    .addOnSuccessListener { visionText: Text ->
                        val detectedText: String = visionText.text
                        if (detectedText.isNotBlank()) {
//                            onDetectedTextUpdated(detectedText)
                            languageIdentifier(
                                text = detectedText,
                                onSuccess = {
                                    onDetectedLanguageUpdated(it)
                                }
                            )
                            if (checkModel()) {

                                textTranslator(
                                    sourceLanguageTag = convertToLanguageTag(sourceLanguage),
                                    targetLanguageTag = convertToLanguageTag(targetLanguage),
                                    text = visionText.text,
                                    onSuccess = {
                                        onDetectedTextUpdated(it)
                                    }
                                )
                            }
                        }
                    }
                    .addOnCompleteListener {
                        continuation.resume(Unit)
                    }
            }

            delay(TIMEOUT_MS)
        }.invokeOnCompletion { exception ->
            exception?.printStackTrace()
            imageProxy.close()
        }
    }

    fun checkModel(): Boolean {
        val s = convertToLanguageTag(sourceLanguage)
        val t = convertToLanguageTag(targetLanguage)
        return if (downloadedLanguages.isNotEmpty()) {
            val checkModel = downloadedLanguages.contains(s) && downloadedLanguages.contains(t)
            checkModel
        } else {
            false
        }
    }
}