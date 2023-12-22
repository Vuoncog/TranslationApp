package com.example.translator.ui.screens.realtime

import android.content.Context
import android.view.ViewGroup
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.example.translator.ui.components.LanguageCard
import com.example.translator.ui.screens.camera.CameraPreview
import com.example.translator.utils.Language
import com.example.translator.utils.setLanguage
import com.google.mlkit.vision.text.TextRecognizer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealTimeScreen(
    realtimeViewModel: RealtimeViewModel,
    navController: NavHostController
) {
    val context: Context = LocalContext.current
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val uiState by realtimeViewModel.realtimeUiState.collectAsState()
    val cameraController: LifecycleCameraController =
        remember { LifecycleCameraController(context) }
    var detectedText by remember { mutableStateOf("No text detected yet..") }
    var expanded by remember { mutableStateOf(false) }

    fun onTextUpdated(updatedText: String) {
        detectedText = updatedText
    }
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f),
            contentAlignment = androidx.compose.ui.Alignment.TopStart
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
                            sourceLanguage = uiState.sourceLanguage,
                            targetLanguage = uiState.targetLanguage,
                            onDetectedTextUpdated = ::onTextUpdated,
                            downloadLanguages = uiState.downloadedLanguages,
                            textRecognizer = uiState.textRecognition,
                            onDetectedLanguageUpdated = {
                                realtimeViewModel.setSourceLanguage(
                                    language = setLanguage(it)
                                )
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true },
                verticalAlignment = Alignment.CenterVertically
            )
            {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                LanguageCard(
                    language = uiState.targetLanguage,
                )
            }
            Text(
                text = detectedText,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            )
        }
    }
}

private fun startTextRecognition(
    context: Context,
    cameraController: LifecycleCameraController,
    lifecycleOwner: LifecycleOwner,
    previewView: PreviewView,
    onDetectedTextUpdated: (String) -> Unit,
    onDetectedLanguageUpdated: (String) -> Unit,
    sourceLanguage: Language,
    targetLanguage: Language,
    downloadLanguages: List<String>,
    textRecognizer: TextRecognizer
) {

    cameraController.imageAnalysisTargetSize = CameraController.OutputSize(Int.MAX_VALUE)
    cameraController.setImageAnalysisAnalyzer(
        ContextCompat.getMainExecutor(context),
        TextRecognitionAnalyzer(
            onDetectedTextUpdated = onDetectedTextUpdated,
            onDetectedLanguageUpdated = onDetectedLanguageUpdated,
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage,
            downloadedLanguages = downloadLanguages,
            textRecognizer = textRecognizer
        )
    )

    cameraController.bindToLifecycle(lifecycleOwner)
    previewView.controller = cameraController
}