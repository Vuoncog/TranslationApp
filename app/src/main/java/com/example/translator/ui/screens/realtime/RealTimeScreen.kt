package com.example.translator.ui.screens.realtime

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.example.translator.data.Language
import com.example.translator.utils.boxInfo
import com.example.translator.utils.convertToLanguageTag
import com.example.translator.utils.languageIdentifier
import com.example.translator.utils.offsetInfo
import com.example.translator.utils.setLanguage
import com.example.translator.utils.textRotate
import com.example.translator.utils.textTranslator
import com.google.mlkit.vision.text.Text.TextBlock
import com.google.mlkit.vision.text.TextRecognizer

@Composable
fun RealTimeScreen(
    realtimeViewModel: RealtimeViewModel,
    navController: NavHostController
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp
    val screenWidth = configuration.screenWidthDp
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val uiState by realtimeViewModel.realtimeUiState.collectAsState()
    val cameraController: LifecycleCameraController =
        remember { LifecycleCameraController(context) }

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopStart
    ) {
        AndroidView(
            modifier = Modifier
                .fillMaxSize(),
            factory = { context ->
                PreviewView(context).apply {
                    setBackgroundColor(Color.Black.toArgb())
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    scaleType = PreviewView.ScaleType.FILL_START
                }.also { previewView ->
                    startTextRecognition(
                        context = context,
                        cameraController = cameraController,
                        lifecycleOwner = lifecycleOwner,
                        previewView = previewView,
                        textRecognizer = uiState.textRecognition,
                        onDetectedTextBlocksUpdated = {
                            realtimeViewModel.setTextBlocks(it)
                        },
                        onRotateUpdated = {
                            realtimeViewModel.setRotate(it)
                        }
                    )
                }
            }
        )

        IconButton(
            onClick = {
                cameraController.cameraSelector =
                    if (cameraController.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else CameraSelector.DEFAULT_BACK_CAMERA
            },
            modifier = Modifier
                .offset(16.dp, 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Cameraswitch,
                contentDescription = "Switch camera",
                tint = Color.White
            )
        }
    }

    if (uiState.textBlocks.isNotEmpty()) {
        uiState.textBlocks.forEach { block ->
            DrawAndTranslate(
                block = block,
                rotate = uiState.rotate,
                screenWidth = screenWidth,
                screenHeight = screenHeight,
                targetLanguage = uiState.targetLanguage,
                downloadedLanguages = uiState.downloadedLanguages
            )
        }
    }
}

@Composable
fun DrawAndTranslate(
    block: TextBlock,
    rotate: Int,
    screenWidth: Int,
    screenHeight: Int,
    targetLanguage: Language,
    downloadedLanguages: List<String>
) {
    val textBlock = block.boundingBox!!
    val boxInfo = boxInfo(
        rotate = rotate,
        textBlock = textBlock
    )
    val offsetInfo = offsetInfo(
        rotate = rotate,
        textBlock = textBlock,
        width = screenWidth / 3 * 4,
        height = screenHeight
    )
    val width = boxInfo.first
    val height = boxInfo.second
    val offsetX = offsetInfo.first
    val offsetY = offsetInfo.second

    var textTranslated by remember { mutableStateOf(block.text) }
    var isTranslated by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = offsetInfo) {
        translateBlock(
            text = textTranslated,
            targetLanguage = targetLanguage,
            downloadedLanguages = downloadedLanguages,
            onSuccess = { translatedText: String, translated: Boolean ->
                textTranslated = translatedText
                isTranslated = translated
            }
        )
    }

    Canvas(
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .offset(
                x = offsetX.dp,
                y = offsetY.dp
            ),
        onDraw = {
            drawRect(
                color = if (isTranslated) Color.Green else Color.Red,
                style = Stroke(
                    width = 1f
                )
            )
        }
    )
    Text(
        text = textTranslated,
        color = if (isTranslated) Color.Green else Color.Red,
        modifier = Modifier
            .offset(
                x = offsetX.dp,
                y = offsetY.dp
            )
            .rotate(textRotate(rotate.toFloat())),
    )
}

private fun translateBlock(
    text: String,
    targetLanguage: Language,
    downloadedLanguages: List<String>,
    onSuccess: (String, Boolean) -> Unit
) {
    languageIdentifier(
        text = text,
        onSuccess = {
            if (checkModel(
                    sourceLanguage = setLanguage(it),
                    targetLanguage = targetLanguage,
                    downloadedLanguages = downloadedLanguages
                )
            ) {
                textTranslator(
                    text = text,
                    sourceLanguageTag = it,
                    targetLanguageTag = convertToLanguageTag(targetLanguage),
                    onSuccess = { translatedText ->
                        onSuccess(translatedText, true)
                    }
                )
            } else {
                onSuccess("", false)
            }

        }
    )
}

private fun checkModel(
    sourceLanguage: Language,
    targetLanguage: Language,
    downloadedLanguages: List<String>
): Boolean {
    val s = convertToLanguageTag(sourceLanguage)
    val t = convertToLanguageTag(targetLanguage)
    return if (downloadedLanguages.isNotEmpty()) {
        val checkModel = downloadedLanguages.contains(s) && downloadedLanguages.contains(t)
        checkModel
    } else {
        false
    }
}

private fun startTextRecognition(
    context: Context,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onDetectedTextBlocksUpdated: (List<TextBlock>) -> Unit,
    onRotateUpdated: (Int) -> Unit,
    textRecognizer: TextRecognizer,
) {

//    cameraController.imageAnalysisTargetSize = CameraController.OutputSize()
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        TextRecognitionAnalyzer(
            onDetectedTextBlocksUpdated = onDetectedTextBlocksUpdated,
            textRecognizer = textRecognizer,
            onRotateUpdated = onRotateUpdated
        )
    )

    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}