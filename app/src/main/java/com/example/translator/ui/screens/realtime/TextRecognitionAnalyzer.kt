package com.example.translator.ui.screens.realtime

import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.Image
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.core.graphics.toRectF
import com.example.translator.utils.Character
import com.example.translator.utils.Language
import com.example.translator.utils.convertToLanguageTag
import com.example.translator.utils.languageIdentifier
import com.example.translator.utils.setLanguage
import com.example.translator.utils.textTranslator
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.Text.TextBlock
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
    private val onDetectedTextBlocksUpdated: (List<TextBlock>) -> Unit,
    private val onRotateUpdated: (Int) -> Unit,
    private var sourceLanguage: Language,
    private val targetLanguage: Language,
    private val downloadedLanguages: List<String>,
    private val textRecognizer: TextRecognizer
) : ImageAnalysis.Analyzer {

    companion object {
        const val TIMEOUT_MS = 100L
    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @OptIn(ExperimentalGetImage::class)
    override fun analyze(imageProxy: ImageProxy) {
        scope.launch {
            val mediaImage: Image = imageProxy.image ?: run { imageProxy.close(); return@launch }
            val matrix = Matrix().apply {
                postRotate(imageProxy.imageInfo.rotationDegrees.toFloat())
            }
            Log.d("MLROTATE", imageProxy.imageInfo.rotationDegrees.toString())
            onRotateUpdated(imageProxy.imageInfo.rotationDegrees)
            val rotateImage = Bitmap.createBitmap(
                imageProxy.toBitmap(),
                0,
                0,
                imageProxy.width,
                imageProxy.height,
                matrix,
                true
            )
//            val inputImage: InputImage =
//                InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

            val inputImage: InputImage = InputImage.fromBitmap(rotateImage, 0)

            suspendCoroutine { continuation ->
                textRecognizer.process(inputImage)
                    .addOnSuccessListener { visionText: Text ->
                        onDetectedTextBlocksUpdated(visionText.textBlocks.toList())
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
}